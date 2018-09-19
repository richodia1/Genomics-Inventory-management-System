/**
 * 
 */
package org.iita.inventory.service.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.iita.inventory.model.FieldVariablesList;
import org.iita.inventory.model.Lot;
import org.iita.inventory.model.LotVariable;
import org.iita.inventory.service.LotVariableService;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author KOraegbunam
 *
 */
public class LotVariableServiceImpl implements LotVariableService {
	private static final Log log = LogFactory.getLog(LotVariableService.class);
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
	@Transactional(readOnly = true)
	public LotVariable loadLotVariable(Long id) {
		log.info("Loading Lot variable by id: " + id);
		return this.entityManager.find(LotVariable.class, id);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.iita.inventory.service.LotService#load(long)
	 */
	@Override
	@Transactional(readOnly = true)
	public LotVariable loadLotVariable(long id) {
		return this.entityManager.find(LotVariable.class, id);
	}
	
	/**
	 * @see org.iita.inventory.service.LotVariableService#getLotVariablesByBarcode(java.util.List)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<LotVariable> getLotVariablesByBarcode(Long lotBarcode) {
		if (lotBarcode == null)
			return null;
		
		//System.out.println("PARSED BARCODES: " + lotBarcodes);
		//System.out.println("PARSED BARCODES: " + lotBarcodes.size());
		return this.entityManager.createQuery("from LotVariable lv where lv.lot.barCode.id=:id").setParameter("id", lotBarcode).getResultList();
	}
	
	/**
	 * Get list of field variables
	 * 
	 * @see org.iita.inventory.service.LotVariableService#loadFieldVariablesList()
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<FieldVariablesList> loadFieldVariablesList() {
		return this.entityManager.createQuery("from FieldVariablesList f order by f.id asc").getResultList();
	}
	
	/**
	 * Get list of lot variables for selected lot
	 * 
	 * @see org.iita.inventory.service.LotVariableService#loadLotVariables(org.iita.inventory.model.Lot)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<LotVariable> loadLotVariables(Lot lot) {
		return this.entityManager.createQuery("from LotVariable lv where lv.lot=:lot order by lv.variable.name asc").setParameter("lot", lot)
		.getResultList();
	}

	/**
	 * @see org.iita.inventory.service.LotVariableService#updateLotVariables(org.iita.inventory.model.LotVariable)
	 */
	@Override
	@Transactional
	public void updateLotVariables(LotVariable lotVariable) {
		if(lotVariable.getId()!=null){
			this.entityManager.merge(lotVariable);
		}else{
			this.entityManager.persist(lotVariable);
		}
	}

	@Override
	@Transactional
	public void deleteLotVariables(LotVariable lotVariable) {
		if(lotVariable.getId()!=null)
			this.entityManager.remove(lotVariable);
	}
}
