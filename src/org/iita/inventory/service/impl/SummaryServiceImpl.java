package org.iita.inventory.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.iita.inventory.model.FieldLot;
import org.iita.inventory.model.InVitroLot;
import org.iita.inventory.model.ItemType;
import org.iita.inventory.model.Location;
import org.iita.inventory.model.OtherLot;
import org.iita.inventory.model.SeedLot;
import org.iita.inventory.service.InventoryTrialService;
import org.iita.inventory.service.SummaryService;
import org.iita.service.impl.XLSExportService;
import org.iita.util.PagedResult;
import org.springframework.transaction.annotation.Transactional;

public class SummaryServiceImpl implements SummaryService {
	private static final String[] INVITRO_HEADINGS = new String[] { "Name", "Prefix", "Quantity", "Scale", "Container", "Store", "Location", "Line",
			"Subculture date", "Virus free", "Introduction date", "Origin Geo location", "Lot ID", "Barcode", "Reference Code", "Status", "Origin Explant" };
	private static final String[] INVITROLOT_EXPRESSIONS = new String[] { "root.item.name", "root.item.prefix", "root.quantity", "root.scale",
			"root.container.name", "location.parent.name", "location.name", "root.line", "root.regenerationDate", "root.virusFree", "root.introductionDate",
			"root.origin", "root.id", "barCode.id", "root.referenceCode", "root.invitroStatus", "root.originExplant" };
	private static final String[] FIELD_HEADINGS = new String[] { "Name", "Prefix", "Quantity", "Scale", "Store", "Location", "Planting date", "Latitude",
			"Longitude", "Geopos date", "Lot ID", "Barcode", "Line", "Reference Code" };
	private static final String[] FIELD_EXPRESSIONS = new String[] { "root.item.name", "root.item.prefix", "root.quantity", "root.scale",
			"location.parent.name", "location.name", "root.plantingDate", "root.latitude", "root.longitude", "root.geoposDate", "root.id", "barCode.id", "root.line", "root.referenceCode" };
	private static final String[] OTHER_HEADINGS = new String[] { "Name", "Prefix", "Quantity", "Scale", "Store", "Location", "Lot ID", "Barcode", "Line", "Reference Code" };
	private static final String[] OTHER_EXPRESSIONS = new String[] { "root.item.name", "root.item.prefix", "root.quantity", "root.scale",
			"location.parent.name", "location.name", "root.id", "barCode.id", "root.line", "root.referenceCode" };
	private static final String[] SEEDLOT_HEADINGS = new String[] { "Name", "Prefix", "Quantity", "Scale", "Container", "Location in the store",
			"Location on shelve", "Regeneration location", "Germination %", "Germination date", "Harvest date", "Moisture content %FWB", "Planting date",
			"Seed Tested", "Storage type", "Virus free", "Weight", "100 Seed weight", "Year processed", "Lot ID", "Barcode", "Conformity", "Reference Code", "Line" };
	private static final String[] SEEDLOT_EXPRESSIONS = new String[] { "root.item.name", "root.item.prefix", "root.quantity", "root.scale",
			"root.container.name", "location.parent.name", "location.name", "root.fieldLocation", "root.germination", "root.germinationDate",
			"root.harvestDate", "root.moistureContent", "root.plantingDate", "root.seedTested", "root.storageType", "root.virusFree", "root.weight",
			"root.weight100", "root.yearProcessed", "root.id", "barCode.id", "root.conformity", "root.referenceCode", "root.line" };
	
	private static final String[] INVITRO_HEADINGS_FV = new String[] { "Name", "Prefix", "Quantity", "Scale", "Container", "Store", "Location", "Line",
		"Subculture date", "Virus free", "Introduction date", "Origin Geo location", "Lot ID", "Barcode", "FieldVar", "FieldVarValue", "FieldVarDate", "Reference Code", "Status", "Origin Explant" };
	private static final String[] INVITROLOT_EXPRESSIONS_FV = new String[] { "root.item.name", "root.item.prefix", "root.quantity", "root.scale",
			"root.container.name", "location.parent.name", "location.name", "root.line", "root.regenerationDate", "root.virusFree", "root.introductionDate",
			"root.origin", "root.id", "barCode.id", "fv.var", "fv.qty", "fv.date", "root.referenceCode", "root.invitroStatus", "root.originExplant" };
	private static final String[] FIELD_HEADINGS_FV = new String[] { "Name", "Prefix", "Quantity", "Scale", "Store", "Location", "Planting date", "Latitude",
			"Longitude", "Geopos date", "Lot ID", "Barcode", "Line", "FieldVar", "FieldVarValue", "FieldVarDate", "Reference Code" };
	private static final String[] FIELD_EXPRESSIONS_FV = new String[] { "root.item.name", "root.item.prefix", "root.quantity", "root.scale",
			"location.parent.name", "location.name", "root.plantingDate", "root.latitude", "root.longitude", "root.geoposDate", "root.id", "barCode.id", "root.line", "fv.var", "fv.qty", "fv.date", "root.referenceCode" };
	private static final String[] OTHER_HEADINGS_FV = new String[] { "Name", "Prefix", "Quantity", "Scale", "Store", "Location", "Lot ID", "Barcode", "Line", "FieldVar", "FieldVarValue", "FieldVarDate", "Reference Code" };
	private static final String[] OTHER_EXPRESSIONS_FV = new String[] { "root.item.name", "root.item.prefix", "root.quantity", "root.scale",
			"location.parent.name", "location.name", "root.id", "barCode.id", "root.line", "fv.var", "fv.qty", "fv.date", "root.referenceCode" };
	private static final String[] SEEDLOT_HEADINGS_FV = new String[] { "Name", "Prefix", "Quantity", "Scale", "Container", "Location in the store",
			"Location on shelve", "Regeneration location", "Germination %", "Germination date", "Harvest date", "Moisture content %FWB", "Planting date",
			"Seed Tested", "Storage type", "Virus free", "Weight", "100 Seed weight", "Year processed", "Lot ID", "Barcode", "Conformity", "Reference Code", "Line", "FieldVar", "FieldVarValue", "FieldVarDate" };
	private static final String[] SEEDLOT_EXPRESSIONS_FV = new String[] { "root.item.name", "root.item.prefix", "root.quantity", "root.scale",
			"root.container.name", "location.parent.name", "location.name", "root.fieldLocation", "root.germination", "root.germinationDate",
			"root.harvestDate", "root.moistureContent", "root.plantingDate", "root.seedTested", "root.storageType", "root.virusFree", "root.weight",
			"root.weight100", "root.yearProcessed", "root.id", "barCode.id", "root.conformity", "root.referenceCode", "root.line", "fv.var", "fv.qty", "fv.date" };
	
	private static final String[] HEADINGS = new String[] { "Name", "Scale", "Lots", "Quantity" };
	private static final String[] EXPRESSIONS = new String[] { "name", "prefix", "count", "sum" };
	
	private EntityManager entityManager;
	private static final Log LOG = LogFactory.getLog(SummaryService.class);
	private InventoryTrialService inventoryTrialService;

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	/**
	 * @param inventoryTrialService the inventoryTrialService to set
	 */
	public void setInventoryTrialService(InventoryTrialService inventoryTrialService) {
		this.inventoryTrialService = inventoryTrialService;
	}

	@Override
	@Transactional(readOnly = true)
	public PagedResult<List<Object>> summarizeByItemType(Location location, ItemType itemType, String scale, int startAt, int maxRecords) {
		PagedResult<List<Object>> result = new PagedResult<List<Object>>(startAt, maxRecords);

		Query query;
		//		= this.entityManager
		//				.createQuery(
		//						"select l.item.itemType, l.scale, count(*), sum(l.quantity) from Lot l where l.status=1"
		//								+ (itemType == null ? "" : " and l.item.itemType=:itemType")
		//								+ (scale == null ? "" : " and l.scale=:scale")
		//								+ " and (l.location=:parent or l.location in (select pk.child from LocationFlat lf where lf.pk.parent=:parent)) group by l.item.itemType, l.scale order by sum(l.quantity) desc")
		//				.setFirstResult(startAt).setMaxResults(maxRecords);
		
		String queryTxt="select itype.name, X.scale, X.count, X.sum from (select item.itemType, lot.scale, count(lot.id) count, sum(lot.quantity) sum "
			+ (location == null ? "from Lot lot "
					: "from (select :location id union select lf.id from LocationFlat lf where lf.parentId=:location) LOC "
							+ "inner join Lot lot on lot.location=LOC.id ")
			+ "inner join Item item on item.id=lot.item "
			+ "where lot.status=1 "
			+ (scale == null ? "" : " and lot.scale=:scale ")
			+ "group by item.itemType, lot.scale) X  "
			+ "inner join ItemType itype on itype.id=X.itemType "
			+ (itemType == null ? "" : " where itype.id=:itemType ")
			+ "order by itype.name, X.sum desc";

		LOG.info("Querying: " + queryTxt);
		
		query = this.entityManager
				.createNativeQuery(queryTxt);
		
		if (location != null)
			query.setParameter("location", location.getId());
		if (itemType != null)
			query.setParameter("itemType", itemType.getId());
		if (scale != null)
			query.setParameter("scale", scale);
		
		result.setResults(query.getResultList());

		result.setTotalHits(result.getResults().size());

		return result;
	}

	/**
	 * Summarize lot information by location, scale and item
	 */
	@Override
	@Transactional(readOnly = true)
	public PagedResult<List<Object>> summarizeByItem(Location location, ItemType itemType, String scale, int startAt, int maxRecords) {
		PagedResult<List<Object>> result = new PagedResult<List<Object>>(startAt, maxRecords);

		Query query = this.entityManager
				.createQuery(
						"select l.item, l.scale, count(*), sum(l.quantity) from Lot l where l.status=1"
								+ (itemType == null ? "" : " and l.item.itemType=:itemType")
								+ (scale == null ? "" : " and l.scale=:scale")
								+ " and (l.location=:parent or l.location in (select pk.child from LocationFlat lf where lf.pk.parent=:parent)) group by l.item, l.scale order by sum(l.quantity) desc")
				.setFirstResult(startAt).setMaxResults(maxRecords);
		query.setParameter("parent", location);
		if (itemType != null)
			query.setParameter("itemType", itemType);
		if (scale != null)
			query.setParameter("scale", scale);
		result.setResults(query.getResultList());

		query = this.entityManager.createNativeQuery("select count(*) from (select l.* from Lot l inner join Item I on l.item=I.id where l.status=1 "
				+ (itemType == null ? "" : " and I.itemType=:itemType") + (scale == null ? "" : " and l.scale=:scale")
				+ " and (l.location=:parent or l.location in (select id from LocationFlat lf where lf.parentId=:parent)) group by l.item, l.scale) X");
		query.setParameter("parent", location == null ? null : location.getId());
		if (itemType != null)
			query.setParameter("itemType", itemType.getId());
		if (scale != null)
			query.setParameter("scale", scale);

		result.setTotalHits(((BigInteger) query.getSingleResult()).longValue());

		return result;
	}

	/**
	 * @throws IOException
	 * @see org.iita.inventory.service.SummaryService#summarizeToXLS(org.iita.inventory.model.Location, org.iita.inventory.model.ItemType, java.lang.String)
	 */
	@Override
	@Transactional(readOnly = true)
	public File summarizeToXLS(Location location, ItemType itemType, String scale) throws IOException {
		if (location == null) {
			throw new RuntimeException("Please select location before downloading XLS file!");
		}
		HSSFWorkbook wb = new HSSFWorkbook();

		HSSFSheet seedSheet = wb.createSheet("SEED " + location.getPathNames());
		HSSFSheet invitroSheet = wb.createSheet("INVITRO " + location.getPathNames());
		HSSFSheet fieldSheet = wb.createSheet("FIELD " + location.getPathNames());
		HSSFSheet otherSheet = wb.createSheet("OTHER " + location.getPathNames());

		if (exportRowsToSheet(location, itemType, scale, seedSheet, SeedLot.class, SEEDLOT_HEADINGS, SEEDLOT_EXPRESSIONS) == 0) {
			// remove sheet
			LOG.debug("Removing seed sheet as it is empty");
			wb.removeSheetAt(wb.getSheetIndex(seedSheet));
		}
		if (exportRowsToSheet(location, itemType, scale, invitroSheet, InVitroLot.class, INVITRO_HEADINGS, INVITROLOT_EXPRESSIONS) == 0) {
			// remove sheet
			LOG.debug("Removing invitro sheet as it is empty");
			wb.removeSheetAt(wb.getSheetIndex(invitroSheet));
		}
		if (exportRowsToSheet(location, itemType, scale, fieldSheet, FieldLot.class, FIELD_HEADINGS, FIELD_EXPRESSIONS) == 0) {
			// remove sheet
			LOG.debug("Removing field sheet as it is empty");
			wb.removeSheetAt(wb.getSheetIndex(fieldSheet));
		}
		if (exportRowsToSheet(location, itemType, scale, otherSheet, OtherLot.class, OTHER_HEADINGS, OTHER_EXPRESSIONS) == 0) {
			// remove sheet
			LOG.debug("Removing other sheet as it is empty");
			wb.removeSheetAt(wb.getSheetIndex(otherSheet));
		}

		File tempFile;
		tempFile = java.io.File.createTempFile("inv-", ".xls");

		FileOutputStream fos = new FileOutputStream(tempFile);
		wb.write(fos);
		fos.close();

		return tempFile;
	}
	
	/**
	 * @throws IOException
	 * @see org.iita.inventory.service.SummaryService#summarizeToSummaryXLS(org.iita.inventory.model.Location, org.iita.inventory.model.ItemType, java.lang.String)
	 */
	@Override
	@Transactional(readOnly = true)
	public File summarizeToSummaryXLS(Location location, ItemType itemType, String scale, int type) throws IOException {
		if (location == null) {
			throw new RuntimeException("Please select location before downloading XLS file!");
		}
		HSSFWorkbook wb = new HSSFWorkbook();

		HSSFSheet seedSheet = wb.createSheet("SUMMARY " + location.getPathNames());
	
		if (exportRowsToSheet(location, itemType, scale, seedSheet, HEADINGS, EXPRESSIONS, type) == 0) {
			// remove sheet
			LOG.debug("Removing seed sheet as it is empty");
			wb.removeSheetAt(wb.getSheetIndex(seedSheet));
		}

		File tempFile;
		tempFile = java.io.File.createTempFile("inv-", ".xls");

		FileOutputStream fos = new FileOutputStream(tempFile);
		wb.write(fos);
		fos.close();

		return tempFile;
	}

	/**
	 * @throws IOException
	 * @see org.iita.inventory.service.SummaryService#summarizeToXLS(java.util.List)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public File summarizeToXLS(List<Long> selectedLots) throws IOException {
		HSSFWorkbook wb = new HSSFWorkbook();

		HSSFSheet seedSheet = wb.createSheet("SEED Selection");
		HSSFSheet invitroSheet = wb.createSheet("INVITRO Selection");
		HSSFSheet fieldSheet = wb.createSheet("FIELD Selection");
		HSSFSheet otherSheet = wb.createSheet("OTHER Selection");

		// extras
		List<String> traitVars = new ArrayList<String>();
		traitVars.add("VIABILITY");
		String[] traitHeadings = new String[] { "Date for last viability test", "Viability % at last test" };
		Hashtable<Long, Object[]> traitLastValuesWide = inventoryTrialService.getTraitLastValuesWide(selectedLots, traitVars);

		// seed
		Query query = this.entityManager.createQuery("select distinct " + getProperties(SEEDLOT_EXPRESSIONS, SeedLot.class) + " where root.id in (:selectedLots) ");
		XLSExportService.fillSheet(seedSheet, SEEDLOT_HEADINGS, query.setParameter("selectedLots", selectedLots).setMaxResults(0xFFFE).getResultList());
		XLSExportService.expandSheet(seedSheet, traitHeadings, "Lot ID", traitLastValuesWide);
		if (seedSheet.getLastRowNum() <= 1) {
			// remove sheet
			LOG.debug("Removing seed sheet as it is empty");
			wb.removeSheetAt(wb.getSheetIndex(seedSheet));
		}

		query = this.entityManager.createQuery("select distinct " + getProperties(INVITROLOT_EXPRESSIONS, InVitroLot.class) + " where root.id in (:selectedLots)");
		XLSExportService.fillSheet(invitroSheet, INVITRO_HEADINGS, query.setParameter("selectedLots", selectedLots).setMaxResults(0xFFFE).getResultList());
		XLSExportService.expandSheet(invitroSheet, traitHeadings, "Lot ID", traitLastValuesWide);
		if (invitroSheet.getLastRowNum() <= 1) {
			// remove sheet
			LOG.debug("Removing invitro sheet as it is empty");
			wb.removeSheetAt(wb.getSheetIndex(invitroSheet));
		}

		query = this.entityManager.createQuery("select distinct " + getProperties(FIELD_EXPRESSIONS, FieldLot.class) + " where root.id in (:selectedLots) ");
		XLSExportService.fillSheet(fieldSheet, FIELD_HEADINGS, query.setParameter("selectedLots", selectedLots).setMaxResults(0xFFFE).getResultList());
		XLSExportService.expandSheet(fieldSheet, traitHeadings, "Lot ID", traitLastValuesWide);
		if (fieldSheet.getLastRowNum() <= 1) {
			// remove sheet
			LOG.debug("Removing field sheet as it is empty");
			wb.removeSheetAt(wb.getSheetIndex(fieldSheet));
		}

		query = this.entityManager.createQuery("select distinct " + getProperties(OTHER_EXPRESSIONS, OtherLot.class) + " where root.id in (:selectedLots) ");
		XLSExportService.fillSheet(otherSheet, OTHER_HEADINGS, query.setParameter("selectedLots", selectedLots).setMaxResults(0xFFFE).getResultList());
		XLSExportService.expandSheet(otherSheet, traitHeadings, "Lot ID", traitLastValuesWide);
		if (otherSheet.getLastRowNum() <= 1) {
			// remove sheet
			LOG.debug("Removing other sheet as it is empty");
			wb.removeSheetAt(wb.getSheetIndex(otherSheet));
		}

		File tempFile = java.io.File.createTempFile("inv-", ".xls");
		FileOutputStream fos = new FileOutputStream(tempFile);
		wb.write(fos);
		fos.close();

		return tempFile;
	}
	
	/**
	 * @throws IOException
	 * @see org.iita.inventory.service.SummaryService#summarizeToXLSWithFieldVariables(java.util.List)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public File summarizeToXLSWithFieldVariables(List<Long> selectedLots) throws IOException {
		HSSFWorkbook wb = new HSSFWorkbook();

		HSSFSheet seedSheet = wb.createSheet("SEED Selection");
		HSSFSheet invitroSheet = wb.createSheet("INVITRO Selection");
		HSSFSheet fieldSheet = wb.createSheet("FIELD Selection");
		HSSFSheet otherSheet = wb.createSheet("OTHER Selection");

		// extras
		List<String> traitVars = new ArrayList<String>();
		traitVars.add("VIABILITY");
		String[] traitHeadings = new String[] { "Date for last viability test", "Viability % at last test" };
		Hashtable<Long, Object[]> traitLastValuesWide = inventoryTrialService.getTraitLastValuesWide(selectedLots, traitVars);

		// seed
		Query query = this.entityManager.createQuery("select distinct " + getPropertiesFV(SEEDLOT_EXPRESSIONS_FV, SeedLot.class) + " where (root.id in (:selectedLots) and fv.lot is null) or (fv.lot in (:selectedLots) and root.id in (:selectedLots))");
		XLSExportService.fillSheet(seedSheet, SEEDLOT_HEADINGS_FV, query.setParameter("selectedLots", selectedLots).setMaxResults(0xFFFE).getResultList());
		XLSExportService.expandSheet(seedSheet, traitHeadings, "Lot ID", traitLastValuesWide);
		if (seedSheet.getLastRowNum() <= 1) {
			// remove sheet
			LOG.debug("Removing seed sheet as it is empty");
			wb.removeSheetAt(wb.getSheetIndex(seedSheet));
		}

		query = this.entityManager.createQuery("select distinct " + getPropertiesFV(INVITROLOT_EXPRESSIONS_FV, InVitroLot.class) + " where (root.id in (:selectedLots) and fv.lot is null) or (fv.lot in (:selectedLots) and root.id in (:selectedLots))");
		XLSExportService.fillSheet(invitroSheet, INVITRO_HEADINGS_FV, query.setParameter("selectedLots", selectedLots).setMaxResults(0xFFFE).getResultList());
		XLSExportService.expandSheet(invitroSheet, traitHeadings, "Lot ID", traitLastValuesWide);
		if (invitroSheet.getLastRowNum() <= 1) {
			// remove sheet
			LOG.debug("Removing invitro sheet as it is empty");
			wb.removeSheetAt(wb.getSheetIndex(invitroSheet));
		}

		query = this.entityManager.createQuery("select distinct " + getPropertiesFV(FIELD_EXPRESSIONS_FV, FieldLot.class) + " where (root.id in (:selectedLots) and fv.lot is null) or (fv.lot in (:selectedLots) and root.id in (:selectedLots))");
		XLSExportService.fillSheet(fieldSheet, FIELD_HEADINGS_FV, query.setParameter("selectedLots", selectedLots).setMaxResults(0xFFFE).getResultList());
		XLSExportService.expandSheet(fieldSheet, traitHeadings, "Lot ID", traitLastValuesWide);
		if (fieldSheet.getLastRowNum() <= 1) {
			// remove sheet
			LOG.debug("Removing field sheet as it is empty");
			wb.removeSheetAt(wb.getSheetIndex(fieldSheet));
		}

		query = this.entityManager.createQuery("select distinct " + getPropertiesFV(OTHER_EXPRESSIONS_FV, OtherLot.class) + " where (root.id in (:selectedLots) and fv.lot is null) or (fv.lot in (:selectedLots) and root.id in (:selectedLots))");
		XLSExportService.fillSheet(otherSheet, OTHER_HEADINGS_FV, query.setParameter("selectedLots", selectedLots).setMaxResults(0xFFFE).getResultList());
		XLSExportService.expandSheet(otherSheet, traitHeadings, "Lot ID", traitLastValuesWide);
		if (otherSheet.getLastRowNum() <= 1) {
			// remove sheet
			LOG.debug("Removing other sheet as it is empty");
			wb.removeSheetAt(wb.getSheetIndex(otherSheet));
		}

		File tempFile = java.io.File.createTempFile("inv-", ".xls");
		FileOutputStream fos = new FileOutputStream(tempFile);
		wb.write(fos);
		fos.close();

		return tempFile;
	}
	
	/**
	 * @param location
	 * @param itemType
	 * @param scale
	 * @param conn
	 * @param styles
	 * @param sheet
	 * @param lotClass
	 * @param headings
	 * @param expressions
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	private int exportRowsToSheet(Location location, ItemType itemType, String scale, HSSFSheet sheet, Class<?> lotClass, String[] headings,
			String[] expressions) throws IOException {
		
		LOG.info("LOCATION: " + location);
		LOG.info("SCALE: " + scale);
		LOG.info("ITEMTYPE: " + itemType);
		Query query = this.entityManager
				.createQuery(
						"select "
								+ getProperties(expressions, lotClass)
								+ " where (root.status IS NULL or root.status=1 or root.status=0) and (root.location=:location or root.location in (select pk.child from LocationFlat locationfl2 where locationfl2.pk.parent=:location)) "
								+ (itemType == null ? "" : " and root.item.itemType=:itemType") + (scale == null ? "" : " and root.scale=:scale") + "")
				.setParameter("location", location).setMaxResults(0xFFFE);
		
		if (itemType != null)
			query.setParameter("itemType", itemType);
		if (scale != null)
			query.setParameter("scale", scale);
		
		List<Object[]> lots = query.getResultList();
		XLSExportService.fillSheet(sheet, headings, lots);

		// extras
		List<String> traitVars = new ArrayList<String>();
		traitVars.add("VIABILITY");
		String[] traitHeadings = new String[] { "Last Viability date", "Last Viability" };

		// get IDs
		int rootIdColumn = -1;
		for (int i = 0; i < expressions.length; i++) {
			if (expressions[i].equalsIgnoreCase("root.id"))
				rootIdColumn = i;
		}
		LOG.debug("Getting list of Lot IDs from column " + rootIdColumn);
		int batchSize = 1000;
		for (int i = 0; i < lots.size(); i += batchSize) {
			List<Long> selectedLots = new ArrayList<Long>();
			for (int j = 0; j < batchSize && j + i < lots.size(); j++)
				selectedLots.add((Long) lots.get(i + j)[rootIdColumn]);

			LOG.debug("Loading traits values for " + selectedLots.size() + " entities");
			Hashtable<Long, Object[]> traitLastValuesWide = inventoryTrialService.getTraitLastValuesWide(selectedLots, traitVars);
			LOG.debug("Expanding sheet with " + traitLastValuesWide.size() + " extra rows");
			if (i == 0)
				XLSExportService.expandSheet(sheet, traitHeadings, "Lot ID", traitLastValuesWide);
			else
				XLSExportService.expandSheet(sheet, null, "Lot ID", traitLastValuesWide);
		}
		LOG.debug("Done");

		return lots.size();
	}
	
	/**
	 * @param location
	 * @param itemType
	 * @param scale
	 * @param conn
	 * @param styles
	 * @param sheet
	 * @param lotClass
	 * @param headings
	 * @param expressions
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	private int exportRowsToSheet(Location location, ItemType itemType, String scale, HSSFSheet sheet, String[] headings,
			String[] expressions, int type) throws IOException {

		Query query;
		List<Object[]> lots;
		
		
		
		if (type == 0){
			String queryTxt="select itype.name, X.scale, X.count, cast(X.sum AS DECIMAL(18,2)) from (select item.itemType, lot.scale, count(lot.id) count, sum(lot.quantity) sum "
			+ (location == null ? "from Lot lot "
					: "from (select :location id union select lf.id from LocationFlat lf where lf.parentId=:location) LOC "
							+ "inner join Lot lot on lot.location=LOC.id ")
			+ "inner join Item item on item.id=lot.item "
			+ "where (lot.status IS NULL or lot.status=0 or lot.status=1) "
			+ (scale == null ? "" : " and lot.scale=:scale ")
			+ "group by item.itemType, lot.scale) X  "
			+ "inner join ItemType itype on itype.id=X.itemType "
			+ (itemType == null ? "" : " where itype.id=:itemType ")
			+ "order by itype.name, X.sum desc";
			
			LOG.info("TEST 1: " + queryTxt);
			
			query = this.entityManager.createNativeQuery(queryTxt);
			
			if (location != null)
				query.setParameter("location", location.getId());
			if (itemType != null)
				query.setParameter("itemType", itemType.getId());
			if (scale != null)
				query.setParameter("scale", scale);
			
			LOG.info("TEST 2: " + queryTxt);
			
			
			lots = query.getResultList();
		}else{
			query = this.entityManager
			.createQuery(
					"select l.item, l.scale, count(*), cast(sum(l.quantity) AS DECIMAL(18,2)) from Lot l where l.status=1"
							+ (itemType == null ? "" : " and l.item.itemType=:itemType")
							+ (scale == null ? "" : " and l.scale=:scale")
							+ " and (l.location=:parent or l.location in (select pk.child from LocationFlat lf where lf.pk.parent=:parent)) group by l.item, l.scale order by sum(l.quantity) desc")
							.setMaxResults(0xFFFE);
			query.setParameter("parent", location);
			if (itemType != null)
				query.setParameter("itemType", itemType);
			if (scale != null)
				query.setParameter("scale", scale);
			
			lots = query.getResultList();
		}
		
		/*@SuppressWarnings("rawtypes")
		List<Object[]> lotsArrays = new ArrayList();
				
		for (Object[] obj : lots){
			@SuppressWarnings("rawtypes")
			ArrayList lotitems =  new ArrayList();
			
			if(type==0)
				lotitems.add(obj[0] + "--" + obj[1] + "--" + obj[2] + "--" + obj[3] + "--" + obj[0]);
			else{
				Item item = (Item) obj[0];
				lotitems.add(obj[0] + "--" + obj[1] + "--" + obj[2] + "--" + obj[3] + "--" + item.getName());
			}
			
			lotitems.add(obj[2]);
			lotitems.add(obj[3]);
			lotsArrays.addAll(lotitems);
		}*/
		
		
		XLSExportService.fillSheet(sheet, headings, lots);

		LOG.info("SUMMARY DOWNLOAD Done");

		return lots.size();
	}

	/**
	 * @param expressions
	 * @param lotClass
	 * @return
	 */
	private String getProperties(String[] expressions, Class<?> lotClass) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < expressions.length; i++) {
			if (i > 0)
				sb.append(", ");
			sb.append(expressions[i]);
		}
		sb.append(" from ").append(lotClass.getName() + " root left outer join root.barCode as barCode left outer join root.location as location");
		LOG.warn("My query: " + sb.toString());
		LOG.info("My query: " + sb.toString());
		return sb.toString();
	}

	/**
	 * @param expressions
	 * @param lotClass
	 * @return
	 */
	private String getPropertiesFV(String[] expressions, Class<?> lotClass) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < expressions.length; i++) {
			if (i > 0)
				sb.append(", ");
			sb.append(expressions[i]);
		}
		sb.append(" from ").append(lotClass.getName() + " root left outer join root.barCode as barCode left outer join root.location as location, FieldVariables as fv");
		LOG.warn("My query: " + sb.toString());
		return sb.toString();
	}

}