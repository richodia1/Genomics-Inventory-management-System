/**
 * 
 */
package org.iita.inventory.importer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import org.iita.inventory.model.ContainerType;
import org.iita.inventory.model.InVitroItem;
import org.iita.inventory.model.InVitroLot;
import org.iita.inventory.model.ItemType;
import org.iita.inventory.model.Location;
import org.iita.inventory.service.DataAccessService;
import org.iita.inventory.service.ItemService;
import org.iita.util.CSVDataReader;
import org.iita.util.CSVDataReader.DataType;

/**
 * @author mobreza
 * 
 */
public class CassavaImporter implements Importer {
	private final static Pattern locationSplit = Pattern.compile("(\\d+).(\\d+)");
	private CSVDataReader reader;
	List<InVitroLot> lots = null;
	DataAccessService dao = null;
	private ItemService itemService;

	public CassavaImporter() {
	}

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

	private void parse() throws IOException {
		this.lots = new ArrayList<InVitroLot>();

		ItemType itemType = new ItemType();
		itemType.setName("Cassava");
		itemType.setId(5l);

		ContainerType tube = new ContainerType();
		tube.setId(14l);
		tube.setName("Tube");

		Location parentLocation = new Location();
		parentLocation.setName("Cassava collection");

		Date dateLastModified = new Date();

		List<Object> row = null;
		while ((row = reader.readLine()) != null) {

			InVitroItem item = new InVitroItem();
			item.setDateLastModified(dateLastModified);
			item.setItemType(itemType);
			item.setAccessionIdentifier((Long) row.get(3));
			item.setPrefix("TMe");
			item.setName(item.getPrefix() + "-" + item.getAccessionIdentifier());
			item.setIntroductionDate((Date) row.get(4));

			Location location = null;
			if (row.get(2) == null) {
				location = parentLocation;
			} else {
				String locationName = (String) row.get(2);
				String[] loc = locationName.split("\\.");
				if (loc.length == 2) {
					Location location1 = new Location();
					location1.setName("Rack " + loc[0]);
					location1.setLocationType("rack");
					location1.setParent(parentLocation);

					location = new Location();
					location.setName(locationName);
					location.setLocationType("rack");
					location.setParent(location1);
				} else
					location = parentLocation;
			}

			// create the master lot information
			InVitroLot lot = new InVitroLot();
			lot.setItem(item);
			lot.setLocation(location);
			lot.setContainer(tube);
			lot.setQuantity(0.0d);
			lot.setScale("tube");
			lot.setStatus(0);
			lot.setDateLastModified(dateLastModified);
			this.lots.add(lot);
		}
		reader.close();
	}

	public List<InVitroLot> getLots() {
		if (this.lots == null)
			try {
				parse();
			} catch (IOException e) {
				e.printStackTrace();
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
		this.reader.setDateFormat(DateFormat.getDateInstance(DateFormat.SHORT, Locale.UK));
		this.reader.setColumn(3, DataType.Long);
		this.reader.setColumn(4, DataType.Date);
	}

	@Override
	public String getErrors() {
		return null;
	}
}
