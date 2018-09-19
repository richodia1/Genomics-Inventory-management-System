/**
 * 
 */
package org.iita.inventory.action;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.struts2.ServletActionContext;
import org.iita.inventory.label.NoPrinterException;
import org.iita.inventory.label.PrinterException;
import org.iita.inventory.model.Lot;
import org.iita.inventory.model.LotSelection;
import org.iita.inventory.service.LabelService;
import org.iita.inventory.service.LotService;
import org.iita.inventory.service.SelectionService;
import org.iita.inventory.service.SummaryService;
import org.iita.struts.DownloadableStream;
import org.iita.util.DeleteFileAfterCloseInputStream;
import org.iita.util.StringUtil;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;

/**
 * Bean managing selected lots.
 * 
 * @author mobreza
 */
public class SelectionAction extends BaseAction implements DownloadableStream {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private SelectionService selectionService = null;
	private SummaryService summaryService = null;
	private LotService lotService = null;

	/** Label service */
	private LabelService labelService = null;
	private List<Lot> results;
	private String referer;
	private LotSelection list = new LotSelection();
	private List<LotSelection> existingLists;
	private String lotIds;
	private InputStream downloadStream;

	/**
	 * @return the referer
	 */
	public String getReferer() {
		return this.referer;
	}

	/**
	 * @param referer the referer to set
	 */
	public void setReferer(String referer) {
		this.referer = referer;
	}

	/**
	 * @return the labelService
	 */
	public LabelService getLabelService() {
		return this.labelService;
	}

	/**
	 * @param labelService the labelService to set
	 */
	public void setLabelService(LabelService labelService) {
		this.labelService = labelService;
	}

	public SelectionAction(SelectionService selectionService, LotService lotService, SummaryService summaryService) {
		this.selectionService = selectionService;
		this.lotService = lotService;
		this.summaryService = summaryService;
	}

	/**
	 * @return the selectionService
	 */
	public SelectionService getSelectionService() {
		return this.selectionService;
	}

	/**
	 * Get selected Lots
	 * 
	 * @return
	 */
	public Collection<Lot> getResults() {
		return this.results;
	}

	/** Default action */
	public String execute() {
		if (this.list != null)
			this.results = this.lotService.getLots(this.list.getSelectedLots());

		this.existingLists = this.selectionService.getLists(getUser());
		return Action.SUCCESS;
	}

	/** Clear selection */
	public String clearAll() {
		this.selectionService.createNewList();
		addNotificationMessage("Created new list.");
		return "refresh";
	}

	/** Remove item from selection */
	public String remove() {
		try {
			String[] ids = (String[]) ActionContext.getContext().getParameters().get("id");
			for (String id : ids) {
				Long lotId = new Long(id);
				this.selectionService.getSelectedList().removeSelection(lotId);
			}
		} catch (NumberFormatException nfe) {
			nfe.printStackTrace();
		}
		addNotificationMessage("Removed from selection list.");
		return "goback";
	}

	/** Add lots to selection */
	public String add() {
		try {
			String[] ids = (String[]) ActionContext.getContext().getParameters().get("id");
			for (String id : ids) {
				Long lotId = new Long(id);
				this.selectionService.getSelectedList().addSelection(lotId);
			}
		} catch (NumberFormatException nfe) {
			nfe.printStackTrace();
		}
		addNotificationMessage("Added to selection list.");
		return "goback";
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
				this.list.addSelection(l.getId());
			}
			addActionMessage("Added lots to selection.");
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
				this.list.addSelection(l.getId());
			}
			addActionMessage("Added lots to selection.");
		}

		return "refresh";
	}

	/** Print labels */
	public String printLabels() {
		execute();

		try {
			this.labelService.print(this.results);
		} catch (NoPrinterException e) {
			this.addActionMessage("Please select your printer at <b>Printer</b> menu. " + e.getMessage());
			return Action.INPUT;
		} catch (PrinterException e) {
			this.addActionError(e.getMessage());
			return Action.INPUT;
		}
		addNotificationMessage("Labels sent for printing.");
		return "refresh";
	}

	/*
	 * (non-Javadoc)
	 * @see com.opensymphony.xwork2.Preparable#prepare()
	 */
	@Override
	public void prepare() {
		this.list = this.selectionService.getSelectedList();

		String referer = ServletActionContext.getRequest().getHeader("Referer");
		LOG.debug("Referer: " + referer);
		if (referer != null) {
			this.referer = referer;
		}
	}

	public String save() {
		this.selectionService.save(this.list);
		addNotificationMessage("Stored selected lot list under name <b>" + this.list.getName() + "</b>.");
		return "refresh";
	}

	public String delete() {
		this.selectionService.delete(this.list, getUser());
		this.selectionService.createNewList();
		addNotificationMessage("Deleted stored list <b>" + this.list.getName() + "</b>. Created a blank list.");
		return "refresh";
	}

	public String load() {
		this.list = this.selectionService.loadList(this.list.getId(), getUser());
		addNotificationMessage("Loaded list <b>" + this.list.getName() + "</b>.");
		return "refresh";
	}

	/**
	 * @return the list
	 */
	public LotSelection getList() {
		return this.list;
	}

	/**
	 * @return the existingLists
	 */
	public List<LotSelection> getExistingLists() {
		return this.existingLists;
	}

	/**
	 * @param lotIds the lotIds to set
	 */
	public void setLotIds(String lotIds) {
		this.lotIds = lotIds;
	}

	public String download() {
		try {
			this.downloadStream = new DeleteFileAfterCloseInputStream(this.summaryService.summarizeToXLS(this.list.getSelectedLots()));
		} catch (IOException e) {
			LOG.error(e);
			addActionError(e.getMessage());
			return ERROR;
		}
		return "download";
	}
	
	public String downloadWithFieldVariables() {
		try {
			this.downloadStream = new DeleteFileAfterCloseInputStream(this.summaryService.summarizeToXLSWithFieldVariables(this.list.getSelectedLots()));
		} catch (IOException e) {
			LOG.error(e);
			addActionError(e.getMessage());
			return ERROR;
		}
		return "download";
	}

	/**
	 * @see org.iita.struts.DownloadableStream#getDownloadFileName()
	 */
	@Override
	public String getDownloadFileName() {
		return this.list.getName() == null || this.list.getName().length() == 0 ? String.format("LotSelection-%1$tF.xls", new Date()) : StringUtil
				.sanitizeFileName(String.format("%1$s-%2$tF.xls", this.list.getName(), new Date()));
	}

	/**
	 * @see org.iita.struts.DownloadableStream#getDownloadStream()
	 */
	@Override
	public InputStream getDownloadStream() {
		return this.downloadStream;
	}
}
