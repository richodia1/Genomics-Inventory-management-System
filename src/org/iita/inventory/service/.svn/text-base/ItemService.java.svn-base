/**
 * 
 */
package org.iita.inventory.service;

import java.util.List;

import javax.persistence.NonUniqueResultException;

import org.iita.inventory.model.ContainerType;
import org.iita.inventory.model.Item;
import org.iita.inventory.model.ItemType;
import org.iita.inventory.model.Lot;
import org.iita.util.PagedResult;

/**
 * @author mobreza
 * 
 */
public interface ItemService {
	public Item find(long id);

	public void store(Item item);

	public void delete(Item item);

	/**
	 * @param itemNameFilter
	 * @param startat
	 * @param pagesize
	 * @return
	 */
	public PagedResult<Item> search(String itemNameFilter, int startat, int pagesize);

	/**
	 * Find item by name. Format: itemType/itemName. Example: findByName("Musa/TMp-232");
	 * 
	 * @param itemName
	 * @return
	 */
	public Item findByName(String itemName);

	/**
	 * @param itemType
	 * @param itemName
	 * @return
	 */
	public Item find(ItemType itemType, String itemName) throws NonUniqueResultException;

	/**
	 * List all existing item types
	 * 
	 * @return
	 */
	public List<ItemType> listItemTypes();

	public void storeType(ItemType itype);

	public ItemType findType(long id);

	public void deletetype(ItemType itype);

	/**
	 * @return
	 */
	public List<ContainerType> getContainerTypes();

	public void storeContainerType(ContainerType ctype);

	public boolean deleteContainerType(ContainerType findContainerType);

	public ContainerType findContainerType(long id);

	/**
	 * Search DB for items with names in the <b>itemNames</b> text
	 * 
	 * @param accessionNames
	 */
	public List<Item> findItemsByName(String itemNames);

	public List<Lot> findValidLots(Item item);
}
