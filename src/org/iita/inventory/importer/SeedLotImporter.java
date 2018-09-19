/**
 * 
 */
package org.iita.inventory.importer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

import javax.persistence.NonUniqueResultException;

import org.iita.inventory.model.ContainerType;
import org.iita.inventory.model.Item;
import org.iita.inventory.model.ItemType;
import org.iita.inventory.model.Location;
import org.iita.inventory.model.SeedLot;
import org.iita.inventory.service.DataAccessService;
import org.iita.inventory.service.ItemService;
import org.iita.util.CSVDataReader;
import org.iita.util.CSVDataReader.DataType;
import org.jfree.util.Log;

/**
 * @author mobreza
 * @author koreaegbunam
 * 
 */
public class SeedLotImporter implements Importer {
	private CSVDataReader reader;
	List<SeedLot> lots = null;
	DataAccessService dao = null;
	private Hashtable<String, ContainerType> cachedContainerTypes;
	private Hashtable<String, ItemType> cachedItemTypes;
	private Hashtable<String, Location> cachedLocations;
	private Hashtable<String, Item> cachedItems;
	private ItemService itemService;

	/**
	 * Set DataAccessService to fetch data from database
	 * 
	 * @param dao the dao to set
	 */
	public void setDao(DataAccessService dao) {
		this.dao = dao;
	}

	/**
	 * @param itemService the itemService to set
	 */
	public void setItemService(ItemService itemService) {
		this.itemService = itemService;
	}

	public SeedLotImporter() {
		cachedContainerTypes = new Hashtable<String, ContainerType>();
		cachedItemTypes = new Hashtable<String, ItemType>();
		cachedItems = new Hashtable<String, Item>();
		cachedLocations = new Hashtable<String, Location>();
	}

	private void parse() throws IOException, ImportException {
		this.lots = new ArrayList<SeedLot>();
		List<String> errorMessages = new ArrayList<String>();

		Date dateLastModified = new Date();

		List<Object> row = null;
		while ((row = reader.readLine()) != null) {
			// find item type
			String cropName = (String) row.get(0);
			
			Log.info("1 CROP NAME: " + cropName);
			ItemType itemType = getCachedItemType(cropName);
			Log.info("2 CROP NAME: " + cropName);
			
			String prefix = (String) row.get(1), alternativeIdentifier = null;
			Long accessionIdentifier = (Long) row.get(2);
			if (row.get(3) != null && row.get(3).toString().length() > 0)
				alternativeIdentifier = (String) row.get(3);
			String itemName = alternativeIdentifier;
			if (prefix != null && accessionIdentifier != null)
				itemName = prefix + "-" + accessionIdentifier;

			Item item = getCachedItem(itemType, itemName, prefix, accessionIdentifier, alternativeIdentifier, dateLastModified);
			if (item == null) {
				throw new ImportException("Line " + reader.getCurrentLine() + ": Could not create item " + itemName + " of type " + itemType);
			}

			// Store
			String storeName = (String) row.get(9);
			Location location = null, parentLocation;
			try {
				location = dao.getLocation(storeName);
			} catch (javax.persistence.NonUniqueResultException e) {
				throw new ImportException("Duplicate location '" + storeName + "'. Validate your inventory database!", e);
			}
			parentLocation = location;
			if (location == null) {
				// if store is not found, exit immediately
				throw new ImportException("Line " + reader.getCurrentLine() + ": Store '" + storeName + "' not found.");
			}

			// Sublocation
			String sublocationName = (String) row.get(10);
			parentLocation = location;
			location = getCachedLocation(sublocationName, parentLocation, "shelf");
			if (location == null) {
				addError(errorMessages, "Line " + reader.getCurrentLine() + ": Location '" + sublocationName + "' not found in '" + storeName + "'.");
			}

			// Tray
			if (row.get(12) != null) {
				String trayName = "Tray " + (String) row.get(12);
				parentLocation = location;
				location = getCachedLocation(trayName, parentLocation, "tray");
				if (location == null)
					addError(errorMessages, "Line " + reader.getCurrentLine() + ": Location '" + trayName + "' not found in '" + sublocationName + "'.");
			}

			// number of containers, defaults to 1
			int containerCount = 1;
			if (row.get(14) != null)
				containerCount = Integer.parseInt((String) row.get(14));

			for (int container = containerCount; container > 0; container--) {
				try {
					// create the master lot information
					SeedLot lot = new SeedLot();
					lot.setLocation(location);
					lot.setItem(item);

					if (row.get(4) == null) {
						lot.setWeight(0.0d);
						lot.setQuantity(0.0d);
					} else {
						lot.setWeight((Double) row.get(4) / containerCount);
						lot.setQuantity(lot.getWeight());
					}
					lot.setScale("g");

					// seed count
					if (row.get(5) != null) {
						lot.setQuantity((Double) row.get(5));
						lot.setScale("seed");
					}

					if (row.get(6) != null) {
						lot.setWeight100((Double) row.get(6));
					}

					if (lot.getWeight100()==null && lot.getWeight()!=null && lot.getQuantity()!=null && lot.getQuantity().longValue()!=0.0d && lot.getScale().equals("seed")) {
						lot.setWeight100(lot.getWeight() * 100.0 / lot.getQuantity());
					}
					
					if (lot.getWeight()!=null && lot.getWeight100()!=null && lot.getWeight100().longValue()!=0 &&  lot.getScale().equals("g")) {
						lot.setQuantity((double) Math.round(lot.getWeight() / lot.getWeight100() * 100));
						lot.setScale("seed");
					}

					if (row.get(7) != null)
						lot.setGermination(0.01 * (Double) row.get(7));
					if (row.get(8) != null)
						lot.setMoistureContent(0.01 * (Double) row.get(8));

					// find container
					String containerName = (String) row.get(13);
					if (containerName != null) {
						ContainerType containerType = getCachedContainer(containerName);
						lot.setContainer(containerType);
					}

					lot.setFieldLocation((String) row.get(15));
					lot.setPlantingDate((Date) row.get(16));
					lot.setHarvestDate((Date) row.get(17));
					lot.setGerminationDate((Date) row.get(18));
					lot.setYearProcessed((Long) row.get(19));
					lot.setStorageType((String) row.get(20));
					lot.setSeedTested((Boolean) row.get(21));
					lot.setVirusFree((Boolean) row.get(22));
					
					if(row.size()>=24)//.get(29)!=null)
						lot.setReferenceCode((String) row.get(23));

					this.lots.add(lot);
				} catch (NullPointerException e) {
					addError(errorMessages, "Line " + reader.getCurrentLine() + ": Invalid NULL value: " + e.getMessage() + "  SeedLotImporter:"
							+ e.getStackTrace()[0].getLineNumber() + ".");
				}
			}
			// break if more than 25 errors occur
			if (errorMessages.size() > 25)
				break;
		}
		reader.close();

		StringBuilder errorMsg = new StringBuilder();
		for (String key : errorMessages)
			errorMsg.append(key + "\n");
		if (errorMsg.length() > 0)
			throw new ImportException(errorMsg.toString());
	}

	private void addError(List<String> errorMessages, String message) {
		if (!errorMessages.contains(message))
			errorMessages.add(message);
	}

	private ContainerType getCachedContainer(String containerName) throws ImportException {
		if (containerName == null)
			return null;
		if (cachedContainerTypes.containsKey(containerName))
			return cachedContainerTypes.get(containerName);
		else {
			ContainerType container = null;
			try {
				container = dao.getContainer(containerName);
			} catch (javax.persistence.NonUniqueResultException e) {
				throw new ImportException("Duplicate container '" + containerName + "' defined. Validate your inventory database!", e);
			}

			if (container == null) {
				container = new ContainerType();
				container.setName(containerName);
			}
			cachedContainerTypes.put(containerName, container);
			return container;
		}
	}

	private ItemType getCachedItemType(String cropName) throws ImportException {
		if (cropName == null)
			return null;
		
		if (cachedItemTypes.containsKey(cropName))
			return cachedItemTypes.get(cropName);
		else {
			ItemType itemType = null;
			try {
				itemType = dao.getItemType(cropName);
			} catch (NonUniqueResultException e) {
				throw new ImportException("Duplicate crop name '" + cropName + "' found in database. Please clean up.", e);
			}
			if (itemType == null) {
				//System.out.println("1 CROP NAME: " + cropName);
				throw new ImportException("TESTING Item type '" + cropName + "' not defined in Inventory system. Please define it before importing data.");
			}
			
			cachedItemTypes.put(cropName, itemType);
			return itemType;
		}
	}

	private Item getCachedItem(ItemType itemType, String itemName, String prefix, Long accessionIdentifier, String alternativeIdentifier,
			Date dateLastModified) throws ImportException {
		if (itemName == null || itemType == null)
			return null;
		// need to convert to lower case! BUG: duplicate item names when importing new lot data
		String key = (itemType + ":" + itemName).toLowerCase();
		if (cachedItems.containsKey(key))
			return cachedItems.get(key);
		else {
			Item item = null;
			try {
				item = (Item) itemService.find(itemType, itemName);
			} catch (NonUniqueResultException e) {
				throw new ImportException("Multiple items of '" + itemType.getName() + "' with name '" + itemName + "' found. Cannot continue.");
			}
			if (item == null) {
				item = new Item();
				item.setDateLastModified(dateLastModified);
				item.setItemType(itemType);
				item.setPrefix(prefix);
				item.setAccessionIdentifier(accessionIdentifier);
				if (alternativeIdentifier != null)
					item.setAlternativeIdentifier(alternativeIdentifier);
				item.setName(itemName);
			}

			cachedItems.put(key, item);
			return item;
		}
	}

	private Location getCachedLocation(String sublocationName, Location parentLocation, String locationType) throws ImportException {
		if (sublocationName == null)
			return null;
		String key = parentLocation.getId() + ":" + sublocationName;
		if (cachedLocations.containsKey(key))
			return cachedLocations.get(key);
		else {
			Location location = null;
			try {
				location = dao.getLocation(sublocationName, parentLocation);
			} catch (javax.persistence.NonUniqueResultException e) {
				throw new ImportException("Duplicate location '" + sublocationName + "' in parent '" + parentLocation.getPathNames() + "' [id="
						+ parentLocation.getId() + "]", e);
			}
			if (location == null) {
				location = new Location();
				location.setName(sublocationName);
				location.setLocationType(locationType);
				location.setParent(parentLocation);
			}
			cachedLocations.put(key, location);
			return location;
		}
	}

	public List<SeedLot> getLots() throws ImportException {
		if (this.lots == null)
			try {
				parse();
			} catch (IOException e) {
				throw new ImportException(e.getMessage(), e);
			}
		return this.lots;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.iita.inventory.importer.Importer#setFile(java.io.File)
	 */
	@Override
	public void setFile(File csvFile) throws FileNotFoundException {
		this.reader = new CSVDataReader(csvFile, true, ",", "\"");
		configureReader();
	}

	@Override
	public void setFile(File csvFile, Charset charset) throws FileNotFoundException {
		this.reader = new CSVDataReader(csvFile, charset, true, ",", "\"");
		configureReader();
	}

	/**
	 * 
	 */
	private void configureReader() {
		this.reader.setDateFormat(DateFormat.getDateInstance(DateFormat.SHORT, Locale.UK));
		this.reader.setColumn(2, DataType.Long);
		this.reader.setColumn(4, DataType.Double);
		this.reader.setColumn(5, DataType.Double);
		this.reader.setColumn(6, DataType.Double);
		this.reader.setColumn(7, DataType.Double);
		this.reader.setColumn(8, DataType.Double);
		this.reader.setColumn(16, DataType.Date);
		this.reader.setColumn(17, DataType.Date);
		this.reader.setColumn(18, DataType.Date);
		this.reader.setColumn(19, DataType.Long);
		this.reader.setColumn(21, DataType.Boolean);
		this.reader.setColumn(22, DataType.Boolean);
		this.reader.setColumn(29, DataType.Text);
	}

	@Override
	public String getErrors() {
		StringBuilder errorMsg = new StringBuilder();
		for (String key : this.reader.getWarnings())
			errorMsg.append(key + "\n");
		return errorMsg.toString();
	}
}
