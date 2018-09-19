/**
 * 
 */
package org.iita.inventory.service.impl;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.iita.inventory.model.Lot;
import org.iita.inventory.model.LotVariable;
import org.iita.inventory.model.LotVariableUpdate;
import org.iita.inventory.model.LotVariableUpdateBulk;
import org.iita.inventory.model.Transaction3;
import org.iita.inventory.model.Variables;
import org.iita.inventory.model.Transaction3.Source;
import org.iita.inventory.model.Transaction3.Type;
import org.iita.inventory.service.LotVariableUpdateService;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author KOraegbunam
 *
 */
public class LotVariableUpdateServiceImpl implements LotVariableUpdateService {
	private static final Log log = LogFactory.getLog(LotVariableUpdateService.class);
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
	public void commit(LotVariableUpdateBulk lotVariableUpdate) {
		log.warn("Commit of " + lotVariableUpdate.getId() + " " + lotVariableUpdate.getTitle() + " requested.");
		if (lotVariableUpdate.getStatus() != 0) {
			log.info("Cannot commit an already commited bulk update.");
			throw new IllegalStateException("Cannot commit an already commited bulk update.");
		}

		// update all lot variables
		for (LotVariableUpdate singleItem : lotVariableUpdate.getLotVariables()) {
			// check quantity
			LotVariable lotVariable = singleItem.getLotVariable();
			singleItem.setOriginalQty(lotVariable.getQuantity());

			// create transaction record
			Transaction3 transaction = new Transaction3();
			transaction.setDate(new Date());
			transaction.setSubtype(lotVariableUpdate.getTransactionSubtype());
			transaction.setSource(Source.BULK);
			transaction.setRel(lotVariableUpdate.getId());
			transaction.setLotVariable(lotVariable);
			// depending on bulk update type, the quantity is either - or +
			if (lotVariableUpdate.getTransactionType() == Type.OUT)
				transaction.setQuantity(-singleItem.getQuantity());
			else if (lotVariableUpdate.getTransactionType() == Type.IN)
				transaction.setQuantity(singleItem.getQuantity());
			else if (lotVariableUpdate.getTransactionType() == Type.RSET) {
				transaction.setSubtype("RESET");
				transaction.setQuantity(singleItem.getQuantity());
			}
			this.entityManager.persist(transaction);

			if (lotVariableUpdate.isAffectingInventory()) {
				
				if (lotVariableUpdate.getTransactionType() == Type.RSET) {
					if (transaction.getQuantity() < 0)
						// quantity cannot be negative
						throw new IllegalStateException("Cannot set quantity to negative value.");
					// update lot
					lotVariable.setQuantity(transaction.getQuantity());
				} else {
					if (lotVariable.getQuantity() + transaction.getQuantity() < 0.0d)
						// check that resulting quantity is not negative
						throw new IllegalStateException("Available QTY: " + lotVariable.getQuantity() + " Transaction QTY: " + transaction.getQuantity() + "  in lot "
								+ lotVariable.getId() + " item " + lotVariable.getLot().getItem().getName() + ". Would result in negative quantity!");
					// update lotVariable
					//lotVariable.setQuantity(lotVariable.getQuantity() + transaction.getQuantity());
					lotVariable.setQuantity(transaction.getQuantity());
				}
			}
			
			//Subtracting from Lot quantity when Variable is sorted
			if(lotVariable.getVariable().getName().contains("Sorted")){
				lotVariable.getLot().setQuantity(lotVariable.getLot().getQuantity() - transaction.getQuantity());
				this.entityManager.merge(lotVariable.getLot());
			}
			
			if(verifyLotVariable(lotVariable.getVariable(), lotVariable.getLot())){
				this.entityManager.merge(lotVariable);
			}else{
				this.entityManager.persist(lotVariable);
			}
		}

		// set status to COMMITED
		lotVariableUpdate.setStatus(1);
		this.entityManager.merge(lotVariableUpdate);
		log.warn("Commit of " + lotVariableUpdate.getId() + " " + lotVariableUpdate.getTitle() + " finished.");
	}
	
	public boolean verifyLotVariable(Variables variable, Lot lot){
		long hit = ((Long) this.entityManager.createQuery("select count(lv) from LotVariable lv where lv.variable=:variable and lv.lot=:lot").setParameter("variable", variable).setParameter("lot",lot).getSingleResult()).intValue();
		
		if(hit>0)
			return true;
		else
			return false;
	}

	@Override
	@Transactional
	public void store(LotVariableUpdateBulk lotVariableUpdate) {
		if (lotVariableUpdate == null)
			throw new NullPointerException();

		if (lotVariableUpdate.getId() != null) {
			log.warn("Updating existing bulk update id=" + lotVariableUpdate.getId());
			this.entityManager.merge(lotVariableUpdate);
		} else {
			log.info("Inserting Bulk update \"" + lotVariableUpdate.getTitle() + "\" " + lotVariableUpdate.getTransactionSubtype() + " " + lotVariableUpdate.getTransactionType());
			this.entityManager.persist(lotVariableUpdate);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public LotVariable loadForUpdate(Variables variable, Lot lot) {
		try{
			return (LotVariable) this.entityManager.createQuery("from LotVariable lv where lv.variable=:variable and lv.lot=:lot").setParameter("variable", variable).setParameter("lot",lot).getSingleResult();
		}catch(Exception e){
			return null;
		}
	}

}
