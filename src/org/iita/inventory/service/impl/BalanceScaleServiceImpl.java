/**
 * 
 */
package org.iita.inventory.service.impl;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.iita.inventory.label.ScaleException;
import org.iita.inventory.printing.BalanceScaleInfo;
import org.iita.inventory.service.BalanceScaleService;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author ken
 *
 */
public class BalanceScaleServiceImpl implements BalanceScaleService {
	private static final Log LOG = LogFactory.getLog(BalanceScaleService.class);
	private EntityManager entityManager;
	private Dictionary<String, Integer> selectedScales = new Hashtable<String, Integer>();

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
	public void store(BalanceScaleInfo scaleInfo) {
		LOG.warn("Storing scale: " + scaleInfo);
		if (scaleInfo.getId() == null)
			this.entityManager.persist(scaleInfo);
		else
			this.entityManager.merge(scaleInfo);
	}

	@Override
	@Transactional
	public void delete(BalanceScaleInfo scaleInfo) {
		LOG.warn("Deleting scale: " + scaleInfo);
		this.entityManager.remove(scaleInfo);
	}

	@Override
	@Transactional(readOnly=true)
	public BalanceScaleInfo find(String scaleSessionId) {
		Integer scaleInfoId = this.selectedScales.get(scaleSessionId);
		if(scaleInfoId!=null)
			return this.entityManager.find(BalanceScaleInfo.class, scaleInfoId);
		else
			return null;
	}
	
	@Transactional(readOnly=true)
	public BalanceScaleInfo find(Integer id) {
		return this.entityManager.find(BalanceScaleInfo.class, id);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.iita.inventory.service.BalanceScaleService#list()
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly=true)
	public List<BalanceScaleInfo> list() {
		return this.entityManager.createQuery("from BalanceScaleInfo pi order by pi.host").getResultList();
	}
	
	@Override
	public List<BalanceScaleInfo> getScales() {
		return list();
	}

	@Override
	public void selectBalanceScale(String sessionId, BalanceScaleInfo scale) throws ScaleException {
		if (sessionId == null) {
			LOG.error("Session ID is null when trying to select scale");
			throw new ScaleException("Session ID cannot be null");
		}
		if (scale == null) {
			LOG.info("Unselecting scale for session " + sessionId);
			this.selectedScales.remove(sessionId);
		} else {
			LOG.info("Selecting scale " + scale.getHost() + " for " + sessionId);
			this.selectedScales.put(sessionId, scale.getId());
		}		
	}

	@Override
	@Transactional
	public void deleteScale(Integer id) {
		BalanceScaleInfo sf = this.entityManager.find(BalanceScaleInfo.class, id);
		if(sf!=null){
			this.entityManager.remove(sf);
		}
	}

}
