/**
 * 
 */
package org.iita.inventory.service.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.iita.inventory.model.Lot;
import org.iita.inventory.model.LotSubtypeUpdate;
import org.iita.inventory.model.LotSubtypeUpdateBulk;
import org.iita.inventory.model.Transaction2;
import org.iita.inventory.model.Transaction2.Source;
import org.iita.inventory.model.Transaction2.Type;
import org.iita.inventory.service.LotSubtypeUpdateService;
import org.iita.inventory.service.QuantityUpdateService;
import org.iita.util.PagedResult;
import org.springframework.security.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author KOraegbunam
 *
 */
public class LotSubtypeUpdateServiceImpl implements LotSubtypeUpdateService {
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
	
	@Override
	@Transactional
	public void delete(LotSubtypeUpdateBulk bulkUpdate) {
		log.warn("Delete of " + bulkUpdate.getId() + " " + bulkUpdate.getTitle() + " requested.");
		if (bulkUpdate.getStatus() != 0) {
			log.info("Can only delete non-commited bulk updates.");
			throw new IllegalStateException("Can only delete non-commited bulk updates.");
		}

		// set status to COMMITED
		this.entityManager.remove(bulkUpdate);
		log.warn("Delete of " + bulkUpdate.getId() + " " + bulkUpdate.getTitle() + " finished.");
	}
	
	@Override
	@Transactional
	public void store(LotSubtypeUpdateBulk lotSubtypeUpdate) {
		System.out.println("Lot Subtype Update: " + lotSubtypeUpdate);
		log.info("LotSubTypeUpdate: " + lotSubtypeUpdate);
		
		if (lotSubtypeUpdate == null){
			//throw new NullPointerException();
		}else{
			if (lotSubtypeUpdate.getId() != null) {
				log.warn("Updating existing bulk update id=" + lotSubtypeUpdate.getId());
				this.entityManager.merge(lotSubtypeUpdate);
			} else {
				log.info("Inserting Bulk update \"" + lotSubtypeUpdate.getTitle() + "\" " + lotSubtypeUpdate.getTransactionSubtype() + " " + lotSubtypeUpdate.getTransactionType());
				this.entityManager.persist(lotSubtypeUpdate);
			}
		}
	}
	
	@Override
	@Transactional
	public void commit(LotSubtypeUpdateBulk lotSubtypeUpdate) {
		log.warn("Commit of " + lotSubtypeUpdate.getId() + " " + lotSubtypeUpdate.getTitle() + " requested.");
		if (lotSubtypeUpdate.getStatus() != 0) {
			log.info("Cannot commit an already commited bulk update.");
			throw new IllegalStateException("Cannot commit an already commited bulk update.");
		}

		// update all lot quantities
		for (LotSubtypeUpdate singleItem : lotSubtypeUpdate.getLotSubtypes()) {
			// check quantity
			Lot lot = singleItem.getLot();
			singleItem.setOriginalQty(lot.getQuantity());
			log.info("CHECK TRANS QTY: " + singleItem.getQuantity());
			log.info("TYPE: " + lotSubtypeUpdate.getTransactionSubtype());
			// create transaction record
			Transaction2 transaction = new Transaction2();
			transaction.setDate(new Date());
			transaction.setSubtype(lotSubtypeUpdate.getTransactionSubtype());
			transaction.setSource(Source.BULK);
			transaction.setRel(lotSubtypeUpdate.getId());
			transaction.setLot(lot);
			// depending on bulk update type, the quantity is either - or +
			if (lotSubtypeUpdate.getTransactionType().equals(Type.OUT))
				transaction.setQuantity(-singleItem.getQuantity());
			else if (lotSubtypeUpdate.getTransactionType().equals(Type.IN))
				transaction.setQuantity(singleItem.getQuantity());
			else if (lotSubtypeUpdate.getTransactionType().equals(Type.RSET)) {
				transaction.setSubtype("RESET");
				transaction.setQuantity(singleItem.getQuantity());
			}
			transaction.setScale(singleItem.getLot().getScale());
			this.entityManager.persist(transaction);

			if (lotSubtypeUpdate.isAffectingInventory()) {
				// check scale
				if (!lot.getScale().equalsIgnoreCase(transaction.getScale())) {
					throw new IllegalStateException("Lot scale is different from requested scale: " + transaction.getScale());
				}
				if (lotSubtypeUpdate.getTransactionType() == Type.RSET) {
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
		lotSubtypeUpdate.setStatus(1);
		this.entityManager.merge(lotSubtypeUpdate);
		log.warn("Commit of " + lotSubtypeUpdate.getId() + " " + lotSubtypeUpdate.getTitle() + " finished.");
	}

	@Override
	public long countAll() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	@Transactional
	public void delete(LotSubtypeUpdate singleUpdate) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<LotSubtypeUpdate> findItem(LotSubtypeUpdateBulk description, Lot lot) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Lot> getAffectedLots(LotSubtypeUpdateBulk quantityUpdate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getTransactionSubtypes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<LotSubtypeUpdateBulk> list(int startAt, int maxResults) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PagedResult<LotSubtypeUpdateBulk> listByType(int startAt, int pageSize, String... transactionNames) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LotSubtypeUpdateBulk load(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Lot loadForUpdate(Lot lot) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LotSubtypeUpdate loadItem(LotSubtypeUpdateBulk parentUpdateBulk, long itemid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Secured( { "ROLE_ROLLBACK" })
	@Transactional
	public void rollback(LotSubtypeUpdateBulk bulkUpdate) {
		log.warn("Rollback of " + bulkUpdate.getId() + " " + bulkUpdate.getTitle() + " requested.");
		if (bulkUpdate.getStatus() != 1) {
			log.error("Cannot rollback a non-commited bulk update.");
			throw new IllegalStateException("Cannot rollback a non-commited bulk update.");
		}

		// update all lot subtype quantities
		for (LotSubtypeUpdate singleItem : bulkUpdate.getLotSubtypes()) {
			// check quantity
			Lot lot = singleItem.getLot();

			Transaction2 transaction = new Transaction2();
			transaction.setDate(new Date());
			transaction.setSource(Source.BULK);
			transaction.setRel(bulkUpdate.getId());
			transaction.setLot(lot);
			transaction.setScale(singleItem.getLot().getScale());

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
	}

	@Override
	public void store(LotSubtypeUpdate singleUpdate) {
		// TODO Auto-generated method stub
		log.info("Non-implemented method");
	}

	@Override
	public boolean verifyLot(Lot lot) {
		// TODO Auto-generated method stub
		return false;
	}

}
