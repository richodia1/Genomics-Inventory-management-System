/**
 * 
 */
package org.iita.inventory.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.iita.inventory.barcode.BarcodingException;
import org.iita.inventory.model.ContainerType;
import org.iita.inventory.model.FieldVariables;
import org.iita.inventory.model.InVitroLot;
import org.iita.inventory.model.Item;
import org.iita.inventory.model.ItemType;
import org.iita.inventory.model.Location;
import org.iita.inventory.model.Lot;
import org.iita.inventory.model.Migration;
import org.iita.inventory.model.QuantityUpdate;
import org.iita.inventory.model.QuantityUpdateBulk;
import org.iita.inventory.model.SeedLot;
import org.iita.inventory.model.Transaction2;
import org.iita.inventory.model.Transaction2.Type;
import org.iita.inventory.service.BarcodingService;
import org.iita.inventory.service.InventoryException;
import org.iita.inventory.service.LocationException;
import org.iita.inventory.service.LotService;
import org.iita.security.model.User;
import org.iita.service.XLSDataImportService;
import org.iita.service.impl.XLSImportException;
import org.iita.util.PagedResult;
import org.springframework.security.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author mobreza, koraegbunam
 */
public class LotServiceImpl implements LotService {
	/**
	 * 
	 */
	private static final Method[] METHOD_ARRAY = new Method[] {};
	private static final Log log = LogFactory.getLog(LotService.class);
	private EntityManager entityManager;
	private XLSDataImportService importService;
	private BarcodingService barcodingService;

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
	
	/**
	 * @param importService the importService to set
	 */
	public void setXlsImportService(XLSDataImportService importService) {
		this.importService = importService;
	}
	
	/**
	 * @param barcodingService the barcodingService to set
	 */
	public void setBarcodingService(BarcodingService barcodingService) {
		this.barcodingService = barcodingService;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.iita.inventory.service.LotService#delete(long)
	 */
	@Override
	@Transactional
	public void delete(Lot lot) {
		log.warn("Delete of " + lot.getId() + " requested.");
		this.entityManager.remove(lot);
	}

	/*
	 * (non-Javadoc)
	 * @see org.iita.inventory.service.LotService#load(long)
	 */
	@Override
	@Transactional(readOnly = true)
	public Lot load(long id) {

		System.out.println("LOT ID INNER: " + id);
		return this.entityManager.find(Lot.class, id);
		//Lot lot = new Lot();
		//System.out.println("EM: " + this.entityManager);
		
		/*if(this.entityManager == null){
			System.out.println("System properties size(): " + System.getProperties().size());
            EntityManagerFactory factory= Persistence.createEntityManagerFactory("inventory", System.getProperties());
            this.entityManager = factory.createEntityManager();
            System.out.println("EntityManager initialized using EntityManagerFactory");
		}
		 
		lot = this.entityManager.find(Lot.class, id);
		if(lot!=null){
			return lot;
		}else{
			return null;
		}*/
		//EntityManager em = new EntityManagerImpl(null, null, null, false, null, null);
		//return em.find(Lot.class, id);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.iita.inventory.service.LotService#loadByBarcode(long)
	 */
	@Override
	@Transactional(readOnly = true)
	public Lot loadByBarcode(long barCode) {
		/*
		 * BarCode barcode=this.entityManager.find(BarCode.class, barCode); if (barcode!=null) { if (barcode.getLot()!=null) return barcode.getLot(); else
		 * log.warn("No lot assigned to barcode " + barCode); } else { log.warn("No such barcode registered"); }
		 */
		try {
			return (Lot) this.entityManager.createQuery("from Lot l where l.barCode.id=?").setParameter(1, barCode).getSingleResult();
		} catch (javax.persistence.NoResultException ex) {
			log.warn("No lot for barcode: " + barCode);
			return null;
		}
	}

	/**
	 * Store all lots in the list.
	 */
	@Transactional
	public void store(List<Lot> lots) {
		for (Lot lot : lots)
			this.store(lot);
	}

	/*
	 * (non-Javadoc)
	 * @see org.iita.inventory.service.LotService#store(org.iita.inventory.model.Lot)
	 */
	@Override
	@Transactional
	public void store(Lot lot) {
		Date lastUpdate = new Date();

		if (lot.getId() == null) {
			// initial values
			lot.setInitialQuantity(lot.getQuantity());
			lot.setInitialScale(lot.getScale());
			lot.setStatus(1); // available
			this.entityManager.persist(lot);

			// create add transaction
			if (lot.getQuantity() != 0) {
				Transaction2 transaction = new Transaction2();
				transaction.setLot(lot);
				transaction.setDate(lastUpdate);
				transaction.setSubtype("INITIAL");
				transaction.setQuantity(lot.getQuantity());
				transaction.setScale(lot.getScale());
				this.entityManager.persist(transaction);
			}
		} else {
			Lot existingRecord = null;

			// Create a new entity manager and load existing lot data
			EntityManager secondEntityManager = org.iita.struts.PersistenceUtil.getEntityManager();
			existingRecord = (Lot) secondEntityManager.find(Lot.class, lot.getId());
			secondEntityManager.close();

			Transaction2 transaction = null;
			this.entityManager.merge(lot);

			// check existing record for differences in quantity & scale (compare by value)
			if (existingRecord != null) {
				// check if scale changed
				if (!existingRecord.getScale().equals(lot.getScale())) {
					log.debug("Scale changed!");
					// create remove transaction
					transaction = new Transaction2();
					transaction.setLot(lot);
					transaction.setDate(lastUpdate);
					transaction.setSubtype("AUTO");
					transaction.setQuantity(-existingRecord.getQuantity());
					transaction.setScale(existingRecord.getScale());
					this.entityManager.persist(transaction);

					// create add transaction
					transaction = new Transaction2();
					transaction.setLot(lot);
					transaction.setDate(lastUpdate);
					transaction.setSubtype("AUTO");
					transaction.setQuantity(lot.getQuantity());
					transaction.setScale(lot.getScale());
					this.entityManager.persist(transaction);

				} else if (existingRecord.getQuantity().doubleValue() != lot.getQuantity().doubleValue()) {
					// create change transaction
					log.debug("Quantity change detected: ");
					// we have a modification in quantities
					transaction = new Transaction2();
					transaction.setLot(lot);
					transaction.setDate(lastUpdate);
					transaction.setSubtype("RESET");
					transaction.setQuantity(lot.getQuantity());
					transaction.setScale(lot.getScale());
					this.entityManager.persist(transaction);
				}
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Throwable.class)
	public void importLots(List<? extends Lot> lots) {
		for (Lot l : lots)
			importLot(l);
	}

	/*
	 * (non-Javadoc)
	 * @see org.iita.inventory.service.LotService#store(org.iita.inventory.model.Lot)
	 */
	@Override
	@Transactional(rollbackFor = Throwable.class)
	public void importLot(Lot lot) {
		if (lot.getId() == null) {
			// check container
			if (!this.entityManager.contains(lot.getContainer())) {
				if (lot.getContainer().getId() == null)
					this.entityManager.persist(lot.getContainer());
				else
					lot.setContainer(this.entityManager.find(ContainerType.class, lot.getContainer().getId()));
			}

			// check item type
			if (!this.entityManager.contains(lot.getItem().getItemType())) {
				if (lot.getItem().getItemType().getId() == null)
					this.entityManager.persist(lot.getItem().getItemType());
				else
					lot.getItem().setItemType(this.entityManager.find(ItemType.class, lot.getItem().getItemType().getId()));
			}

			// check item
			if (!this.entityManager.contains(lot.getItem())) {
				if (lot.getItem().getId() == null)
					this.entityManager.persist(lot.getItem());
				else
					lot.setItem(this.entityManager.find(Item.class, lot.getItem().getId()));
			}

			// check location
			if (!this.entityManager.contains(lot.getLocation())) {
				lot.setLocation(ensureLocation(lot.getLocation()));
			}

			this.entityManager.persist(lot);
		} else {
			this.entityManager.merge(lot);
		}
	}

	/**
	 * Create locations
	 * 
	 * @param location
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	private Location ensureLocation(Location location) {
		if (location.getParent() != null)
			location.setParent(ensureLocation(location.getParent()));
		if (location.getId() == null) {
			List<Location> foo = this.entityManager.createQuery("from Location l where l.parent=? and l.name=?").setParameter(1, location.getParent())
					.setParameter(2, location.getName()).setMaxResults(1).getResultList();
			if (foo.size() == 0)
				this.entityManager.persist(location);
			else
				location = (Location) foo.get(0);
		} else
			location = this.entityManager.find(Location.class, location.getId());
		return location;
	}

	/*
	 * (non-Javadoc)
	 * @see org.iita.inventory.service.LotService#list(org.iita.inventory.model.Location , int, int)
	 */
	@Override
	@Transactional(readOnly = true)
	public PagedResult<Lot> list(Location location, int startAt, int pageSize, boolean showHiddenLots) {
		return list(location, 0, startAt, pageSize, showHiddenLots);
	}

	/**
	 * @see org.iita.inventory.service.LotService#list(org.iita.inventory.model.Location, int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Lot> list(Location location, int maxRecords) {
		return this.entityManager
				.createQuery(
						"from Lot lot where lot.quantity>0 and lot.status=1 and (lot.location=:location or lot.location in (select pk.child from LocationFlat lf where lf.pk.parent=:location)) order by lot.item.name, lot.line ASC")
				.setParameter("location", location).setMaxResults(maxRecords).getResultList();
	}

	/*
	 * (non-Javadoc)
	 * @see org.iita.inventory.service.LotService#list(org.iita.inventory.model.Location , int, int)
	 */
	@Override
	@Transactional(readOnly = true)
	public PagedResult<Lot> list(Location location, int maxDepth, int startAt, int pageSize, boolean showHiddenLots) {
		PagedResult<Lot> results = new PagedResult<Lot>(startAt, pageSize);

		if (location != null)
			log.info("Listing lots at location '" + location.getName() + "' including children to depth " + maxDepth);
		else
			log.info("Listing lots at root location" + "' including children to depth " + maxDepth);

		Session session = (Session) this.entityManager.getDelegate();
		Criteria crit = session.createCriteria(Lot.class);
		crit.createCriteria("location").addOrder(Order.asc("name"));
		crit.createCriteria("item").addOrder(Order.asc("name"));
		// if displaying only valid stuff
		if (!showHiddenLots)
			crit.add(Restrictions.and(Restrictions.ne("status", -100), Restrictions.ne("quantity", 0.0d)));
		if (maxDepth == 0)
			crit.add(Restrictions.eq("location", location));
		else
			crit.add(Restrictions.in("location", location.getSubset(maxDepth)));
		crit.setFirstResult(startAt).setMaxResults(pageSize);

		results.setResults(crit.list());

		// count
		crit = session.createCriteria(Lot.class);
		crit.setProjection(Projections.rowCount());
		// if displaying only valid stuff
		//log.info("LOCATION: " + location.getId() + ", LOCATION NAME: " + location.getName());
		if (!showHiddenLots)
			crit.add(Restrictions.and(Restrictions.ne("status", -100), Restrictions.ne("quantity", 0.0d)));
		if (maxDepth == 0)
			crit.add(Restrictions.eq("location", location));
		else
			crit.add(Restrictions.in("location", location.getSubset(maxDepth)));
		results.setTotalHits((Integer) crit.list().get(0));

		//
		//		if (location == null) {
		//			results.setResults(this.entityManager.createQuery("from Lot l where l.location=? order by l.location.name, l.item.name").setParameter(1, location)
		//					.setFirstResult(startAt).setMaxResults(pageSize).getResultList());
		//			results.setTotalHits(((Long) this.entityManager.createQuery("select count(*) from Lot l where l.location=? order by l.location.name, l.item.name")
		//					.setParameter(1, location).getSingleResult()).intValue());
		//		} else {
		//			results.setResults(this.entityManager.createQuery("from Lot l where l.location in (:locations) order by l.location.name, l.item.name")
		//					.setParameter("locations", location.getSubset(maxDepth)).setFirstResult(startAt).setMaxResults(pageSize).getResultList());
		//			results.setTotalHits(((Long) this.entityManager.createQuery(
		//					"select count (*) from Lot l where l.location in (:locations) order by l.location.name, l.item.name").setParameter("locations",
		//					location.getSubset(maxDepth)).getSingleResult()).intValue());
		//		}

		return results;
	}

	/*
	 * (non-Javadoc)
	 * @see org.iita.inventory.service.LotService#getLots(java.util.List)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Lot> getLots(List<Long> selectedLots) {
		// no data
		if (selectedLots == null || selectedLots.size() == 0)
			return null;
		return this.entityManager.createQuery("from Lot l where l.id in (:ids)").setParameter("ids", selectedLots).getResultList();
	}

	/**
	 * @see org.iita.inventory.service.LotService#getLotsByBarcode(java.util.List)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Lot> getLotsByBarcode(List<Long> lotBarcodes) {
		if (lotBarcodes == null || lotBarcodes.size() == 0)
			return null;
		return this.entityManager.createQuery("from Lot l where l.barCode.id in (:ids)").setParameter("ids", lotBarcodes).getResultList();
	}

	/*
	 * (non-Javadoc)
	 * @see org.iita.inventory.service.LotService#duplicate(org.iita.inventory.model .Lot)
	 */
	@Override
	@Transactional
	public Lot duplicate(Lot lot) throws InstantiationException, IllegalAccessException, InventoryException {
		Lot dupe = lot.getClass().newInstance();
		dupe.copyFrom(lot);
		this.entityManager.persist(dupe);
		return dupe;
	}

	/**
	 * Migrate lot to selected destination location
	 * 
	 * @param lot Lot to migrate
	 * @param destination New location of the lot
	 * @throws LocationException Thrown if location is <c>null</c> or lot already at that location
	 */
	@Override
	@Transactional
	public Migration migrate(Lot lot, Location destination, String reason) throws LocationException {
		if (lot == null)
			throw new NullPointerException("Lot not provided.");
		if (destination == null)
			throw new NullPointerException("Destination location not provided.");
		if (lot.getLocation() != null && destination.getId() == lot.getLocation().getId())
			throw new LocationException("Lot " + lot.getItem().getName() + " already at selected location.");

		// Create migration log record
		Migration lotMigration = new Migration();
		lotMigration.setLot(lot);
		lotMigration.setMigrationDate(new Date());
		if (lot.getLocation() != null) {
			lotMigration.setOldLocationId(lot.getLocation().getId());
			lotMigration.setOldLocationName(lot.getLocation().getPathNames());
		} else {
			lotMigration.setOldLocationName("Nowhere");
		}
		lotMigration.setNewLocationId(destination.getId());
		lotMigration.setNewLocationName(destination.getPathNames());
		lotMigration.setReason(reason);
		this.entityManager.persist(lotMigration);

		// Move lot
		lot.setLocation(destination);
		this.entityManager.merge(lot);

		return lotMigration;
	}

	/**
	 * @return
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws Exception
	 * @see org.iita.inventory.service.LotService#updateFields(java.util.List, java.lang.String[])
	 */
	@Override
	@Transactional
	public List<Lot> updateFields(List<? extends Lot> lots, String[] fieldsToUpdate) throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		if (fieldsToUpdate == null || fieldsToUpdate.length == 0)
			return null;

		List<Lot> updatedLots = new ArrayList<Lot>();

		ArrayList<Method> SLgetters = new ArrayList<Method>(), SLsetters = new ArrayList<Method>();
		{
			// seed lot
			Class<?> clazz = SeedLot.class;
			for (String fieldToUpdate : fieldsToUpdate) {
				try {
					String baseName = fieldToUpdate.substring(0, 1).toUpperCase() + fieldToUpdate.substring(1);
					log.info("Looking up getter and setter method for SeedLot." + baseName);
					Method getter = clazz.getMethod("get" + baseName);
					Method setter = clazz.getMethod("set" + baseName, getter.getReturnType());
					SLgetters.add(getter);
					SLsetters.add(setter);
				} catch (SecurityException e) {
					log.error(e);
				} catch (NoSuchMethodException e) {
					log.error(e);
				} catch (IllegalArgumentException e) {
					log.error(e);
				}
			}
		}

		Method[] SLgettersA = (Method[]) SLgetters.toArray(METHOD_ARRAY);
		Method[] SLsettersA = (Method[]) SLsetters.toArray(METHOD_ARRAY);
		log.trace("Got all getters and setters");

		ArrayList<Method> ILgetters = new ArrayList<Method>(), ILsetters = new ArrayList<Method>();
		{
			// seed lot
			Class<?> clazz = InVitroLot.class;
			for (String fieldToUpdate : fieldsToUpdate) {
				try {
					String baseName = fieldToUpdate.substring(0, 1).toUpperCase() + fieldToUpdate.substring(1);
					log.info("2: Looking up getter and setter method for InVitroLot." + baseName);
					Method getter = clazz.getMethod("get" + baseName);
					Method setter = clazz.getMethod("set" + baseName, getter.getReturnType());
					ILgetters.add(getter);
					ILsetters.add(setter);
				} catch (SecurityException e) {
					log.error(e);
				} catch (NoSuchMethodException e) {
					log.error(e);
				} catch (IllegalArgumentException e) {
					log.error(e);
				}
			}
		}
		Method[] ILgettersA = (Method[]) ILgetters.toArray(METHOD_ARRAY);
		Method[] ILsettersA = (Method[]) ILsetters.toArray(METHOD_ARRAY);

		try {
			for (Lot lot : lots) {
				// get DB lot
				log.debug("Loading existing lot " + lot.getId());
				Lot dbLot = this.entityManager.find(lot.getClass(), lot.getId());
				if (dbLot == null) {
					log.warn("Lot not found in DB, not updating");
				}
				if (dbLot instanceof SeedLot) {
					SeedLot myLot = (SeedLot) dbLot, sourceLot = (SeedLot) lot;
					updateFields(myLot, sourceLot, SLgettersA, SLsettersA);
					this.entityManager.merge(myLot);
				} else if (dbLot instanceof InVitroLot) {
					InVitroLot myLot = (InVitroLot) dbLot, sourceLot = (InVitroLot) lot;
					updateFields(myLot, sourceLot, ILgettersA, ILsettersA);
					this.entityManager.merge(myLot);
				}
				updatedLots.add(dbLot);
			}
		} catch (InvocationTargetException e) {
			log.error(e);
			throw e;
		}

		// Return list of updated lots.
		return updatedLots;
	}

	/**
	 * @param myLot
	 * @param sourceLot
	 * @param lgettersA
	 * @param lsettersA
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	private void updateFields(Lot myLot, final Lot sourceLot, final Method[] lgettersA, final Method[] lsettersA) throws IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		for (int i = 0; i < lgettersA.length; i++) {
			Object value = lgettersA[i].invoke(sourceLot);
			lsettersA[i].invoke(myLot, value);
			//log.info("3: VALUE." + value);
		}
	}

	/**
	 * @throws InventoryException
	 * @see org.iita.inventory.service.LotService#registerSafeDuplicationLots(org.iita.inventory.model.QuantityUpdateBulk)
	 */
	@Override
	@Transactional
	public QuantityUpdateBulk registerDuplicatedLots(QuantityUpdateBulk bulk) throws InventoryException {
		if (bulk == null)
			throw new InventoryException("Null bulk source passed to register safe duplication lots.");

		if (bulk.getStatus() != 1)
			throw new InventoryException("This bulk was already completely processed.");

		if (bulk.getTransactionType() != Transaction2.Type.OUT
				|| !(bulk.getTransactionSubtype().equalsIgnoreCase("SAFEDUP") || bulk.getTransactionSubtype().equalsIgnoreCase("REGENERATION")))
			throw new InventoryException("Bulk " + bulk.getTitle() + " is not an OUT and SAFEDUP or REGENERATION type!");

		QuantityUpdateBulk newbulk = new QuantityUpdateBulk();
		newbulk.setAffectingInventory(true);
		newbulk.setDate(new Date());
		newbulk.setStatus(1);
		newbulk.setTransactionType(Transaction2.Type.IN);
		newbulk.setTransactionSubtype("SAFEDUP");
		newbulk.setTitle("Safeduplicated " + bulk.getTitle());
		newbulk.setDescription("Lots generated by safeduplication '" + bulk.getTitle() + "' with id=" + bulk.getId());
		newbulk.setLots(new ArrayList<QuantityUpdate>());

		log.info("Generating new lots from bulk: " + bulk.getTitle());

		List<Lot> dupedLots = duplicateLots(bulk);
		this.store(dupedLots);
		for (Lot lot : dupedLots) {
			QuantityUpdate lotUpdate = new QuantityUpdate();
			lotUpdate.setLot(lot);
			lotUpdate.setQuantity(lot.getQuantity());
			lotUpdate.setScale(lot.getScale());
			lotUpdate.setDescription(newbulk);
			newbulk.getLots().add(lotUpdate);
		}

		// finalize bulk status
		bulk.setStatus(100);
		this.entityManager.merge(bulk);

		this.entityManager.persist(newbulk);

		log.info("Finished creating duplicated lots.");
		return newbulk;
	}

	/**
	 * Utility method to generate new Lots for safe duplication
	 * 
	 * @param bulk
	 * @return
	 * @throws InventoryException
	 */
	private List<Lot> duplicateLots(QuantityUpdateBulk bulk) throws InventoryException {
		List<Lot> dupedLots = new ArrayList<Lot>();
		if (bulk.getTransactionType() != Type.OUT)
			throw new InventoryException("Will not generate duplicated when transaction type is not OUT");

		for (QuantityUpdate lotUpdate : bulk.getLots()) {
			Lot lot = lotUpdate.getLot();
			Lot duped;
			try {
				duped = this.duplicate(lot);
				// set quantity
				duped.setInitialQuantity(Math.abs(lotUpdate.getQuantity()));
				duped.setInitialScale(lotUpdate.getScale());

				if (bulk.getTransactionSubtype().equalsIgnoreCase("SAFEDUP")) {
					duped.setQuantity(Math.abs(lotUpdate.getQuantity()));
					duped.setScale(lotUpdate.getScale());
				} else if (bulk.getTransactionSubtype().equalsIgnoreCase("REGENERATION")) {
					duped.setScale("plant");
				}
				duped.setStatus(1);
				dupedLots.add(duped);
				log.debug("Duplicated lot " + duped.getItem().getName() + " " + duped.getQuantity() + " " + duped.getScale());
			} catch (InstantiationException e) {
				log.error(e);
			} catch (IllegalAccessException e) {
				log.error(e);
			}
		}

		return dupedLots;
	}

	/**
	 * @see org.iita.inventory.service.LotService#listTransactions(int, int)
	 */
	@Override
	@Transactional(readOnly = true)
	public PagedResult<Transaction2> listTransactions(int startAt, int maxRecords) {
		PagedResult<Transaction2> paged = new PagedResult<Transaction2>(startAt, maxRecords);
		paged.setResults(this.entityManager.createQuery("from Transaction2 t order by t.date desc").setFirstResult(startAt).setMaxResults(maxRecords)
				.getResultList());
		paged.setTotalHits(((Long) this.entityManager.createQuery("select count(t) from Transaction2 t").getSingleResult()).intValue());
		return paged;
	}

	/**
	 * @see org.iita.inventory.service.LotService#getLotsForItems(java.util.List)
	 */
	@Override
	@Transactional(readOnly = true)
	public Dictionary<Item, List<Lot>> getLotsForItems(List<Item> items) {
		if (items == null || items.size() == 0)
			return null;
		Dictionary<Item, List<Lot>> results = new Hashtable<Item, List<Lot>>();

		for (Item item : items) {
			if (item == null)
				continue;
			List<Lot> lots = results.get(item);
			if (lots == null) {
				results.put(item, lots = new ArrayList<Lot>());
				lots.addAll(this.getAvailableLots(item));
			}
		}

		return results;
	}

	/**
	 * @param item
	 * @return
	 */
	@Transactional(readOnly = true)
	@SuppressWarnings("unchecked")
	private Collection<? extends Lot> getAvailableLots(Item item) {
		return this.entityManager.createQuery("from Lot l where l.item=:item and l.status=1 order by l.quantity desc").setParameter("item", item)
				.getResultList();
	}

	/**
	 * @see org.iita.inventory.service.LotService#listTransactions(java.util.Calendar, java.util.Calendar)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Transaction2> listTransactions(Calendar startDate, Calendar endDate) {
		return this.entityManager.createQuery("from Transaction2 t where t.date between :start and :end order by t.date").setParameter("start",
				startDate.getTime()).setParameter("end", endDate.getTime()).getResultList();
	}

	/**
	 * Get total quantity of same item in same location using same scale
	 * 
	 * @see org.iita.inventory.service.LotService#getTotalQuantityInLocation(org.iita.inventory.model.Lot)
	 */
	@Override
	@Transactional(readOnly = true)
	public double getTotalQuantityInLocation(Lot lot) {
		if (lot == null)
			return 0.0d;
		Double res = (Double) this.entityManager
				.createQuery("select sum(l.quantity) from Lot l where l.item=:item and l.location=:location and l.scale=:scale").setParameter("item",
						lot.getItem()).setParameter("location", lot.getLocation()).setParameter("scale", lot.getScale()).getSingleResult();
		return res == null ? 0.0d : res;
	}
	
	/**
	 * Get total quantity of same item in same location using same scale
	 * 
	 * @see org.iita.inventory.service.LotService#getTotalQuantityInLocation(org.iita.inventory.model.Lot)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public PagedResult<Object[]> getListLotTotalQuantityInLocation(int startAt, int maxResults) {
		PagedResult<Object[]> paged = new PagedResult<Object[]>(startAt, maxResults);
		paged.setResults(new ArrayList<Object[]>());
		/*paged.setResults(this.entityManager
				.createQuery("select l.item.name, l.scale, l.item.itemType.name, sum(l.quantity), l.location.name from Lot l where (l.item.itemType.name='Yam' OR l.item.itemType.name='Cassava' OR l.item.itemType.name='Musa') and (l.location.name='SD Yam G.C' OR l.location.name='SD 363.4' OR l.location.name='Ymm 377.3') group by l.item.name, l.item.itemType.name, l.location.name")
				.setFirstResult(startAt).setMaxResults(maxResults).getResultList());*/
		
		List<Object[]> lots = this.entityManager
		.createQuery("select l.item.name, l.scale, l.item.itemType.name, sum(l.quantity), l.location.name from Lot l " +
				"where (l.item.itemType.name='Yam' OR l.item.itemType.name='Cassava' OR l.item.itemType.name='Musa') and " +
				"(l.location.name='SD Yam G.C' OR l.location.name='SD 363.4' OR l.location.name='Ymm 377.3') " +
				"group by l.item.name, l.item.itemType.name, l.location.name " +
				"order by l.location.name asc")
		.getResultList();
		/*
		if (paged.getResults().size() > 0) {
			paged.setTotalHits((Long) Long.valueOf(paged.getResults().size()));//this.entityManager.createQuery("select count(*) from (select count(l.item.name) from Lot l where (l.item.itemType.name='Yam' OR l.item.itemType.name='Cassava' OR l.item.itemType.name='Musa') and (l.location.name='SD Yam G.C' OR l.location.name='SD 363.4' OR l.location.name='Ymm 377.3') group by l.item.name, l.item.itemType.name, l.location.name)").getSingleResult());
		}*/
		log.info("Lots SIZE: " + lots.size());
		int lotsCount = -1;
		for (Object[] lot : lots) {
			lotsCount++;
			if (startAt > lotsCount)
				continue;
			if (lotsCount - startAt >= maxResults)
				continue;
			paged.getResults().add((Object[]) lot);
		}
		paged.setTotalHits(lotsCount + 1);
		
		return paged;
	}
	
	/**
	 * Get update field variables for lot
	 * 
	 * @see org.iita.inventory.service.LotService#updateFieldVariables(org.iita.inventory.model.Lot)
	 */
	@Override
	@Transactional
	public void updateFieldVariables(Lot lot){
		if(lot != null){
			cleanup(lot);
			this.entityManager.merge(lot);
		}
	}
	/**
	 * Get update field variables for lot
	 * 
	 * @see org.iita.inventory.service.LotService#deleteFieldVariables(org.iita.inventory.model.FieldVariables)
	 */
	@Override
	@Transactional
	public void deleteFieldVariables(Lot lot){
		if(lot.getFieldVariables()!=null){
			for (int i = lot.getFieldVariables().size() - 1; i >= 0; i--) {
				FieldVariables fieldVariable = lot.getFieldVariables().get(i);
				if (fieldVariable != null) {
					log.info("Removing " + fieldVariable.getVar() + " " + fieldVariable);
					ensureRemoved(lot.getFieldVariables().remove(i));
				}
			}
		}
	}
	/**
	 * 
	 */
	@Transactional
	public void cleanup(Lot lot) {
		//cleanup
		for (int i = lot.getFieldVariables().size() - 1; i >= 0; i--) {
			FieldVariables fieldVariable = lot.getFieldVariables().get(i);
			if(fieldVariable!=null){
				if (fieldVariable.getQty()==null) {
					log.info("Removing var null " + fieldVariable);
					ensureRemoved(lot.getFieldVariables().remove(i));
				} else if(fieldVariable.getVar()!=null){
					if(fieldVariable.getQty().length()==0){
						log.info("Removing length zero " + fieldVariable);
						ensureRemoved(lot.getFieldVariables().remove(i));
					}else{
						log.info("Setting Lot " + fieldVariable.getVar());
						fieldVariable.setLot(lot);
					}
				}
			}
		}
	}
	

	/**
	 * Utility method checks if object is in session and deletes from persistent storage
	 * 
	 * @param objectToRemove
	 */
	@Transactional(readOnly = false)
	private void ensureRemoved(Object objectToRemove) {
		if (objectToRemove != null) {
			log.debug("EM removing " + objectToRemove);
			this.entityManager.remove(objectToRemove);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<FieldVariables> getLotFieldVariablesByLotId(Long lotId) {
		if (lotId == null)
			return null;
		log.info("getting field variables for supplied lot id");
		return this.entityManager.createQuery("from FieldVariables lv where lv.lot_id=:id").setParameter("id", lotId).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<FieldVariables> getLotFieldVariablesByLot(Lot lot) {
		List<FieldVariables> result = null;
		
		if (lot == null)
			return null;
		
		log.info("getting field variables for supplied lot id");
		try{
			result = this.entityManager.createQuery("from FieldVariables lv where lv.lot=:lot").setParameter("lot", lot).getResultList();
		}catch(Exception e) {
			return null;
		}
		
		if(result.size() <= 0)
			return null;
		else
			return result;
	}

	@Override
	@Transactional(readOnly = true)
	public FieldVariables loadLotFieldVariable(Lot lot, String var) {
		Object result = null;
		if (lot == null)
			return null;
		if (var.toString().isEmpty())
			return null;
		
		log.info("getting field variables for supplied lot id and variable string: LOTID: " + lot.getId() + "; VAR: " + var.toString());
		try{
			result = this.entityManager.createQuery("select lv from FieldVariables lv where lv.lot=:lot and lv.var=:var").setParameter("lot", lot).setParameter("var", var).getSingleResult();
		}catch(Exception e) {
			return null;
		}
		
		if(result==null)
			return null;
		else
			return (FieldVariables) result;
	}
	
	/**
	 * Get merge existing field variable
	 * 
	 * @see org.iita.inventory.service.LotService#mergeLotFieldVariable(org.iita.inventory.model.FieldVariables)
	 */
	@Override
	@Transactional
	public void mergeLotFieldVariable(FieldVariables fv){
		if(fv != null){
			this.entityManager.merge(fv);
		}
	}
	
	@Override
	@Secured({ "ROLE_ADMIN" })
	@Transactional
	public List<Lot> previewXLSLots(File file, List<Object[]> rowFailedLotData, User user) throws FileNotFoundException,
			IOException, XLSImportException {
		HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(file));

		List<Object[]> rowData = this.importService.getObjectsFromXLS(workbook.getSheetAt(0), 0);
		List<Lot> lots = new ArrayList<Lot>();
		for (Object[] row : rowData) {
			Lot lot;
			try {
				lot = getLot(row, rowFailedLotData, user);
			} catch (Exception e) {
				log.error(e, e);
				throw new RuntimeException("Could not locate lot: " + e.getMessage() + " / LotName: " + row[0] + " / LotPrefix: " + row[1] + " / LotID: " + row[2], e);
			}
			if (lot != null) {
				lots.add(lot);
				log.debug("LOT FOUND");
			} else {
				log.debug("LOT NOT FOUND");
			}
		}

		return lots;
	}
	
	/**
	 * @param row
	 * @return 
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	private Lot getLot(Object[] row, List<Object[]> rowFailedLotData, User user) throws InstantiationException,
			IllegalAccessException {

		int i = 0;
		for (Object x : row) {
			log.debug("" + i++ + ": " + x);
		}

		if (row[0] != null) {
			try{
				Lot lot = this.findLot((String) row[0], (String) row[1], (String) row[2]);
				
				if (lot == null) {
					//log.info("LOT NULL BY LOTID: " + lot);
					try{
						ensureLotExists((String) row[0], (String) row[1], (String) row[2], user, rowFailedLotData);
					}catch(Exception ex){
						rowFailedLotData.add(row);
					}
					return null;
				}else{
					//log.info("LOT NOT NULL BY LOTID: " + lot);
					
					if(lot.getLocation()==null){
						//log.info("LOT LOCATION NULL: " + lot);
						try{
							ensureLotExists((String) row[0], (String) row[1], (String) row[2], user, rowFailedLotData);
						}catch(Exception ex){
							rowFailedLotData.add(row);
						}
					}
					
					if(lot.getBarCode()==null){
						//log.info("LOT LOCATION NULL: " + lot);
						try{
							ensureLotExists((String) row[0], (String) row[1], (String) row[2], user, rowFailedLotData);
						}catch(Exception ex){
							rowFailedLotData.add(row);
						}
					}
				}
	
				return lot;
			}catch(Exception ex){
				rowFailedLotData.add(row);
				return null;
			}
		} else {
			return null;
		}
	}
	
	@Transactional
	@Secured({ "ROLE_ADMIN"})
	public void importXLSLots(List<Lot> lots, User user, List<Object[]> rowFailedLotData) {
		for (Lot lot : lots)
			ensureLotExists(lot.getItem().getName(), lot.getItem().getPrefix(), lot.getLocation().getName(), user, rowFailedLotData);
	}
	
	/**
	 * @return
	 * @see org.iita.par.service.AppraisalService#ensureLotExists(org.iita.security.model.User, org.iita.security.model.User, int)
	 */
	@Transactional
	@Secured({ "ROLE_ADMIN" })
	private Lot ensureLotExists(String name, String prefix, String location, User user, List<Object[]> rowFailedLotData) {
		log.info("Ensuring lot exists for " + name + " by prefix " + prefix + " in location " + location);
		Lot lot = this.findLot(name, prefix, location);
		Lot lotUpdate = this.findLot(name, prefix);
		Location loc = this.findLocation(location);
		
		log.info("lot: " + lot);
		log.info("lotUpdate: " + lotUpdate);
		log.info("location: " + location);
		
		if(lotUpdate!=null){
			ItemType itemType = this.findItemType("Cassava");
			
			ContainerType plant = this.findContainerType("Stem");
			
			Item item = this.findItem(name, prefix, itemType);
			
			if(item==null){
				item = new Item();
				
				item.setName(name);
				item.setPrefix(prefix);
				item.setDateLastModified(new Date());
				item.setItemType(itemType);
				try{
					this.entityManager.persist(item);
				}catch(Exception ex){
					Object[] row = (Object[]) new Object();
					row[0] = name;
					row[1] = prefix;
					row[2] = location;
					
					rowFailedLotData.add(row);
					return null;
				}
			}
			
			lotUpdate.setItem(item);
			lotUpdate.setInitialScale("stem");
			lotUpdate.setScale("stem");
			lotUpdate.setQuantity(0D);
			lotUpdate.setContainer(plant);
			lotUpdate.setInitialQuantity(0D);
			lotUpdate.setCreatedDate(new Date());	
			lotUpdate.setLastUpdated(new Date());					
			lotUpdate.setCreatedBy(user.getUserName());
			
			lotUpdate.setLocation(loc);
			this.entityManager.merge(lotUpdate);
			

			if(lotUpdate.getBarCode()==null){
				try {
					this.barcodingService.assignBarCode(lotUpdate);
				} catch (BarcodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}else if (lot == null) {
			lot = new Lot();
			//log.warn("Creating new item in lot in location " + location);
			
			//if(lot != null){
				//try {
					ItemType itemType = this.findItemType("Cassava");
					
					ContainerType plant = this.findContainerType("Stem");
					
					log.info("item name: " + name);
					log.info("item_prefix: " + prefix);
					log.info("itemtype_name: " + itemType.getName());
					Item item = this.findItem(name, prefix, itemType);
										
					if(item==null){
						item = new Item();
						
						item.setName(name);
						item.setPrefix(prefix);
						item.setDateLastModified(new Date());
						item.setItemType(itemType);
						
						try{
							this.entityManager.persist(item);
						}catch(Exception ex){
							Object[] row = (Object[]) new Object();
							row[0] = name;
							row[1] = prefix;
							row[2] = location;
							
							rowFailedLotData.add(row);
							return null;
						}
					}
					
					lot.setItem(item);
					lot.setLocation(loc);
					lot.setInitialScale("stem");
					lot.setScale("stem");
					lot.setQuantity(0D);
					lot.setContainer(plant);
					lot.setInitialQuantity(0D);
					lot.setCreatedDate(new Date());	
					lot.setLastUpdated(new Date());					
					lot.setCreatedBy(user.getUserName());
					
					try{
						this.entityManager.persist(lot);
					}
					catch(Exception e){
						
					}
					
					try {
						if(lot.getBarCode()==null){
							this.barcodingService.assignBarCode(lot);
						}
					} catch (BarcodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				//} catch (Exception e) {
				//	log.error("Error creating instance of " + lot.getClass().getName());
				//	throw new RuntimeException("Error creating instance of " + lot, e);
				//}
			//}
		}else if(lot!=null){
			if(lot.getLocation()==null && loc!=null){
				lot.setLocation(loc);
				
				this.entityManager.merge(lot);			
			}
			
			if(lot.getScale()=="plant"){
				lot.setInitialScale("stem");
				lot.setScale("stem");
				this.entityManager.merge(lot);
			}
			
			if(lot.getBarCode()==null){
				try {
					this.barcodingService.assignBarCode(lot);
				} catch (BarcodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return lot;
	}
	
	/**
	 * @see org.iita.par.service.LotService#findLot(java.lang.String, java.lang.String, java.lang.Long, int)
	 */
	@Transactional(readOnly = true)
	private Item findItem(String name, String prefix, ItemType itemType) {
		Item item = new Item();
		//try {			
			item = (Item) this.entityManager
					.createQuery("from Item i where (i.name=:name and i.prefix=:prefix and i.itemType.id=:itemType) or (i.name=:name and i.itemType.id=:itemType)")
					.setParameter("name", name).setParameter("prefix", prefix).setParameter("itemType", itemType.getId()).setMaxResults(1).getSingleResult();
			
			log.info("ITEM: " + item);
			
			if(item!=null) 
				log.info("ITEM: " + item.getName() + " " + item.getPrefix() + " " + item.getItemType().getName());
			
			return item;
		//} catch (Exception e) {
		//	log.info("ITEM: " + item);
		//	return null;
		//}
	}
	
	/**
	 * @see org.iita.par.service.LotService#findLot(java.lang.String, java.lang.String, java.lang.Long, int)
	 */
	@Transactional(readOnly = true)
	private Lot findLot(String name, String prefix, String location) {
		Lot lot = new Lot();
		try {
			lot = (Lot) this.entityManager
					.createQuery("from Lot l where l.item.name=:name and l.item.prefix=:prefix and l.location.name=:location")
					.setParameter("name", name).setParameter("prefix", prefix).setParameter("location", location).setMaxResults(1).getSingleResult();
			
			if(lot!=null)
				return lot;
			else
				return null;
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * @see org.iita.par.service.LotService#findLot(java.lang.String, java.lang.String, java.lang.Long, int)
	 */
	@Transactional(readOnly = true)
	private Lot findLot(String name, String prefix) {
		Lot lot = new Lot();
		try {
			lot = (Lot) this.entityManager
					.createQuery("from Lot l where l.item.name=:name and l.item.prefix=:prefix and l.location IS NULL and l.quantity<1")
					.setParameter("name", name).setParameter("prefix", prefix).setMaxResults(1).getSingleResult();
			if(lot!=null)
				return lot;
			else
				return null;
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * @see org.iita.par.service.LotService#findLot(java.lang.String, java.lang.String, java.lang.Long, int)
	 */
	@Transactional(readOnly = true)
	private Location findLocation(String name) {
		try {
			return (Location) this.entityManager
					.createQuery("from Location l where l.name=:name")
					.setParameter("name", name).setMaxResults(1).getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * @see org.iita.par.service.LotService#findContainerType(java.lang.String, java.lang.String, java.lang.Long, int)
	 */
	@Transactional(readOnly = true)
	private ContainerType findContainerType(String name) {
		try {
			return (ContainerType) this.entityManager
					.createQuery("from ContainerType c where c.name=:name")
					.setParameter("name", name).setMaxResults(1).getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * @see org.iita.par.service.LotService#findLot(java.lang.String, java.lang.String, java.lang.Long, int)
	 */
	@Transactional(readOnly = true)
	private ItemType findItemType(String name) {
		try {
			return (ItemType) this.entityManager
					.createQuery("from ItemType c where c.name=:name")
					.setParameter("name", name).setMaxResults(1).getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}
}
