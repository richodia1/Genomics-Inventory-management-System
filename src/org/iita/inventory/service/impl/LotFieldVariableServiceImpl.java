/**
 * 
 */
package org.iita.inventory.service.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.iita.inventory.model.FieldVariables;
import org.iita.inventory.service.LotFieldVariableService;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author KOraegbunam
 *
 */
public class LotFieldVariableServiceImpl implements LotFieldVariableService {
	private static final Log log = LogFactory.getLog(LotFieldVariableService.class);
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
	 * @see org.iita.inventory.service.LotVariableService#getLotVariablesByBarcode(java.util.List)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<FieldVariables> getLotFieldVariablesByLotId(Long lotId) {
		if (lotId == null)
			return null;
		log.info("getting field variables for supplied lot id");
		return this.entityManager.createQuery("from FieldVariables lv where lv.lot=:id").setParameter("id", lotId).getResultList();
	}
}
