/**
 * 
 */
package org.iita.inventory.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.iita.inventory.model.GenebankOrder;
import org.iita.inventory.model.GenebankOrderItem;
import org.iita.inventory.model.Lot;
import org.iita.inventory.model.QuantityUpdate;
import org.iita.inventory.model.QuantityUpdateBulk;
import org.iita.inventory.model.Transaction2;
import org.iita.inventory.model.Order.OrderState;
import org.iita.inventory.model.OrderItem.OrderItemStatus;
import org.iita.inventory.model.Transaction2.Source;
import org.iita.inventory.model.Transaction2.Type;
import org.iita.inventory.service.QuantityUpdateService;
import org.iita.util.PagedResult;
import org.springframework.security.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author mobreza
 */
public class QuantityUpdateServiceImpl implements QuantityUpdateService {
	private static final Log log = LogFactory.getLog(QuantityUpdateService.class);
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
	 * @see org.iita.inventory.service.QuantityUpdateService#delete(org.iita.inventory .model.QuantityUpdateDescription)
	 */
	@Override
	@Transactional
	public void delete(QuantityUpdateBulk bulkUpdate) {
		log.warn("Delete of " + bulkUpdate.getId() + " " + bulkUpdate.getTitle() + " requested.");
		if (bulkUpdate.getStatus() != 0) {
			log.info("Can only delete non-commited bulk updates.");
			throw new IllegalStateException("Can only delete non-commited bulk updates.");
		}

		// set status to COMMITED
		this.entityManager.remove(bulkUpdate);
		log.warn("Delete of " + bulkUpdate.getId() + " " + bulkUpdate.getTitle() + " finished.");
	}

	/*
	 * (non-Javadoc)
	 * @see org.iita.inventory.service.QuantityUpdateService#insert(org.iita.inventory .model.QuantityUpdateDescription)
	 */
	@Override
	@Transactional
	public void store(QuantityUpdateBulk bulkUpdate) {
		if (bulkUpdate == null)
			throw new NullPointerException();

		if (bulkUpdate.getId() != null) {
			log.warn("Updating existing bulk update id=" + bulkUpdate.getId());
			this.entityManager.merge(bulkUpdate);
		} else {
			log.info("Inserting Bulk update \"" + bulkUpdate.getTitle() + "\" " + bulkUpdate.getTransactionSubtype() + " " + bulkUpdate.getTransactionType());
			this.entityManager.persist(bulkUpdate);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.iita.inventory.service.QuantityUpdateService#commit(org.iita.inventory .model.QuantityUpdateDescription)
	 */
	@Override
	@Transactional
	public void commit(QuantityUpdateBulk bulkUpdate) {
		log.warn("Commit of " + bulkUpdate.getId() + " " + bulkUpdate.getTitle() + " requested.");
		if (bulkUpdate.getStatus() != 0) {
			log.info("Cannot commit an already commited bulk update.");
			throw new IllegalStateException("Cannot commit an already commited bulk update.");
		}

		// update all lot quantities
		for (QuantityUpdate singleItem : bulkUpdate.getLots()) {
			// check quantity
			Lot lot = singleItem.getLot();
			singleItem.setOriginalQty(lot.getQuantity());

			// create transaction record
			Transaction2 transaction = new Transaction2();
			transaction.setDate(new Date());
			transaction.setSubtype(bulkUpdate.getTransactionSubtype());
			transaction.setSource(Source.BULK);
			transaction.setRel(bulkUpdate.getId());
			transaction.setLot(lot);
			// depending on bulk update type, the quantity is either - or +
			if (bulkUpdate.getTransactionType() == Type.OUT) {
				transaction.setQuantity(-singleItem.getQuantity());

				//Check the value of lot quantity and set to incoming quantity if less than
				if (lot.getQuantity() < singleItem.getQuantity()) {
					lot.setQuantity(singleItem.getQuantity());
					this.entityManager.merge(lot);
				}
				log.info("LOT TRANSACTION TYPE PASSED OUT");
			} else if (bulkUpdate.getTransactionType() == Type.IN) {
				transaction.setQuantity(singleItem.getQuantity());
				log.info("LOT TRANSACTION TYPE PASSED IN");
			} else if (bulkUpdate.getTransactionType() == Type.RSET) {
				transaction.setSubtype("RESET");
				transaction.setQuantity(singleItem.getQuantity());
				log.info("LOT TRANSACTION TYPE PASSED RESET");
			}

			log.info("LOT TRANSACTION TYPE: " + bulkUpdate.getTransactionType());

			transaction.setScale(singleItem.getScale());
			this.entityManager.persist(transaction);

			if (bulkUpdate.isAffectingInventory()) {
				// check scale
				if (!lot.getScale().equalsIgnoreCase(transaction.getScale())) {
					throw new IllegalStateException("Lot scale is different from requested scale: " + transaction.getScale());
				}
				if (bulkUpdate.getTransactionType() == Type.RSET) {
					if (transaction.getQuantity() < 0)
						// quantity cannot be negative
						throw new IllegalStateException("Cannot set quantity to negative value.");
					// update lot
					lot.setQuantity(transaction.getQuantity());
				} else {
					if (lot.getQuantity() + transaction.getQuantity() < 0.0d)
						// check that resulting quantity is not negative
						throw new IllegalStateException("Available QTY: " + lot.getQuantity() + " Transaction QTY: " + transaction.getQuantity() + "  in lot "
								+ lot.getId() + " item " + lot.getItem().getName() + ". Would result in negative quantity!");
					// update lot
					lot.setQuantity(lot.getQuantity() + transaction.getQuantity());
				}
			}
			this.entityManager.merge(lot);
		}

		// set status to COMMITED
		bulkUpdate.setStatus(1);
		this.entityManager.merge(bulkUpdate);
		log.warn("Commit of " + bulkUpdate.getId() + " " + bulkUpdate.getTitle() + " finished.");

		// check if order exists
		if (bulkUpdate.getOrder() != null)
			commitOrder(bulkUpdate);
	}

	/**
	 * Update status of requested items
	 * 
	 * @param bulkUpdate
	 */
	private void commitOrder(QuantityUpdateBulk bulkUpdate) {
		GenebankOrder order = bulkUpdate.getOrder();

		for (QuantityUpdate update : bulkUpdate.getLots()) {
			GenebankOrderItem orderItem = order.findItem(update.getLot());
			if (orderItem != null) {
				orderItem.setStatus(OrderItemStatus.DISPATCHED);
				this.entityManager.merge(orderItem);
			}
		}

		updateOrderStatus(order);
	}

	private void updateOrderStatus(GenebankOrder order) {
		boolean allDispatched = true;
		for (GenebankOrderItem orderItem : order.getItems()) {
			allDispatched &= (orderItem.getStatus() == OrderItemStatus.DISPATCHED);
		}

		if (allDispatched) {
			log.info("All items dispatched. Setting order status to closed");
			order.setState(OrderState.CLOSED);
			this.entityManager.merge(order);
		} else {
			log.info("All items dispatched. Setting order status to closed");
			order.setState(OrderState.PENDING);
			this.entityManager.merge(order);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.iita.inventory.service.QuantityUpdateService#rollback(org.iita.inventory .model.QuantityUpdateDescription)
	 */
	@Override
	@Secured({ "ROLE_ROLLBACK" })
	@Transactional
	public void rollback(QuantityUpdateBulk bulkUpdate) {
		log.warn("Rollback of " + bulkUpdate.getId() + " " + bulkUpdate.getTitle() + " requested.");
		if (bulkUpdate.getStatus() != 1) {
			log.error("Cannot rollback a non-commited bulk update.");
			throw new IllegalStateException("Cannot rollback a non-commited bulk update.");
		}

		// update all lot quantities
		for (QuantityUpdate singleItem : bulkUpdate.getLots()) {
			// check quantity
			Lot lot = singleItem.getLot();

			Transaction2 transaction = new Transaction2();
			transaction.setDate(new Date());
			transaction.setSource(Source.BULK);
			transaction.setRel(bulkUpdate.getId());
			transaction.setLot(lot);
			transaction.setScale(singleItem.getScale());

			if (bulkUpdate.getTransactionType() == Type.RSET) {
				transaction.setSubtype("RESET");
				transaction.setQuantity(singleItem.getOriginalQty());
			} else {
				transaction.setSubtype("ROLLBACK");
				if (lot.getQuantity() + singleItem.getQuantity() < 0.0d)
					throw new IllegalStateException("Cannot revert, quantity would be <0.");

				// depending on bulk update type, the quantity is either - or +
				if (bulkUpdate.getTransactionType() == Type.OUT)
					transaction.setQuantity(singleItem.getQuantity());
				else
					transaction.setQuantity(-singleItem.getQuantity());
			}

			this.entityManager.persist(transaction);

			if (bulkUpdate.isAffectingInventory()) {
				if (!lot.getScale().equalsIgnoreCase(transaction.getScale()))
					throw new IllegalStateException("Lot scale does not match requested scale: " + transaction.getScale());

				if (bulkUpdate.getTransactionType() == Type.RSET) {
					lot.setQuantity(transaction.getQuantity());
				} else {
					lot.setQuantity(lot.getQuantity() + transaction.getQuantity());
				}
				this.entityManager.merge(lot);
			}
		}

		// revert status
		bulkUpdate.setStatus(0);
		this.entityManager.merge(bulkUpdate);
		log.warn("Rollback of " + bulkUpdate.getId() + " " + bulkUpdate.getTitle() + " finished.");

		// check if order exists
		if (bulkUpdate.getOrder() != null)
			rollbackOrder(bulkUpdate);
	}

	/**
	 * @param bulkUpdate
	 */
	private void rollbackOrder(QuantityUpdateBulk bulkUpdate) {
		GenebankOrder order = bulkUpdate.getOrder();
		for (QuantityUpdate update : bulkUpdate.getLots()) {
			GenebankOrderItem orderItem = order.findItem(update.getLot());
			if (orderItem == null)
				continue;

			boolean found = false;
			// check if any other committed bulk update contains this lot
			for (QuantityUpdateBulk bulk : order.getUpdates()) {
				if (bulk == bulkUpdate)
					continue; // skip self
				if (bulk.getStatus() == 0)
					continue; // skip non-committed

				found |= bulk.getLotList().contains(orderItem.getLot());
			}

			if (!found) {
				orderItem.setStatus(OrderItemStatus.PENDING);
				this.entityManager.merge(orderItem);
			}
		}

		// could be pending or closed based on distributions
		updateOrderStatus(order);
	}

	/*
	 * (non-Javadoc)
	 * @see org.iita.inventory.service.QuantityUpdateService#store(org.iita.inventory .model.QuantityUpdate)
	 */
	@Override
	@Transactional
	public void store(QuantityUpdate singleUpdate) {
		if (singleUpdate.getDescription() == null) {
			log.info("Bulk not set for single update.");
			throw new IllegalStateException("Bulk not set for single update.");
		}
		if (singleUpdate.getDescription().getStatus() != 0) {
			log.info("Cannot update individual quantity updates after the transaction for Bulk=" + singleUpdate.getDescription().getId()
					+ " has been commited. Use rollback first.");
			throw new IllegalStateException("Cannot update individual quantity updates after the transaction has been commited. Use rollback first.");
		}

		if (singleUpdate.getId() != null) {
			log.info("Updating single lot quantity in Bulk=" + singleUpdate.getDescription().getId() + " ID=" + singleUpdate.getId() + " Lot="
					+ singleUpdate.getLot().getId());
			this.entityManager.merge(singleUpdate);
		} else {
			log.info("Inserting single lot quantity to Bulk=" + singleUpdate.getDescription().getId() + " Lot=" + singleUpdate.getLot().getId());
			this.entityManager.persist(singleUpdate);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.iita.inventory.service.QuantityUpdateService#delete(org.iita.inventory .model.QuantityUpdate)
	 */
	@Override
	@Transactional
	public void delete(QuantityUpdate singleUpdate) {
		if (singleUpdate.getDescription().getStatus() != 0) {
			log.info("Cannot delete individual quantity updates after the transaction has been commited. Use rollback first.");
			throw new IllegalStateException("Cannot delete individual quantity updates after the transaction has been commited. Use rollback first.");
		}

		log.info("Deleting single lot quantity ID=" + singleUpdate.getId() + " Lot=" + singleUpdate.getLot().getId());
		singleUpdate.getDescription().getLots().remove(singleUpdate);
		this.entityManager.remove(singleUpdate);
	}

	/*
	 * (non-Javadoc)
	 * @see org.iita.inventory.service.QuantityUpdateService#load(long)
	 */
	@Override
	@Transactional(readOnly = true)
	public QuantityUpdateBulk load(long id) {
		log.debug("Reading QuantityUpdateBulk ID=" + id);
		return this.entityManager.find(QuantityUpdateBulk.class, id);
	}

	/*
	 * (non-Javadoc)
	 * @see org.iita.inventory.service.QuantityUpdateService#listByType(org.iita.inventory.model.Transaction.Type[], int, int)
	 */
	//@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public PagedResult<QuantityUpdateBulk> listByType(int startAt, int pageSize, Long location, String title, String... transactionNames) {
		PagedResult<QuantityUpdateBulk> results = new PagedResult<QuantityUpdateBulk>(startAt, pageSize);
		Collection<String> colltypes = new ArrayList<String>();

		if (transactionNames != null)
			for (String transactionName : transactionNames)
				if (transactionName != null && transactionName.trim().length() > 0)
					colltypes.add(transactionName);

		
			StringBuffer whereClause = new StringBuffer();
			//if (StringUtils.isNotBlank(title) || colltypes.size() > 0 || location != null)
			//	whereClause.append("where");
			
			if (StringUtils.isNotBlank(title)) {
				whereClause.append(" where ");
				whereClause.append("qu.description.title like :title");
			}
			if (colltypes.size() > 0) {
				if (whereClause.length() > 0)
					whereClause.append(" and ");
				else
					whereClause.append(" where ");
				whereClause.append("qu.description.transactionSubtype in (:types)");
			}
			if (location != null) {
				if (whereClause.length() > 0)
					whereClause.append(" and ");
				else
					whereClause.append(" where ");
				whereClause.append("(qu.lot.location.id=:loc or qu.lot.location in (select lf.pk.child from LocationFlat lf where lf.pk.parent.id=:loc))");
			}
			
			Query countQuery = this.entityManager.createQuery("select count(distinct qu.description) from QuantityUpdate qu " + whereClause.toString());
			Query query = this.entityManager.createQuery("select distinct qu.description from QuantityUpdate qu " + whereClause.toString()
					+ " order by qu.description.date desc, qu.description.id desc");

			if (location != null) {
				countQuery.setParameter("loc", location);
				query.setParameter("loc", location);
			}
			if (colltypes.size() > 0) {
				countQuery.setParameter("types", colltypes);
				query.setParameter("types", colltypes);
			}
			if (StringUtils.isNotBlank(title)) {
				countQuery.setParameter("title", "%" + title + "%");
				query.setParameter("title", "%" + title + "%");
			}
			query.setFirstResult(startAt).setMaxResults(pageSize);

			results.setResults(query.getResultList());
			results.setTotalHits(((Long) countQuery.getSingleResult()).intValue());

//			if (title != null && location > 0) {
//				results.setResults(this.entityManager
//						.createQuery(
//								"select distinct qu.description from QuantityUpdate qu where qu.description.title like :title and (qu.lot.location.id=:loc or qu.lot.location in (select lf.child from LocationFlat lf where lf.parent.id=:loc)) order by qu.description.date desc, qu.description.id desc")
//						.setParameter("loc", location).setParameter("title", "%" + title + "%").setFirstResult(startAt).setMaxResults(pageSize).getResultList());
//				results.setTotalHits(((Long) this.entityManager
//						.createQuery(
//								"select count(distinct qu.description) from QuantityUpdate qu where qu.description.title like :title and (qu.lot.location.id=:loc or qu.lot.location in (select lf.child from LocationFlat lf where lf.parent.id=:loc))")
//						.setParameter("loc", location).setParameter("title", "%" + title + "%").getSingleResult()).intValue());
//			} else if (title == null && location > 0) {
//				results.setResults(this.entityManager
//						.createQuery(
//								"select qu.description from QuantityUpdate qu where qu.lot.location.id=:loc group by qu.description order by qu.description.date desc, qu.description.id desc")
//						.setFirstResult(startAt).setParameter("loc", location).setMaxResults(pageSize).getResultList());
//				results.setTotalHits(((Long) this.entityManager
//						.createQuery("select count(distinct qu.description) from QuantityUpdate qu where qu.lot.location.id=:loc")
//						.setParameter("loc", location).getSingleResult()).intValue());
//			} else if (title != null && location <= 0) {
//				results.setResults(this.entityManager
//						.createQuery(
//								"select qu.description from QuantityUpdate qu where qu.description.title like :title group by qu.description order by qu.description.date desc, qu.description.id desc")
//						.setParameter("title", "%" + title + "%").setFirstResult(startAt).setMaxResults(pageSize).getResultList());
//
//				results.setTotalHits(((Long) this.entityManager
//						.createQuery("select count(distinct qu.description) as total from QuantityUpdate qu where qu.description.title like :title")
//						.setParameter("title", "%" + title + "%").getSingleResult()).intValue());
//
//				//int count = 0;
//				//for (Object[] result : listCount) {
//				//    count = ((Number) result[0]).intValue();
//				//}
//				//results.setTotalHits(count);			
//			} else {
//				results.setResults(this.entityManager.createQuery("from QuantityUpdateBulk bulk order by bulk.date desc, bulk.id desc").setFirstResult(startAt)
//						.setMaxResults(pageSize).getResultList());
//				results.setTotalHits(((Long) this.entityManager.createQuery("select count(*) from QuantityUpdateBulk bulk").getSingleResult()).intValue());
//			}
//		} else {
//			if (title != null && location > 0) {
//				results.setResults(this.entityManager
//						.createQuery(
//								"select qu.description from QuantityUpdate qu where qu.description.transactionSubtype in (:types) and qu.description.title like :title and qu.lot.location.id=:loc group by qu.description order by qu.description.date desc, qu.description.id desc")
//						.setParameter("types", colltypes).setParameter("loc", location).setParameter("title", "%" + title + "%").setFirstResult(startAt)
//						.setMaxResults(pageSize).getResultList());
//				results.setTotalHits(((Long) this.entityManager
//						.createQuery(
//								"select count(distinct qu.description) from QuantityUpdate qu where qu.description.transactionSubtype in (:types) and qu.description.title like :title and qu.lot.location.id=:loc")
//						.setParameter("types", colltypes).setParameter("loc", location).setParameter("title", "%" + title + "%").getSingleResult()).intValue());
//			} else if (title == null && location > 0) {
//				results.setResults(this.entityManager
//						.createQuery(
//								"select qu.description from QuantityUpdate qu where qu.description.transactionSubtype in (:types) and qu.lot.location.id=:loc group by qu.description order by qu.description.date desc, qu.description.id desc")
//						.setFirstResult(startAt).setParameter("types", colltypes).setParameter("loc", location).setMaxResults(pageSize).getResultList());
//				results.setTotalHits(((Long) this.entityManager
//						.createQuery(
//								"select count(distinct qu.description) from QuantityUpdate qu where qu.description.transactionSubtype in (:types) and qu.lot.location.id=:loc")
//						.setParameter("types", colltypes).setParameter("loc", location).getSingleResult()).intValue());
//			} else if (title != null && location <= 0) {
//				results.setResults(this.entityManager
//						.createQuery(
//								"select qu.description from QuantityUpdate qu where qu.description.transactionSubtype in (:types) and qu.description.title like :title group by qu.description order by qu.description.date desc, qu.description.id desc")
//						.setParameter("types", colltypes).setParameter("title", "%" + title + "%").setFirstResult(startAt).setMaxResults(pageSize)
//						.getResultList());
//				results.setTotalHits(((Long) this.entityManager
//						.createQuery(
//								"select count(distinct qu.description) from QuantityUpdate qu where qu.description.transactionSubtype in (:types) and qu.description.title like :title")
//						.setParameter("types", colltypes).setParameter("title", "%" + title + "%").getSingleResult()).intValue());
//			} else {
//				results.setResults(this.entityManager
//						.createQuery("from QuantityUpdateBulk bulk where bulk.transactionSubtype in (:types) order by bulk.date desc, bulk.id desc")
//						.setParameter("types", colltypes).setFirstResult(startAt).setMaxResults(pageSize).getResultList());
//				results.setTotalHits(((Long) this.entityManager
//						.createQuery("select count(*) from QuantityUpdateBulk bulk where bulk.transactionSubtype in (:types)").setParameter("types", colltypes)
//						.getSingleResult()).intValue());
//			}
//		}
		return results;
	}

	/*
	 * (non-Javadoc)
	 * @see org.iita.inventory.service.QuantityUpdateService#list(int, int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<QuantityUpdateBulk> list(int startAt, int maxResults) {
		log.debug("Reading QuantityUpdateBulk startAt=" + startAt + " maxResults=" + maxResults);
		return this.entityManager.createQuery("from QuantityUpdateBulk bulk order by bulk.date desc").setFirstResult(startAt).setMaxResults(maxResults)
				.getResultList();
	}

	/*
	 * (non-Javadoc)
	 * @see org.iita.inventory.service.QuantityUpdateService#countAll()
	 */
	@Override
	@Transactional(readOnly = true)
	public long countAll() {
		log.info("Counting QuantityUpdateBulk records");
		return ((Long) this.entityManager.createQuery("select count(*) from QuantityUpdateBulk bulk").getSingleResult()).longValue();
	}

	/*
	 * (non-Javadoc)
	 * @see org.iita.inventory.service.QuantityUpdateService#loadItem(long)
	 */
	@Override
	@Transactional(readOnly = true)
	public QuantityUpdate loadItem(QuantityUpdateBulk parentUpdateBulk, long itemid) {
		QuantityUpdate item = this.entityManager.find(QuantityUpdate.class, itemid);
		if (parentUpdateBulk.getLots().contains(item))
			return item;
		log.error("Item with id=" + itemid + " requested from bulk id=" + parentUpdateBulk.getId() + " but is not in that bulk.");
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.iita.inventory.service.QuantityUpdateService#findItem(org.iita.inventory.model.QuantityUpdateBulk, org.iita.inventory.model.QuantityUpdate)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<QuantityUpdate> findItem(QuantityUpdateBulk description, Lot lot) {
		return this.entityManager.createQuery("from QuantityUpdate qu where qu.description=:bulk and qu.lot=:lot").setParameter("bulk", description)
				.setParameter("lot", lot).getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public String[] getTransactionSubtypes() {
		return (String[]) this.entityManager.createQuery("select distinct qub.transactionSubtype from QuantityUpdateBulk qub").getResultList()
				.toArray(new String[] {});
	}

	/**
	 * @see org.iita.inventory.service.QuantityUpdateService#getAffectedLots(org.iita.inventory.model.QuantityUpdateBulk)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Lot> getAffectedLots(QuantityUpdateBulk quantityUpdate) {
		return this.entityManager.createQuery("select qu.lot from QuantityUpdate qu where qu.description=:bulk").setParameter("bulk", quantityUpdate)
				.getResultList();
	}

	/**
	 * @return
	 * @see org.iita.inventory.service.QuantityUpdateService#createOrder(org.iita.inventory.model.QuantityUpdateBulk)
	 */
	@Override
	@Transactional
	public GenebankOrder createOrder(QuantityUpdateBulk description) {
		if (description.getOrder() != null)
			throw new RuntimeException("Order already exists");
		if (description.getStatus() == 0)
			throw new RuntimeException("Update not committed to inventory.");
		if (description.getTransactionType() != Transaction2.Type.OUT)
			throw new RuntimeException("This inventory update is not outgoing!");
		if (!"DISTRIBUTION".equalsIgnoreCase(description.getTransactionSubtype()))
			throw new RuntimeException("This inventory update is not marked as DISTRIBUTION.");

		GenebankOrder order = new GenebankOrder();
		order.setTitle(description.getTitle());
		order.setDescription(description.getDescription());
		order.setOrderDate(description.getDate());
		order.setState(OrderState.CLOSED);
		order.setPurpose("Auto-generated");
		this.entityManager.persist(order);

		List<GenebankOrderItem> orderItems = order.getItems();
		for (QuantityUpdate qu : description.getLots()) {
			GenebankOrderItem orderItem = new GenebankOrderItem();
			orderItem.setOrder(order);
			orderItem.setItemName(qu.getLot().getItem().getName());
			orderItem.setItem(qu.getLot().getItem());
			orderItem.setLot(qu.getLot());
			orderItem.setQuantity(qu.getQuantity());
			orderItem.setScale(qu.getScale());
			orderItem.setStatus(OrderItemStatus.DISPATCHED);
			this.entityManager.persist(orderItem);
			orderItems.add(orderItem);
		}

		description.setOrder(order);
		this.entityManager.merge(description);
		return order;
	}
}
