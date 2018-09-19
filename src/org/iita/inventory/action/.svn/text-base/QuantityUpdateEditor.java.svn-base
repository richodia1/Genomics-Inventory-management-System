/**
 * 
 */
package org.iita.inventory.action;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.iita.inventory.barcode.BarcodingException;
import org.iita.inventory.model.GenebankOrder;
import org.iita.inventory.model.Lot;
import org.iita.inventory.model.LotSelection;
import org.iita.inventory.model.QuantityUpdate;
import org.iita.inventory.model.QuantityUpdateBulk;
import org.iita.inventory.service.BarcodingService;
import org.iita.inventory.service.InventoryException;
import org.iita.inventory.service.LotService;
import org.iita.inventory.service.QuantityUpdateService;
import org.iita.inventory.service.SelectionService;
import org.iita.service.ExportService;
import org.iita.struts.DownloadableStream;
import org.iita.util.StringUtil;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;

/**
 * Struts2 backing class for updating quantities in the inventory system.
 * 
 * @author mobreza
 */
public class QuantityUpdateEditor extends BaseAction implements DownloadableStream {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected QuantityUpdateService service;
	protected LotService lotService;
	protected SelectionService selectionService;
	protected BarcodingService barcodingService;
	private ExportService exportService;

	/** QuantityUpdateBulk ID */
	protected Long id;
	/** QuantityUpdate ID */
	protected Long itemid;
	/** BarCode */
	protected Long barCode;
	/** Active scale */
	private String activeScale;
	private boolean affectingInventory = true;

	/**
	 * @return the barCode
	 */
	public Long getBarCode() {
		return this.barCode;
	}

	/**
	 * @return the barCode
	 */
	public Long getBarcode() {
		return this.barCode;
	}

	/**
	 * @param barCode the barCode to set
	 */
	public void setBarCode(Long barCode) {
		this.barCode = barCode;
	}

	/**
	 * @param barCode the barCode to set
	 */
	public void setBarcode(Long barCode) {
		this.barCode = barCode;
	}

	protected QuantityUpdateBulk description = new QuantityUpdateBulk();

	protected QuantityUpdate lot;
	private InputStream downloadStream;

	public QuantityUpdateEditor(QuantityUpdateService service, LotService lotService, SelectionService selectionService, BarcodingService barcodingService,
			ExportService exportService) {
		this.service = service;
		this.lotService = lotService;
		this.selectionService = selectionService;
		this.barcodingService = barcodingService;
		this.exportService = exportService;
	}

	/**
	 * @return the description
	 */
	public QuantityUpdateBulk getDescription() {
		return this.description;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return this.id;
	}

	/**
	 * @return the itemid
	 */
	public Long getItemid() {
		return this.itemid;
	}

	/**
	 * @return the lot
	 */
	public QuantityUpdate getLot() {
		return this.lot;
	}

	/**
	 * Get all lots under the bulk update.
	 * 
	 * @return all lots in this bulk update.
	 */
	public List<QuantityUpdate> getLots() {
		return this.description.getLots();
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(QuantityUpdateBulk description) {
		this.description = description;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @param itemid the itemid to set
	 */
	public void setItemid(Long itemid) {
		this.itemid = itemid;
	}

	/**
	 * @param lot the lot to set
	 */
	public void setLot(QuantityUpdate lot) {
		this.lot = lot;
	}

	/**
	 * @see com.opensymphony.xwork2.Preparable#prepare()
	 */
	@Override
	public void prepare() {
		if (this.id != null) {
			this.description = this.service.load(this.id);
		} else {
			this.description.setDate(new Date());
		}

		if (this.itemid != null && this.description != null) {
			this.lot = this.service.loadItem(this.description, this.itemid);
			// check if the item belongs to the lot
			if (this.lot.getDescription() != this.description) {
				this.lot = null;
			}
		} else {
			LOG.debug("QuantityUpdateEditor.prepare() no itemID provided");
		}
	}

	public String edit() {
		return Action.INPUT;
	}

	@Validations(requiredStrings = { @RequiredStringValidator(fieldName = "description.title", message = "Please enter title for inventory update."),
			@RequiredStringValidator(fieldName = "description.transactionSubtype", message = "Please transaction subtype.") }, requiredFields = { @RequiredFieldValidator(fieldName = "description.date", message = "Enter update date.") })
	public String saveDescription() {
		this.service.store(this.description);
		return "updated";
	}

	/**
	 * Delete whole lot.
	 * 
	 * @return
	 */
	public String deleteBulk() {
		this.service.delete(this.description);
		return "redirect";
	}

	public String updateItem() {
		LOG.debug("AccessionAction.updateItem() id=" + this.id + "\n\t");
		this.lot.setLot(this.lotService.load(this.lot.getLot().getId()));
		this.description.getLots().add(this.lot);
		this.lot.setDescription(this.description);
		if (this.description.isAffectingInventory())
			this.lot.setScale(this.lot.getLot().getScale());
		else
			this.lot.setScale(activeScale);
		try {
			this.service.store(this.lot);
			addNotificationMessage("Registering <b>" + this.lot.getQuantity() + " " + this.lot.getScale() + "</b> of " + this.lot.getLot().getItem().getName()
					+ ", ID " + this.lot.getLot().getId());
			return "updated";
		} catch (Exception e) {
			addNotificationMessage("Error storing update: " + e.getMessage());
			return "updated";
		}
	}

	public String removeItem() {
		if (this.lot != null)
			this.service.delete(this.lot);
		this.lot = null;
		return "updated";
	}

	public String scanLot() {
		this.itemid = null;
		if (this.barCode == null) {
			addActionError("No barcode provided.");
			return "input";
		}

		Lot lot = this.lotService.loadByBarcode(this.barCode);
		if (lot == null) {
			addActionError("No lot registered with barcode: " + this.barCode);
			this.lot = null;
			this.barCode = null;
		} else {
			this.lot = new QuantityUpdate();
			this.lot.setLot(lot);
			this.lot.setScale(lot.getScale());
			if (!this.description.isAffectingInventory() && activeScale != null) {
				this.lot.setScale(activeScale);
			}
		}
		return "input";
	}

	/**
	 * Commit changes to inventory.
	 * 
	 * @return
	 */
	public String commit() {
		try {
			this.service.commit(this.description);
		} catch (IllegalStateException e) {
			addActionError(e.getMessage());
			return Action.ERROR;
		}
		return "updated";
	}

	/**
	 * Commit changes to inventory.
	 * 
	 * @return
	 */
	public String rollback() {
		try {
			this.service.rollback(this.description);
		} catch (IllegalStateException e) {
			addActionError(e.getMessage());
			return Action.ERROR;
		}
		return "updated";
	}

	/**
	 * Action method that will redirect user to page where viability results can be entered
	 * 
	 * @return
	 */
	//	public String viability() {
	//		return "do-viability";
	//	}
	/**
	 * Action method that will generate new lots, assign barcodes and put the lots to currently selected lot list
	 * 
	 * @return
	 */
	public String safeduplication() {
		List<Lot> safeDupedLots;
		try {
			QuantityUpdateBulk newbulk = this.lotService.registerDuplicatedLots(this.description);
			safeDupedLots = newbulk.getLotList();
			this.barcodingService.ensureBarCode(safeDupedLots);
			LotSelection selection = this.selectionService.getSelectedList();
			selection.replaceSelection(safeDupedLots);
			addActionMessage("New lots were generated and were put to your current selection list.");
			return "to-selection";
		} catch (InventoryException e1) {
			LOG.error(e1);
			addActionError(e1.getMessage());
			return Action.ERROR;
		} catch (BarcodingException e) {
			LOG.error(e);
			addActionError(e.getMessage());
			return Action.ERROR;
		}
	}

	public String exportXLS() {
		try {
			this.downloadStream = this.exportService.exportToStream(new String[] { "Lot ID", "Item", "Location", "Quantity", "Scale", "Barcode",
					"Year processed", "Introduction date", "Line number", "Subculture date" }, new String[] { "lot.id", "lot.item.name", "lot.location.name",
					"quantity", "scale", "lot.barCode.id", "lot.yearProcessed", "lot.introductionDate", "lot.line", "lot.regenerationDate" },
					this.description.getSortedLots());
		} catch (IOException e) {
			addActionError(e.getMessage());
			return Action.ERROR;
		}
		return "download";
	}

	/**
	 * @see org.iita.struts.DownloadableStream#getDownloadFileName()
	 */
	@Override
	public String getDownloadFileName() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return this.description == null ? "???" : StringUtil.sanitizeFileName(this.description.getTitle()) + "-" + df.format(new Date()) + ".xls";
	}

	/**
	 * @see org.iita.struts.DownloadableStream#getDownloadStream()
	 */
	@Override
	public InputStream getDownloadStream() {
		return this.downloadStream;
	}

	/**
	 * @param activeScale the activeScale to set
	 */
	public void setActiveScale(String activeScale) {
		this.activeScale = activeScale;
	}

	/**
	 * @return the activeScale
	 */
	public String getActiveScale() {
		return this.activeScale;
	}

	/**
	 * @return the affectingInventory
	 */
	public boolean isAffectingInventory() {
		return this.affectingInventory;
	}

	/**
	 * @param affectingInventory the affectingInventory to set
	 */
	public void setAffectingInventory(boolean affectingInventory) {
		this.affectingInventory = affectingInventory;
	}
	
	/**
	 * Generate Genebank Order from this distribution
	 * @return
	 */
	public String createorder() {
		try {
			GenebankOrder order = this.service.createOrder(this.description);
			LOG.info("Order generated from bulk update: " + order);
		} catch (Exception e) {
			addNotificationMessage("Could not generate order. " + e.getMessage());
			LOG.error(e, e);
		}
		return "updated";		
	}
}
