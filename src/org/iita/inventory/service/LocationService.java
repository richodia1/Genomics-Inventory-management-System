/**
 * 
 */
package org.iita.inventory.service;

import java.util.Collection;
import java.util.List;

import org.iita.inventory.model.Location;

/**
 * @author mobreza
 */
public interface LocationService {
	/**
	 * Get list of root locations
	 * 
	 * @return list of root locations
	 */
	public List<Location> listLocations();

	/**
	 * @param locid
	 * @return
	 */
	public Location load(long locid);

	/**
	 * Presist location data
	 * 
	 * @param location
	 */
	public void store(Location location);
	
	/**
	 * Presist location data
	 * 
	 * @param location
	 */
	public Location storeLocation(Location location);

	/**
	 * Remove location
	 * 
	 * @param location
	 */
	public void remove(Location location);

	public Collection<Location> getSubLocations(Location location);

	/**
	 * Metho to regenerate data in LocationFlat table
	 */
	public void regenerateLocationFlat();

	public Location lookUpChildLoc(Location loc, String childName);

	public void removeLots(long[] lotIds);
}
