/**
 * inventory.Struts Jul 10, 2010
 */
package org.iita.inventory.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.iita.inventory.model.Item;
import org.iita.inventory.model.Lot;
import org.iita.inventory.model.LotTraitLastValue;
import org.iita.inventory.model.LotTraitValue;
import org.iita.inventory.model.LotTrial;
import org.iita.inventory.service.InventoryTrialService;
import org.iita.service.impl.XLSExportService;
import org.iita.trial.model.Trait;
import org.iita.trial.service.TrialException;
import org.iita.trial.service.TrialServiceImpl;
import org.iita.util.StringUtil;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author mobreza
 */
public class InventoryTrialServiceImpl extends TrialServiceImpl<Lot, LotTrial, LotTraitValue, LotTraitLastValue> implements InventoryTrialService {
	private static final Log LOG = LogFactory.getLog(InventoryTrialServiceImpl.class);

	/**
	 * @see org.iita.trial.service.TrialService#findTrial(java.lang.Long)
	 */
	@Override
	@Transactional(readOnly = true)
	public LotTrial findTrial(Long id) {
		return this.entityManager.find(LotTrial.class, id);
	}

	/**
	 * @see org.iita.trial.service.TrialServiceImpl#getEntityClass()
	 */
	@Override
	public Class<Lot> getEntityClass() {
		return Lot.class;
	}

	/**
	 * @see org.iita.trial.service.TrialServiceImpl#getTraitValueClass()
	 */
	@Override
	protected Class<LotTraitValue> getTraitValueClass() {
		return LotTraitValue.class;
	}

	/**
	 * @see org.iita.trial.service.TrialServiceImpl#getTrialClass()
	 */
	@Override
	protected Class<LotTrial> getTrialClass() {
		return LotTrial.class;
	}

	/**
	 * Generate Excel file with Trial data
	 * 
	 * @param trial
	 * @return
	 * @throws IOException
	 */
	@Override
	@Transactional(readOnly = true)
	public File generateXLS(LotTrial trial) throws IOException {
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(StringUtil.sanitizeHSSFSheetName(trial.getName()));

		List<Object[]> wideData = getWideData(trial);
		String[] headings;
		Object[] headerRow = wideData.get(0);
		headings = new String[headerRow.length + 2];
		headings[0] = "Item";
		headings[1] = "Lot ID";
		headings[2] = "Barcode";

		for (int i = 1; i < headerRow.length; i++) {
			Trait trait = (Trait) headerRow[i];
			headings[i + 2] = trait.getTitle();
		}

		List<Object[]> data = new ArrayList<Object[]>();
		for (int i = 1; i < wideData.size(); i++) {
			Object[] rowData = wideData.get(i);
			rowData[0] = this.entityManager.find(getEntityClass(), (Long) rowData[0]);
			Object[] row = new Object[headings.length];
			data.add(row);
			Lot lot = (Lot) rowData[0];
			row[0] = lot.getItem().getName();
			row[1] = lot.getId();
			if (lot.getBarCode() != null)
				row[2] = lot.getBarCode().getId();
			for (int j = 1; j < rowData.length; j++) {
				row[j + 2] = rowData[j];
			}
		}
		XLSExportService.fillSheet(sheet, headings, data);

		File tempFile = File.createTempFile("inv-", ".xls");
		FileOutputStream fos = new FileOutputStream(tempFile);
		wb.write(fos);
		fos.close();

		return tempFile;
	}

	/**
	 * @see org.iita.trial.service.TrialServiceImpl#updateTrialBySheet(org.iita.trial.model.Trial, org.apache.poi.hssf.usermodel.HSSFSheet)
	 */
	@Override
	protected void updateTrialBySheet(LotTrial trial, HSSFSheet sheet) throws TrialException {
		// this type of sheet has first column with item name, second with lot id, third with barcode, then traits
		int STARTING_COLUMN = 3;
		HSSFRow headerRow = sheet.getRow(0);
		if (headerRow == null) {
			LOG.warn("XLS sheet " + sheet.getSheetName() + " doesn't have a header row.");
			return;
		}

		List<Trait> usedTraits = this.getUsedTraits(trial);
		LOG.debug("Sheet has " + headerRow.getLastCellNum() + " cells in header row");
		for (int i = STARTING_COLUMN; i < headerRow.getLastCellNum(); i++) {
			String headerText = headerRow.getCell(i).getStringCellValue();
			LOG.debug("Header #" + i + ": " + headerText);
			if (headerText.equalsIgnoreCase(usedTraits.get(i - STARTING_COLUMN).getTitle())) {
				LOG.debug("Header matches used trait " + usedTraits.get(i - 3));
			} else {
				throw new TrialException("Header \"" + headerText + "\" does not match trait title \"" + usedTraits.get(i - STARTING_COLUMN) + "\".");
			}
		}
		int traitsUsed = headerRow.getLastCellNum() - STARTING_COLUMN;

		List<Lot> trialedLots = getUsedEntities(trial);

		// read sheet values
		for (int i = 1; i <= sheet.getLastRowNum(); i++) {
			HSSFRow row = sheet.getRow(i);
			Lot lot = this.entityManager.find(Lot.class, (long) row.getCell(1).getNumericCellValue());
			if (lot == null) {
				LOG.error("No lot id=" + row.getCell(1).getNumericCellValue() + " in the database. Skipping.");
				continue;
			}
			if (!trialedLots.contains(lot)) {
				throw new TrialException("Lot with id=" + lot.getId() + " is not part of this trial. <b>Stopping data import.</b>");
			}
			LOG.debug("Updating from sheet row #" + i + " for: " + lot);
			Double[] traitValues = new Double[traitsUsed];
			for (int j = STARTING_COLUMN; j < row.getLastCellNum(); j++) {
				HSSFCell cell = row.getCell(j);
				traitValues[j - STARTING_COLUMN] = cell == null ? null : cell.getNumericCellValue();
			}
			updateTraitValues(trial, lot, usedTraits, traitValues, 0, traitsUsed);
		}

	}

	/**
	 * @see org.iita.trial.service.TrialServiceImpl#getTraitLastValueClass()
	 */
	@Override
	protected Class<LotTraitLastValue> getTraitLastValueClass() {
		return LotTraitLastValue.class;
	}

	/**
	 * @see org.iita.inventory.service.InventoryTrialService#getTraitLastValues(java.lang.Class, org.iita.inventory.model.Item)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<LotTraitLastValue> getTraitLastValues(Item item) {
		LOG.info("Listing last trait values of item " + item);
		return this.entityManager.createQuery("from LotTraitLastValue ltv where ltv.entity.item=:item order by ltv.date desc").setParameter("item", item).getResultList();
	}
}
