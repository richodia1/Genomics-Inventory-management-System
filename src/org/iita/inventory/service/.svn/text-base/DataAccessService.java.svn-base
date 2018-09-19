/**
 * 
 */
package org.iita.inventory.service;

import java.util.List;

import javax.persistence.NonUniqueResultException;

import org.iita.inventory.model.ContainerType;
import org.iita.inventory.model.ItemType;
import org.iita.inventory.model.Location;

/**
 * @author mobreza
 * 
 */
public interface DataAccessService {
	/**
	 * Find ItemType by name
	 * 
	 * @param name
	 * @return
	 */
	public ItemType getItemType(String name) throws NonUniqueResultException;

	/**
	 * Find Container by name
	 * 
	 * @param containerName
	 * @return
	 */
	public ContainerType getContainer(String containerName) throws NonUniqueResultException;

	/**
	 * @param storeName
	 * @return
	 */
	public Location getLocation(String locationName) throws NonUniqueResultException;

	/**
	 * @param sublocationName
	 * @param store
	 * @return
	 */
	public Location getLocation(String locationName, Location parentLocation);

	/**
	 * @param arg2
	 * @param string
	 * @return
	 */
	public Object get(Class<?> clazz, Long identifier);

	/**
	 * @param class1
	 * @return
	 */
	public List<? extends Object> getAll(Class<?> class1);
}
