/**
 * inventory.Struts Oct 22, 2009
 */
package org.iita.inventory.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.iita.inventory.model.QuantityUpdateBulk;
import org.iita.inventory.model.Transaction2.Type;
import org.iita.inventory.service.QuantityUpdateService;
import org.iita.inventory.service.ViabilityService;
import org.iita.inventory.service.ViabilityService.ViabilityResult;

import com.opensymphony.xwork2.Action;

/**
 * Action to allow entering results of viability testing. Viability test is done by first creating a distribution record of type VIABILITYTEST, and committed to
 * inventory. After committing, the test results can be entered into the form.
 * 
 * The form will display all lots that went out for VIABILITYTEST and render a single input box next to the name where user can enter the viability %. The date
 * of testing is entered once on top of page.
 * 
 * This action requires the ID of the original distribution record.
 * 
 * 
 * 
 * @author mobreza
 */
@SuppressWarnings("serial")
public class ViabilityResultsAction extends org.iita.struts.BaseAction {
	private QuantityUpdateService quantityUpdateService;
	private ViabilityService viabilityService;

	// Source Bulkupdate ID
	private Long id;
	private QuantityUpdateBulk bulk;
	private List<ViabilityResult> viability = new ArrayList<ViabilityResult>();
	private Date testDate = new Date();

	/**
	 * @param quantityUpdateService Service to load QuantityUpdateBulk records
	 * @param viabilityService Viability service
	 * 
	 */
	public ViabilityResultsAction(QuantityUpdateService quantityUpdateService, ViabilityService viabilityService) {
		this.quantityUpdateService = quantityUpdateService;
		this.viabilityService = viabilityService;
	}

	/**
	 * Set the ID of source QuantityUpdateBulk record
	 * 
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Get the source QuantityUpdateBulk record.
	 * 
	 * @return the bulk
	 */
	public QuantityUpdateBulk getBulk() {
		return this.bulk;
	}

	/**
	 * Return reference to viability result list
	 * 
	 * @return the viability
	 */
	public List<ViabilityResult> getViability() {
		return this.viability;
	}

	/**
	 * Get viability test date
	 * 
	 * @return the testDate
	 */
	public Date getTestDate() {
		return this.testDate;
	}

	/**
	 * Allow user to set date of viablity test
	 * 
	 * @param testDate the testDate to set
	 */
	public void setTestDate(Date testDate) {
		this.testDate = testDate;
	}

	/**
	 * On prepare, the source QuantityUpdateBulk record is loaded and checked that is of proper type
	 * 
	 * @see org.iita.struts.BaseAction#prepare()
	 */
	@Override
	public void prepare() {
		if (this.id != null) {
			QuantityUpdateBulk bulk = this.quantityUpdateService.load(this.id);
			if (bulk != null && bulk.getTransactionType() == Type.OUT && bulk.getTransactionSubtype().equalsIgnoreCase("VIABILITYTEST")) {
				this.bulk = bulk;
			}
		} else
			this.bulk = null;
	}

	/**
	 * The default action method will check that a source {@link QuantityUpdateBulk} record was provided and that the record is of proper transaction type OUT
	 * and subtype "VIABILITYTEST".
	 * 
	 * @see org.iita.struts.BaseAction#execute()
	 */
	@Override
	public String execute() {
		if (this.bulk == null) {
			addActionError("The source VIABILITYTEST bulk id was not provided.");
			return Action.ERROR;
		}

		LOG.debug("Got " + this.viability.size() + " viability test results.");

		return Action.SUCCESS;
	}

	/**
	 * Action method to save the Viability test results.
	 * 
	 * @return
	 */
	public String save() {
		if (this.bulk == null) {
			addActionError("The source VIABILITYTEST bulk id was not provided.");
			return Action.ERROR;
		}

		try {
			this.viabilityService.addViabilityResult(this.testDate, this.viability);
		} catch (Exception e) {
			LOG.error(e);
			addActionError(e.getMessage());
			return Action.ERROR;
		}
		return Action.SUCCESS;
	}
}
