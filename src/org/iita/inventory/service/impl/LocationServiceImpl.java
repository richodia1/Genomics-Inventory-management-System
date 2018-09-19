/**
 * 
 */
package org.iita.inventory.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.iita.inventory.model.FieldVariables;
import org.iita.inventory.model.Item;
import org.iita.inventory.model.Location;
import org.iita.inventory.model.Lot;
import org.iita.inventory.model.LotTraitLastValue;
import org.iita.inventory.model.LotTraitValue;
import org.iita.inventory.model.LotVariable;
import org.iita.inventory.model.Migration;
import org.iita.inventory.model.QuantityUpdate;
import org.iita.inventory.model.Transaction2;
import org.iita.inventory.service.LocationService;
import org.iita.util.NaturalOrderComparator;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author mobreza
 */
public class LocationServiceImpl implements LocationService {
	private static final Log log = LogFactory.getLog(LocationService.class);
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
	 * @see org.iita.inventory.service.LocationService#listLocations()
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Location> listLocations() {
		log.info("Listing ROOT locations.");
		return this.entityManager.createQuery("from Location l where l.parent=null").getResultList();
	}

	/*
	 * (non-Javadoc)
	 * @see org.iita.inventory.service.LocationService#load(long)
	 */
	@Override
	@Transactional(readOnly = true)
	public Location load(long locid) {
		log.debug("Loading Location id=" + locid);
		return this.entityManager.find(Location.class, locid);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.iita.inventory.service.LocationService#load(long)
	 */
	@Override
	@Transactional(readOnly = true)
	public Location lookUpChildLoc(Location loc, String childName) {
		log.info("Loading Parent Location id=" + loc.getId());
		log.info("Loading Child Location Name=" + childName);
		return (Location) this.entityManager.createQuery("from Location l where l.parent=:loc and l.name=:childName").setParameter("loc", loc).setParameter("childName",childName).getSingleResult();
	}

	@Transactional
	public void store(Location location) {
		if (location.getId() == null)
			this.entityManager.persist(location);
		else
			this.entityManager.merge(location);
	}
	
	@Transactional
	public Location storeLocation(Location location) {
		if (location.getId() == null){
			this.entityManager.persist(location);
			return location;
		}else
			return this.entityManager.merge(location);
	}

	/*
	 * (non-Javadoc)
	 * @see org.iita.inventory.service.LocationService#remove(org.iita.inventory.model.Location)
	 */
	@Override
	@Transactional
	public void remove(Location location) {
		this.entityManager.remove(location);
		this.entityManager.flush();
	}

	/**
	 * Get a list of sub-locations for location
	 */
	@Override
	@Transactional(readOnly = true)
	public Collection<Location> getSubLocations(Location location) {
		List<Location> loc = new ArrayList<Location>(location == null ? this.listLocations() : location.getChildren());
		Collections.sort(loc, new NaturalOrderComparator<Location>() {
			@Override
			public int compare(Location o1, Location o2) {
				return naturalCompare(o1.getName(), o2.getName());
			}
		});
		return loc;
	}

	/**
	 * Remove all data from LocationFlat table and regenerate contents of database
	 * 
	 * @see org.iita.inventory.service.LocationService#regenerateLocationFlat()
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(rollbackFor = Throwable.class)
	public void regenerateLocationFlat() {
		log.info("Regenerating contents of LocationFlat table...");
		this.entityManager.createQuery("delete from LocationFlat").executeUpdate();
		List<Object[]> locationData = this.entityManager.createNativeQuery("select l.id, l.parentId from Location l").getResultList();
		Query insertQuery = this.entityManager.createNativeQuery("insert into LocationFlat (parentId, id) values (?, ?)");
		Hashtable<BigInteger, BigInteger> parentTree = new Hashtable<BigInteger, BigInteger>();

		// first pass: generate id-parentId cache
		for (Object[] ld : locationData) {
			if (ld[1] != null)
				parentTree.put((BigInteger) ld[0], (BigInteger) ld[1]);

			// also insert
			log.debug("Inserting parent=" + ld[1] + " child=" + ld[0]);
			insertQuery.setParameter(1, ld[1]);
			insertQuery.setParameter(2, ld[0]);
			insertQuery.executeUpdate();
		}

		// second pass, get all parents
		for (BigInteger key : parentTree.keySet()) {
			BigInteger orgKey = key;
			BigInteger parentId = parentTree.get(orgKey);

			while (parentId != null) {
				key = parentId;
				parentId = parentTree.get(key);
				if (parentId != null) {
					log.debug("Inserting parent=" + parentId + " child=" + orgKey);
					insertQuery.setParameter(1, parentId);
					insertQuery.setParameter(2, orgKey);
					insertQuery.executeUpdate();
				}
			}
			
			log.debug("Inserting parent=" + parentId + " child=" + orgKey);
			insertQuery.setParameter(1, null);
			insertQuery.setParameter(2, orgKey);
			insertQuery.executeUpdate();
		}
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Throwable.class)
	public void removeLots(long[] lotIds) {
		try{
			List<Item> items = new ArrayList<Item>();
			for(long id : lotIds){
				Lot lot = (Lot) this.entityManager.createQuery("from Lot l where l.id=:id").setParameter("id", id).getSingleResult();
				
				if(lot != null){
					if(!items.contains(lot.getItem()))
						items.add(lot.getItem());
					
					//Delete lots here with individual lot ID
					this.entityManager.remove(lot.getBarCode());
					for(Transaction2 trans : lot.getTransactions()){
						this.entityManager.remove(trans);
					}
					
					QuantityUpdate qUp = (QuantityUpdate) this.entityManager.createQuery("from QuantityUpdate q where q.lot=:lot").setParameter("lot", lot).getSingleResult();
					if(qUp!=null)
						this.entityManager.remove(qUp);
					
					for(LotVariable lv : lot.getLotVariable()){
						this.entityManager.remove(lv);
					}
					
					for(Migration mig : lot.getMigrations()){
						this.entityManager.remove(mig);
					}
					
					for(LotTraitLastValue tlv : lot.getTraitLastValues()){
						this.entityManager.remove(tlv);
					}
					
					for(LotTraitValue tv : lot.getTraitValues()){
						this.entityManager.remove(tv);
					}
					
					for(FieldVariables fv : lot.getFieldVariables()){
						this.entityManager.remove(fv);
					}
					
					this.entityManager.remove(lot);
				}
			}
			
			//Delete items here with individual item object after deleting lots by IDs
			if(!items.isEmpty())
				for(Item item : items)
					this.entityManager.remove(item);
		}
		catch(Exception ex){
			
		}
	}
}
