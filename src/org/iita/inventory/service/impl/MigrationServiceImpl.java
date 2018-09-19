/**
 * 
 */
package org.iita.inventory.service.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.iita.inventory.model.Location;
import org.iita.inventory.model.Migration;
import org.iita.inventory.service.MigrationService;
import org.iita.util.PagedResult;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author mobreza
 * 
 */
public class MigrationServiceImpl implements MigrationService {
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
	 * @see org.iita.inventory.service.MigrationService#getMigrationsFrom(org.iita.inventory.model.Location)
	 */
	@Override
	@Transactional(readOnly=true)
	public PagedResult<Migration> getMigrationsFrom(Location source, int startAt, int maxResults) {
		PagedResult<Migration> result = new PagedResult<Migration>();
		result.setStartAt(startAt);
		result.setMaxResults(maxResults);
		result.setResults(this.entityManager.createQuery("from Migration m left join fetch m.lot left join fetch m.lot.item where m.oldLocationId=:locid order by m.migrationDate desc").setParameter("locid",
				source.getId()).setFirstResult(startAt).setMaxResults(maxResults).getResultList());
		result.setTotalHits(((Long) this.entityManager.createQuery(
				"select count(*) from Migration m where m.oldLocationId=:locid order by m.migrationDate desc").setParameter("locid", source.getId())
				.getSingleResult()).longValue());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.iita.inventory.service.MigrationService#getMigrationsTo(org.iita.inventory.model.Location)
	 */
	@Override
	@Transactional(readOnly=true)
	public PagedResult<Migration> getMigrationsTo(Location destination, int startAt, int maxResults) {
		PagedResult<Migration> result = new PagedResult<Migration>();
		result.setStartAt(startAt);
		result.setMaxResults(maxResults);
		result.setResults(this.entityManager.createQuery("from Migration m left join fetch m.lot left join fetch m.lot.item where m.newLocationId=:locid order by m.migrationDate desc").setParameter("locid",
				destination.getId()).setFirstResult(startAt).setMaxResults(maxResults).getResultList());
		result.setTotalHits(((Long) this.entityManager.createQuery(
				"select count(*) from Migration m where m.newLocationId=:locid order by m.migrationDate desc").setParameter("locid", destination.getId())
				.getSingleResult()).longValue());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.iita.inventory.service.MigrationService#getLastMigrations(int)
	 */
	@Override
	@Transactional(readOnly=true)
	public PagedResult<Migration> getLastMigrations(int startAt, int maxResults) {
		PagedResult<Migration> result = new PagedResult<Migration>();
		result.setStartAt(startAt);
		result.setMaxResults(maxResults);
		result.setResults(this.entityManager.createQuery("from Migration m left join fetch m.lot left join fetch m.lot.item order by m.migrationDate desc").setFirstResult(startAt).setMaxResults(maxResults).getResultList());
		result.setTotalHits(((Long) this.entityManager.createQuery("select count(*) from Migration m order by m.migrationDate desc").getSingleResult())
				.longValue());
		return result;
	}
}
