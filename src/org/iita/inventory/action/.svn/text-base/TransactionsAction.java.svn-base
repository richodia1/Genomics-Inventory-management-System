/**
 * inventory.Struts Oct 27, 2009
 */
package org.iita.inventory.action;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.List;

import org.iita.inventory.model.Transaction2;
import org.iita.inventory.service.LotService;
import org.iita.service.ExportService;
import org.iita.struts.DownloadableStream;
import org.iita.util.PagedResult;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;

/**
 * Action to view recent transactions on inventory system
 * 
 * @author mobreza
 */
@SuppressWarnings("serial")
public class TransactionsAction extends BaseActionInventory implements DownloadableStream {
	/**
	 * 
	 */
	private static final String[] EXPORT_FIELDS = new String[] { "date", "lastUpdatedBy", "type", "subtype", "lot.item.name", "lot.id",
			"lot.barCode==null ? '' : lot.barCode.id", "quantity", "scale" };
	/**
	 * 
	 */
	private static final String[] EXPORT_HEADINGS = new String[] { "Timestamp", "User", "Type", "Subtype", "Item", "Lot ID", "Barcode", "Quantity", "Scale" };
	private LotService lotService;
	private ExportService exportService;
	private PagedResult<Transaction2> paged;
	private int startAt = 0;
	private InputStream downloadStream;
	private Calendar startDate;
	private Calendar endDate;
	private final static int maxRecords = 50;

	/**
	 * Construct Transaction action
	 * 
	 * @param lotService Lot service is required to access transaction records
	 * @param exportService Export service for exporting transaction data to XLS
	 */
	public TransactionsAction(LotService lotService, ExportService exportService) {
		this.lotService = lotService;
		this.exportService = exportService;
	}

	/**
	 * @return the paged
	 */
	public PagedResult<Transaction2> getPaged() {
		return this.paged;
	}

	/**
	 * @param startAt the startAt to set
	 */
	public void setStartAt(int startAt) {
		this.startAt = startAt;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Calendar startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the startDate
	 */
	public Calendar getStartDate() {
		return this.startDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Calendar endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the endDate
	 */
	public Calendar getEndDate() {
		return this.endDate;
	}

	/**
	 * @see org.iita.struts.BaseAction#execute()
	 */
	@Override
	public String execute() {
		this.paged = this.lotService.listTransactions(startAt, maxRecords);
		return Action.SUCCESS;
	}

	@Validations(requiredFields = { @RequiredFieldValidator(fieldName = "startDate", message = "Please provide start date for XLS export."),
			@RequiredFieldValidator(fieldName = "endDate", message = "Please provide end date for XLS export.") })
	public String download() {
		if (endDate.before(startDate)) {
			// swap
			Calendar tmp = startDate;
			startDate = endDate;
			endDate = tmp;
		}
		LOG.info("Downloading transactions from " + startDate + " to " + endDate);
		List<Transaction2> transactions = this.lotService.listTransactions(startDate, endDate);
		try {
			this.downloadStream = this.exportService.exportToStream(EXPORT_HEADINGS, EXPORT_FIELDS, transactions);
			return "download";
		} catch (IOException e) {
			LOG.error(e);
			addActionError(e.getMessage());
			return Action.ERROR;
		}
	}

	/**
	 * @see org.iita.struts.DownloadableStream#getDownloadFileName()
	 */
	@Override
	public String getDownloadFileName() {
		return "Transactions-" + "x.xls";
	}

	/**
	 * @see org.iita.struts.DownloadableStream#getDownloadStream()
	 */
	@Override
	public InputStream getDownloadStream() {
		return this.downloadStream;
	}
}
