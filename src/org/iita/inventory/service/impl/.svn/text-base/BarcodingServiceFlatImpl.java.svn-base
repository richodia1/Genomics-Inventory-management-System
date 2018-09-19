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
import org.iita.inventory.barcode.BarcodingException;
import org.iita.inventory.model.BarCode;
import org.iita.inventory.model.Lot;
import org.iita.inventory.service.BarcodingService;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author mobreza
 */
public class BarcodingServiceFlatImpl implements BarcodingService {
	private static final Log log = LogFactory.getLog(BarcodingService.class);
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

	/**
	 * Assign a free barcode identifier to a lot. If the lot is already associated with a barcode this method does nothing. Assigns a free {@link BarCode} with
	 * the lowest value. If there are no free barcodes available, a new barcode is generated.
	 * 
	 * @see BarCode Barcode
	 * @param lot Inventory lot
	 */
	@Transactional
	public synchronized BarCode assignBarCode(Lot lot) throws BarcodingException {
		if (lot.getBarCode() != null)
			return lot.getBarCode();

		BarCode barCode = null;
		try {
			barCode = (BarCode) this.entityManager.createQuery("from BarCode b where lot is null order by id").setMaxResults(1).getSingleResult();
		} catch (javax.persistence.NoResultException ex) {
		}
		if (barCode == null) {
			barCode = new BarCode();
			this.entityManager.persist(barCode);
			log.warn("Created new barcode " + barCode.getId() + " for lot " + lot.getId());
		} else {
			log.warn("Assigning existing barcode " + barCode.getId() + " to lot " + lot.getId());
		}
		barCode.setDateAssigned(new Date());
		barCode.setLot(lot);
		lot.setBarCode(barCode);
		this.entityManager.merge(lot);
		return barCode;
	}

	/**
	 * Unassociate a barcode from the lot. The barcode becomes available for reuse. If the lot has no barcode associated with it, the method returns
	 * immediately.
	 * 
	 * @param lot Inventory lot
	 */
	@Override
	@Transactional
	public synchronized void removeBarCode(Lot lot) {
		BarCode barCode = lot.getBarCode();
		if (barCode == null)
			return;
		log.warn("Unassigning barcode " + barCode.getId() + " from lot " + lot.getId() + ". Was assigned " + barCode.getDateAssigned());
		lot.setBarCode(null);
		barCode.setLot(null);
		this.entityManager.merge(lot);
		this.entityManager.merge(barCode);
		log.warn("Barcode " + barCode.getId() + " unassigned from lot " + lot.getId());
	}

	/**
	 * Ensure the lot has a barcode assigned.
	 * 
	 * @throws BarcodingException
	 * @see #assignBarCode(Lot)
	 * @see #ensureBarCode(List<? extends Lot>)
	 */
	@Transactional
	public synchronized void ensureBarCode(Lot lot) throws BarcodingException {
		BarCode barCode = lot.getBarCode();
		if (barCode == null)
			this.assignBarCode(lot);
	}

	/**
	 * Ensure all lots in the list have barcodes assigned.
	 * 
	 * @see #ensureBarCode(Lot)
	 * @param selectedLots
	 * @throws BarcodingException
	 */
	@Override
	@Transactional
	public synchronized void ensureBarCode(List<? extends Lot> selectedLots) throws BarcodingException {
		for (Lot lot : selectedLots) {
			this.ensureBarCode(lot);
		}
	}
}
