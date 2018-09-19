/**
 * 
 */
package org.iita.inventory.service.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.iita.inventory.model.ContainerType;
import org.iita.inventory.model.ItemType;
import org.iita.inventory.model.Location;
import org.iita.inventory.service.DataAccessService;
import org.iita.inventory.service.ItemService;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author mobreza
 * 
 */
public class DataAccessServiceImpl implements DataAccessService {
	private static final Log log = LogFactory.getLog(ItemService.class);
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
	 * 
	 * @see org.iita.inventory.service.DataAccessService#getContainer(java.lang.String )
	 */
	@Override
	@Transactional(readOnly=true)
	public ContainerType getContainer(String containerName) {
		try {
			return (ContainerType) this.entityManager.createQuery("from ContainerType ct where ct.name=:name").setParameter("name", containerName)
					.getSingleResult();
		} catch (javax.persistence.NoResultException e) {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.iita.inventory.service.DataAccessService#getItemType(java.lang.String )
	 */
	@Override
	@Transactional(readOnly=true)
	public ItemType getItemType(String name) {
		try {
			//System.out.println("3 CROP NAME: " + name);
			return (ItemType) this.entityManager.createQuery("from ItemType it where it.name=:name").setParameter("name", name).getSingleResult();
		} catch (javax.persistence.NoResultException e) {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.iita.inventory.service.DataAccessService#getLocation(java.lang.String)
	 */
	@Override
	@Transactional(readOnly=true)
	public Location getLocation(String locationName) throws NonUniqueResultException {
		try {
			log.debug("Loading location by name: " + locationName);
			return (Location) this.entityManager.createQuery("from Location l where l.name=:name").setParameter("name", locationName).getSingleResult();
		} catch (javax.persistence.NoResultException e) {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.iita.inventory.service.DataAccessService#getLocation(java.lang.String, org.iita.inventory.model.Location)
	 */
	@Override
	@Transactional(readOnly=true)
	public Location getLocation(String locationName, Location parentLocation) {
		// if parent location is not persisted, return null
		if (!this.entityManager.contains(parentLocation))
			return null;

		try {
			return (Location) this.entityManager.createQuery("from Location l where l.parent=:parent and l.name=:name").setParameter("parent", parentLocation)
					.setParameter("name", locationName).getSingleResult();
		} catch (javax.persistence.NoResultException e) {
			log.error("No location '" + locationName + "' in parent " + parentLocation.getName() + " [" + parentLocation.getId() + "]");
			return null;
		} catch (javax.persistence.NonUniqueResultException e) {
			log.error("Duplicate location '" + locationName + "' in parent " + parentLocation.getName() + " [" + parentLocation.getId() + "]");
			log.error(e);
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.iita.inventory.service.DataAccessService#get(java.lang.Class, java.lang.Long)
	 */
	@Override
	@Transactional(readOnly=true)
	public Object get(Class<?> clazz, Long identifier) {
		return this.entityManager.find(clazz, identifier);
	}
	
	/**
	 * @see org.iita.inventory.service.DataAccessService#getAll(java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly=true)
	public List<? extends Object> getAll(Class<?> clazz) {
		return this.entityManager.createQuery("from " + clazz.getName()).getResultList();
	}
}
