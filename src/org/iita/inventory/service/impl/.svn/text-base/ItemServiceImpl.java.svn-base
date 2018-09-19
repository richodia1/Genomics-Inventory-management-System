/**
 * 
 */
package org.iita.inventory.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.iita.inventory.model.ContainerType;
import org.iita.inventory.model.Item;
import org.iita.inventory.model.ItemType;
import org.iita.inventory.model.Lot;
import org.iita.inventory.service.ItemService;
import org.iita.util.PagedResult;
import org.springframework.security.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author mobreza
 */
public class ItemServiceImpl implements ItemService {
	private static final Log LOG = LogFactory.getLog(ItemService.class);
	private EntityManager entityManager;

	/**
	 * @return the entityManager
	 */
	public EntityManager getEntityManager() {
		return this.entityManager;
	}

	/**
	 * @param entityManager the entityManager to set
	 */
	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	/*
	 * (non-Javadoc)
	 * @see org.iita.inventory.service.ItemService#delete(org.iita.inventory.model .Item)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	@Secured( { "ROLE_MANAGER" })
	public void delete(Item item) {
		// only role maanger allowed to delete item completely
		for (Lot lot : item.getLots()) {
			if (lot.getStatus() != -100 && lot.getQuantity() != 0)
				throw new RuntimeException("Cannot delete Item, has lots with non-zero quantity.");
		}
		// delete Transactions
		this.entityManager.createQuery("delete from Transaction2 t where t.lot in (select l from Lot l where l.item=:item)").setParameter("item", item)
				.executeUpdate();
		// delete Migrations
		this.entityManager.createQuery("delete from Migration m where m.lot in (select l from Lot l where l.item=:item)").setParameter("item", item)
				.executeUpdate();
		// delete QuantityUpdates
		this.entityManager.createQuery("delete from QuantityUpdate qu where qu.lot in (select l from Lot l where l.item=:item)").setParameter("item", item)
				.executeUpdate();
		// delete LotTraitValues
		this.entityManager.createQuery("delete from LotTraitValue ltv where ltv.entity in (select l from Lot l where l.item=:item)").setParameter("item", item)
				.executeUpdate();
		// update parents -- break chain
		this.entityManager.createQuery("update Lot l set l.parent1=null where l.parent1 in (select l from Lot l where l.item=:item)")
				.setParameter("item", item).executeUpdate();

		// delete Lots -- one by one for reindexing
		for (Lot lot : (List<Lot>) this.entityManager.createQuery("from Lot l where l.item=:item").setParameter("item", item).getResultList())
			this.entityManager.remove(lot);
		// remove item
		this.entityManager.remove(item);
	}

	// Delete Item type record
	@Override
	@Transactional
	public void deletetype(ItemType itype) {
		this.entityManager.remove(itype);
	}

	// Delete Container type record
	@Override
	@Transactional
	public boolean deleteContainerType(ContainerType ctype) {
		try {
			this.entityManager.remove(ctype);
			return true;
		} catch (javax.persistence.NoResultException e) {
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.iita.inventory.service.ItemService#load(long)
	 */
	@Override
	@Transactional(readOnly = true)
	public Item find(long id) {
		return this.entityManager.find(Item.class, id);
	}

	@Override
	@Transactional(readOnly = true)
	public ItemType findType(long id) {
		return this.entityManager.find(ItemType.class, id);
	}

	@Override
	@Transactional(readOnly = true)
	public ContainerType findContainerType(long id) {
		return this.entityManager.find(ContainerType.class, id);
	}

	/*
	 * (non-Javadoc)
	 * @see org.iita.inventory.service.ItemService#find(org.iita.inventory.model. ItemType, java.lang.String)
	 */
	@Override
	@Transactional(readOnly = true)
	public Item find(ItemType itemType, String itemName) throws NonUniqueResultException {
		if (itemType == null)
			return null;
		if (itemType.getId() == null)
			return null;

		try {
			return (Item) this.entityManager.createQuery("from Item i where i.itemType=:itemType and i.name=:name").setParameter("itemType", itemType)
					.setParameter("name", itemName).getSingleResult();
		} catch (javax.persistence.NoResultException e) {
			LOG.info("No item '" + itemName + "' in item type " + itemType.getName());
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.iita.inventory.service.ItemService#store(org.iita.inventory.model .Item)
	 */
	@Override
	@Transactional
	public void store(Item item) {
		item.setDateLastModified(new Date());
		if (item.getId() == null)
			this.entityManager.persist(item);
		else
			this.entityManager.merge(item);
	}

	@Override
	@Transactional
	public void storeType(ItemType itype) {
		// System.out.println(itype.getId() + " test.");
		if (itype.getId() == null)
			this.entityManager.persist(itype);
		else
			this.entityManager.merge(itype);
	}

	/*
	 * (non-Javadoc)
	 * @see org.iita.inventory.service.ItemService#search(java.lang.String, int, int)
	 */
	@Override
	@Transactional(readOnly = true)
	public PagedResult<Item> search(String searchQuery, int startat, int pagesize) {
		PagedResult<Item> sr = new PagedResult<Item>(startat, pagesize);
		if (searchQuery == null)
			return sr;

		LOG.debug("Searching for: " + searchQuery + " st=" + startat + " ps=" + pagesize);

		FullTextEntityManager ftEm = Search.createFullTextEntityManager(this.entityManager);
		org.apache.lucene.search.Query luceneQuery = null;
		MultiFieldQueryParser parser = new MultiFieldQueryParser(new String[] { "name", "alternativeIdentifier", "description", "itemType.name", "notes" },
				new StandardAnalyzer());
		try {
			luceneQuery = parser.parse(searchQuery);
		} catch (ParseException e) {
			LOG.debug(e);
			return null;
		}
		if (luceneQuery != null) {
			javax.persistence.Query query = ftEm.createFullTextQuery(luceneQuery, Item.class);			
			sr.setResults(query.setMaxResults(pagesize).setFirstResult(startat).getResultList());
			sr.setTotalHits(((org.hibernate.search.jpa.impl.FullTextQueryImpl) query).getResultSize());
		}
		return sr;
	}

	/*
	 * (non-Javadoc)
	 * @see org.iita.inventory.service.ItemService#findByName(java.lang.String)
	 */
	@Override
	@Transactional(readOnly = true)
	public Item findByName(String itemName) {
		LOG.debug("findByName: " + itemName);
		try {
			if (itemName.contains("/")) {
				String[] i = itemName.split("/");
				LOG.trace("findByName: " + i.length + "  " + i);
				return (Item) this.entityManager.createQuery("from Item i where i.itemType.shortName=? and i.name=?").setParameter(1, i[0]).setParameter(2,
						i[1]).setMaxResults(1).getSingleResult();
			} else
				return (Item) this.entityManager.createQuery("from Item i where i.name=?").setParameter(1, itemName).setMaxResults(1).getSingleResult();
		} catch (NoResultException nre) {
			LOG.info("No item named \"" + itemName + "\" found in Inventory");
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<ItemType> listItemTypes() {
		return this.entityManager.createQuery("from ItemType it order by it.name").getResultList();
	}

	/*
	 * (non-Javadoc)
	 * @see org.iita.inventory.service.ItemService#getContainerTypes()
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<ContainerType> getContainerTypes() {
		return this.entityManager.createQuery("from ContainerType ct order by ct.name").getResultList();
	}

	@Override
	@Transactional
	public void storeContainerType(ContainerType ctype) {
		System.out.println(ctype.getId() + " test.");
		if (ctype.getId() == null)
			this.entityManager.persist(ctype);
		else
			this.entityManager.merge(ctype);
	}

	/**
	 * @see org.iita.inventory.service.ItemService#findItemsByName(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Item> findItemsByName(String itemNames) {
		if (itemNames == null || itemNames.length() == 0) {
			LOG.warn("No item names provided in input string. String is empty.");
			return null;
		}
		String[] names = itemNames.split("[, \\n\\r]+");
		if (names.length == 0) {
			LOG.warn("No item names provided in input string: " + itemNames);
			return null;
		}

		List<Item> results = new ArrayList<Item>();
		Query query = this.entityManager.createQuery("from Item i where i.name=:name");
		for (String name : names) {
			name = name.trim();
			LOG.debug("Searching for item '" + name + "'");
			query.setParameter("name", name);
			results.addAll(query.getResultList());
		}

		LOG.info("Found " + results.size() + " items from list of " + names.length + " names");
		return results;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Lot> findValidLots(Item item) {
		//LOG.info("KEN -- LOT ITEM NAME: " + item.getName() + ", ID:" + item.getId());
		List<Lot> lots = (List<Lot>) this.entityManager.createQuery("from Lot l where l.item=:item and l.quantity>0 and l.status<>-100").setParameter("item", item).getResultList();
		//LOG.info("KEN FINISH -- LOT ITEM NAME: " + item.getName());
		// and (YEAR(l.lastUpdated)>=2013 or l.lastUpdated IS NULL)
		return lots;
	}
}
