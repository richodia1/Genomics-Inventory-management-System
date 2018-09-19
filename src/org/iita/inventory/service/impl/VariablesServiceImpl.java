/**
 * 
 */
package org.iita.inventory.service.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.iita.inventory.model.Variables;
import org.iita.inventory.service.LotService;
import org.iita.inventory.service.VariablesService;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author KOraegbunam
 *
 */
public class VariablesServiceImpl implements VariablesService {
	private static final Log log = LogFactory.getLog(LotService.class);
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
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Variables> listVariables() throws Exception {
		log.info("Loading variables list");
		return this.entityManager.createQuery("from Variables var order by var.name ASC").getResultList();
	}

	@Override
	@Transactional
	public void update(Variables variable) {
		if (variable.getId() == null)
			this.entityManager.persist(variable);
		else
			this.entityManager.merge(variable);
	}

	@Override
	@Transactional
	public void remove(Variables variable) {
		this.entityManager.remove(variable);
	}

	/*
	 * (non-Javadoc)
	 * @see org.iita.inventory.service.LotService#load(long)
	 */
	@Override
	@Transactional(readOnly = true)
	public Variables loadVariable(long id) {
		return this.entityManager.find(Variables.class, id);
	}
}
