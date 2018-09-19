/**
 * 
 */
package org.iita.inventory.service.impl;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.iita.inventory.model.Location;
import org.iita.inventory.model.Lot;
import org.iita.inventory.model.Migration;
import org.iita.inventory.model.MigrationLotLocationUpdate;
import org.iita.inventory.model.MigrationLotLocationUpdateBulk;
import org.iita.inventory.service.LocationService;
import org.iita.inventory.service.LotLocationMigrationUpdateService;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author KOraegbunam
 *
 */
public class LotLocationMigrationUpdateServiceImpl implements LotLocationMigrationUpdateService {
	private static final Log log = LogFactory.getLog(LotLocationMigrationUpdateService.class);
	private EntityManager entityManager;
	private LocationService locationService;

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
	 * @param locationService the locationService to set
	 */
	public void setLocationService(LocationService locationService) {
		this.locationService = locationService;
	}
	
	@Override
	@Transactional
	public void delete(MigrationLotLocationUpdateBulk bulkUpdate) {
		log.warn("Delete of " + bulkUpdate.getId() + " requested.");
		if (bulkUpdate.getStatus() != 0) {
			log.info("Can only delete non-commited bulk updates.");
			throw new IllegalStateException("Can only delete non-commited bulk updates.");
		}

		// set status to COMMITED
		this.entityManager.remove(bulkUpdate);
		log.warn("Delete of " + bulkUpdate.getId() + " finished.");
	}
	
	@Override
	@Transactional
	public void store(MigrationLotLocationUpdateBulk lotLocationMigrationUpdate) {
		if (lotLocationMigrationUpdate == null)
			throw new NullPointerException();

		if (lotLocationMigrationUpdate.getId() != null) {
			log.warn("Updating existing bulk update id=" + lotLocationMigrationUpdate.getId());
			this.entityManager.merge(lotLocationMigrationUpdate);
		} else {
			log.info("Inserting Bulk update \"" + lotLocationMigrationUpdate.getId());
			this.entityManager.persist(lotLocationMigrationUpdate);
		}		
	}
	
	@Override
	@Transactional
	public void commit(MigrationLotLocationUpdateBulk lotLocationMigrationUpdate) {
		log.warn("Commit of " + lotLocationMigrationUpdate.getId() + " requested.");
		if (lotLocationMigrationUpdate.getStatus() != 0) {
			log.info("Cannot commit an already commited bulk update.");
			throw new IllegalStateException("Cannot commit an already commited bulk update.");
		}

		// update all lot quantities
		Location location = new Location();
		for (MigrationLotLocationUpdate singleItem : lotLocationMigrationUpdate.getLotLocationUpdate()) {
			// check quantity
			Lot lot = singleItem.getLot();

			// create transaction record
			Migration migrate = new Migration();
			migrate.setCreatedBy(singleItem.getDescription().getCreatedBy());
			migrate.setCreatedDate(new Date());
			migrate.setLot(lot);
			migrate.setMigrationDate(new Date());
			
			if(singleItem.getToLocationId()!=null){
				System.out.println("ToLocID: " + singleItem.getToLocationId());
				migrate.setNewLocationId(singleItem.getToLocationId());
				migrate.setOldLocationId(singleItem.getFromLocation());
				migrate.setOldLocationName(lot.getLocation().getPathNames());
				
				location = this.locationService.load(singleItem.getToLocationId());
				
				migrate.setNewLocationName(location.getPathNames());
				lot.setLocation(location);
				this.entityManager.persist(migrate);
			}else if(singleItem.getToLocation()!=null){
				System.out.println("ToLocID: NULL");
				location.setLocationType(null);
				location.setName(null);
				location.setParent(null);
				location.setVersion(0);
				
				this.entityManager.persist(location);
				migrate.setNewLocationId(location.getId());
				migrate.setNewLocationName(location.getPathNames());
				
				lot.setLocation(location);
				this.entityManager.persist(migrate);
			}			
			this.entityManager.merge(lot);
		}
		
		// set status to COMMITED
		lotLocationMigrationUpdate.setStatus(1);
		this.entityManager.merge(lotLocationMigrationUpdate);
		log.warn("Commit of " + lotLocationMigrationUpdate.getId() + " finished.");
	}

	@Override
	public void delete(MigrationLotLocationUpdate singleUpdate) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public MigrationLotLocationUpdateBulk load(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void store(MigrationLotLocationUpdate singleUpdate) {
		// TODO Auto-generated method stub
		
	}
}
