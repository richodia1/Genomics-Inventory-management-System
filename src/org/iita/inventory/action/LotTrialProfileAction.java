/**
 * inventory.Struts Jul 10, 2010
 */
package org.iita.inventory.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.iita.inventory.model.Lot;
import org.iita.inventory.model.LotTrial;
import org.iita.inventory.service.InventoryTrialService;
import org.iita.inventory.service.LotService;
import org.iita.trial.action.TrialProfileAction;
import org.iita.trial.service.TrialException;

import com.opensymphony.xwork2.Action;

/**
 * @author mobreza
 */
@SuppressWarnings("serial")
public class LotTrialProfileAction extends TrialProfileAction<Lot, LotTrial> {
	private Long barcode = null;
	private LotService lotService;
	private String lotIds;
	
	private Long[] item_id;
	private Long[] lot_id;
	private Long[] barCode_id;
	private Long[] num_tested;
	private Long[] germ_6d;
	private Long[] germ_10d;
	private Double[] perc_6d;
	private Double[] perc_10d;
	private String numTested;
	
	/**
	 * @param barcode the barcode to set
	 */
	public void setBarcode(Long barcode) {
		this.barcode = barcode;
	}

	/**
	 * @param trialService
	 */
	public LotTrialProfileAction(InventoryTrialService trialService, LotService lotService) {
		super(trialService);
		this.lotService = lotService;
	}

	/**
	 * Action method that takes the scanned barcode (through the {@link #barcode} parameter), finds the lot and ensured the lot is to be trialed in this trial.
	 */
	public String addEntity() {
		LOG.info("Ensuring lot with barcode " + barcode + " is registered with trial");
		if (this.barcode == null) {
			addActionError("No barcode scanned.");
			return "refresh";
		}
		Lot lot = this.lotService.loadByBarcode(this.barcode);
		if (lot == null) {
			addActionError("No lot with barcode: " + this.barcode + " found.");
			return "refresh";
		}
		this.trialService.ensureEntityTrialed(this.trial, lot);
		addNotificationMessage("Lot " + lot.getId() + " registered with trial <b>" + this.trial.getName() + "</b>");
		return "refresh";
	}
	

	/**
	 * Form parameter used for bulk import of lots
	 * 
	 * @param lotIds the lotIds to set
	 */
	public void setLotIds(String lotIds) {
		this.lotIds = lotIds;
	}

	/** Add lots to selection */
	public String addBulk() {
		if (this.lotIds == null) {
			addNotificationMessage("No lot IDs specified");
			return "refresh";
		}

		String[] ids = this.lotIds.split("[\\s,\\r\\n]+");
		List<Long> lotNumbers = new ArrayList<Long>();
		for (String id : ids) {
			if (id.trim().length() == 0)
				continue;
			try {
				Long lotId = new Long(id);
				lotNumbers.add(lotId);
			} catch (NumberFormatException nfe) {
				addNotificationMessage("Could not parse lot ID: " + id);
				LOG.info("Could not parse: " + id + " to Long");
			}
		}
		List<Lot> lots = lotService.getLots(lotNumbers);
		if (lots != null) {
			for (Lot l : lots) {
				this.trialService.ensureEntityTrialed(this.trial, l);
			}
			addActionMessage("Added lots to trial.");
		}
		return "refresh";
	}

	/** Add lots to selection by barcode */
	public String addBar() {
		if (this.lotIds == null) {
			addNotificationMessage("No lot barcodes specified");
			return "refresh";
		}

		String[] ids = this.lotIds.split("[\\s,\\r\\n]+");
		List<Long> lotBarcodes = new ArrayList<Long>();
		for (String id : ids) {
			if (id.trim().length() == 0)
				continue;
			try {
				Long lotId = new Long(id);
				lotBarcodes.add(lotId);
			} catch (NumberFormatException nfe) {
				addNotificationMessage("Could not parse lot ID: " + id);
				LOG.info("Could not parse barcode: " + id + " to Long");
			}
		}
		List<Lot> lots = lotService.getLotsByBarcode(lotBarcodes);
		if (lots != null) {
			for (Lot l : lots) {
				this.trialService.ensureEntityTrialed(this.trial, l);
			}
			addActionMessage("Added lots to trial.");
		}

		return "refresh";
	}
	
	/** Add lots to selection by barcode */
	public String addTrial() {
		if (this.lotIds == null) {
			addNotificationMessage("No lot barcodes specified");
			return "refresh";
		}

		String[] ids = this.lotIds.split("[\\s,\\r\\n]+");
		List<Long> lotBarcodes = new ArrayList<Long>();
		for (String id : ids) {
			if (id.trim().length() == 0)
				continue;
			try {
				Long lotId = new Long(id);
				lotBarcodes.add(lotId);
			} catch (NumberFormatException nfe) {
				addNotificationMessage("Could not parse lot ID: " + id);
				LOG.info("Could not parse barcode: " + id + " to Long");
			}
		}
		List<Lot> lots = lotService.getLotsByBarcode(lotBarcodes);
		if (lots != null) {
			for (Lot l : lots) {
				this.trialService.ensureEntityTrialed(this.trial, l);
			}
			//super.showForm = true;
			addActionMessage("Added lots to trial form.");
		}
		
		return "showform";
	}
	
	//@SuppressWarnings("unused")
	public String addTrailEntries(){
		try {
			HSSFWorkbook wb = new HSSFWorkbook();
			CreationHelper createHelper = wb.getCreationHelper();
		    HSSFSheet sheet = wb.createSheet("new sheet");
		    HSSFRow row = sheet.createRow((short)0);

		    // Or do it on one line.
		    row.createCell(0).setCellValue(createHelper.createRichTextString("Item"));
		    row.createCell(1).setCellValue(createHelper.createRichTextString("Lot ID"));
		    row.createCell(2).setCellValue(createHelper.createRichTextString("Barcode"));
		    row.createCell(3).setCellValue(createHelper.createRichTextString("Germination 6d"));
		    row.createCell(4).setCellValue(createHelper.createRichTextString("Viability"));
		    
			for(int i=0;i<item_id.length;++i){
				Lot lot = this.lotService.loadByBarcode(this.barCode_id[i]);

			    // Create a row and put some cells in it. Rows are 0 based.
			    row = sheet.createRow((short)i+1);

			    // Or do it on one line.
			    row.createCell(0).setCellValue(createHelper.createRichTextString(lot.getItem().getName()));
			    row.createCell(1).setCellValue(lot_id[i]);
			    row.createCell(2).setCellValue(barCode_id[i]);
			    row.createCell(3).setCellValue(perc_6d[i]);
			    row.createCell(4).setCellValue(perc_10d[i]);
			}
			
			try {

				File f = new File(getPath() + "WebContent/filetocreate/", getUser().getId() + "file.xls");
				
				if(!f.exists())
				{
			        //If directories are not available then create it
			        File parent_directory = f.getParentFile();
			        if (null != parent_directory)
			        {
						parent_directory.mkdirs();
					}
			        f.createNewFile();
				}

				FileOutputStream out = new FileOutputStream(f,false);
				wb.write(out);
				out.close();
				
				List<File> files = new ArrayList<File>();
				files.add(f);
				
				this.trialService.updateFromXLS(this.trial, files);
				
				if (f.exists()) {
					f.delete();     
				}
			} catch (IOException e) {
				addActionMessage("ERROR: " + e.getMessage());
				return Action.ERROR;
			}
				
			addActionMessage("Trial data updated successfully from Form entry.");
			return "refresh";
		} catch (TrialException e) {
			LOG.error(e);
			addActionError(e.getMessage());
			return Action.ERROR;
		}
	}

	/**
	 * @return the item_id
	 */
	public Long[] getItem_id() {
		return item_id;
	}

	/**
	 * @param item_id the item_id to set
	 */
	public void setItem_id(Long[] item_id) {
		this.item_id = item_id;
	}

	/**
	 * @return the lot_id
	 */
	public Long[] getLot_id() {
		return lot_id;
	}

	/**
	 * @param lot_id the lot_id to set
	 */
	public void setLot_id(Long[] lot_id) {
		this.lot_id = lot_id;
	}

	/**
	 * @return the barCode_id
	 */
	public Long[] getBarCode_id() {
		return barCode_id;
	}

	/**
	 * @param barCode_id the barCode_id to set
	 */
	public void setBarCode_id(Long[] barCode_id) {
		this.barCode_id = barCode_id;
	}

	/**
	 * @return the num_tested
	 */
	public Long[] getNum_tested() {
		return num_tested;
	}

	/**
	 * @param num_tested the num_tested to set
	 */
	public void setNum_tested(Long[] num_tested) {
		this.num_tested = num_tested;
	}

	/**
	 * @return the germ_6d
	 */
	public Long[] getGerm_6d() {
		return germ_6d;
	}

	/**
	 * @param germ_6d the germ_6d to set
	 */
	public void setGerm_6d(Long[] germ_6d) {
		this.germ_6d = germ_6d;
	}

	/**
	 * @return the germ_10d
	 */
	public Long[] getGerm_10d() {
		return germ_10d;
	}

	/**
	 * @param germ_10d the germ_10d to set
	 */
	public void setGerm_10d(Long[] germ_10d) {
		this.germ_10d = germ_10d;
	}

	/**
	 * @return the perc_6d
	 */
	public Double[] getPerc_6d() {
		return perc_6d;
	}

	/**
	 * @param perc_6d the perc_6d to set
	 */
	public void setPerc_6d(Double[] perc_6d) {
		this.perc_6d = perc_6d;
	}

	/**
	 * @return the perc_10d
	 */
	public Double[] getPerc_10d() {
		return perc_10d;
	}

	/**
	 * @param perc_10d the perc_10d to set
	 */
	public void setPerc_10d(Double[] perc_10d) {
		this.perc_10d = perc_10d;
	}

	/**
	 * @param numTested the numTested to set
	 */
	public void setNumTested(String numTested) {
		this.numTested = numTested;
	}

	/**
	 * @return the numTested
	 */
	public String getNumTested() {
		return numTested;
	}
}
