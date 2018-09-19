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
public class MusaImporter implements Importer {
	private final static Pattern locationSplit = Pattern.compile("(\\d+).(\\d+)");
	private CSVDataReader reader;
	List<InVitroLot> lots = null;
	DataAccessService dao = null;
	private ItemService itemService;

	public MusaImporter() {
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
		itemType.setName("Musa");
		itemType.setId(2l);

		ContainerType tube = new ContainerType();
		tube.setId(14l);
		tube.setName("Tube");

		Location parentLocation = new Location();
		parentLocation.setName("Musa collection");

		Date dateLastModified = new Date();

		List<Object> row = null;
		while ((row = reader.readLine()) != null) {

			InVitroItem item = new InVitroItem();
			item.setDateLastModified(dateLastModified);
			item.setItemType(itemType);
			item.setAccessionIdentifier((Long) row.get(0));
			item.setPrefix((String) row.get(1));
			item.setAlternativeIdentifier((String) row.get(2));
			if (item.getAccessionIdentifier() == null)
				item.setName(item.getAlternativeIdentifier());
			else
				item.setName(item.getPrefix() + "-" + item.getAccessionIdentifier());
			item.setIntroductionDate((Date) row.get(3));

			Location location = null;
			if (row.get(4) == null) {
				location = parentLocation;
			} else {
				String locationName = (String) row.get(4);
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
			lot.setQuantity((Double) row.get(9));
			lot.setScale("tube");
			lot.setStatus(0);
			lot.setDateLastModified(dateLastModified);
			this.lots.add(lot);

			// we have some regeneration info
			if (row.get(14) != null) {
				lot = new InVitroLot();
				lot.setItem(item);
				lot.setLocation(location);
				lot.setContainer(tube);
				lot.setQuantity(0.0);
				lot.setScale("tube");
				lot.setStatus(0);
				lot.setDateLastModified(dateLastModified);
				lot.setRegenerationDate((Date) row.get(14));
				this.lots.add(lot);
			}
			// we have some second regeneration info
			if (row.get(17) != null) {
				lot = new InVitroLot();
				lot.setItem(item);
				lot.setLocation(location);
				lot.setContainer(tube);
				lot.setQuantity(0.0);
				lot.setScale("tube");
				lot.setStatus(0);
				lot.setDateLastModified(dateLastModified);
				lot.setRegenerationDate((Date) row.get(17));
				this.lots.add(lot);
			}
		}
		reader.close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.iita.inventory.importer.Importer#getLots()
	 */
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
		this.reader.setColumn(0, DataType.Long);
		this.reader.setColumn(3, DataType.Date);
		this.reader.setColumn(9, DataType.Double);
		this.reader.setColumn(14, DataType.Date);
		this.reader.setColumn(17, DataType.Date);
		this.reader.setColumn(22, DataType.Double);
		this.reader.setColumn(23, DataType.Date);
		this.reader.setColumn(24, DataType.Double);
		this.reader.setColumn(25, DataType.Date);
	}

	@Override
	public String getErrors() {
		return null;
	}
}
