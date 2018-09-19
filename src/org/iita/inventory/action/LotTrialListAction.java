/**
 * inventory.Struts Jul 13, 2010
 */
package org.iita.inventory.action;

import org.iita.inventory.model.LotTrial;
import org.iita.inventory.service.InventoryTrialService;
import org.iita.util.PagedResult;

import com.opensymphony.xwork2.Action;

/**
 * @author mobreza
 */
@SuppressWarnings("serial")
public class LotTrialListAction extends BaseAction {
	private InventoryTrialService inventoryTrialService;
	private int startAt = 0;
	private int maxResults = 20;
	private PagedResult<LotTrial> paged;

	/**
	 * @return the paged
	 */
	public PagedResult<LotTrial> getPaged() {
		return this.paged;
	}

	/**
	 * @param startAt the startAt to set
	 */
	public void setStartAt(int startAt) {
		this.startAt = startAt;
	}

	public LotTrialListAction(InventoryTrialService inventoryTrialService) {
		this.inventoryTrialService = inventoryTrialService;
	}

	/**
	 * @see org.iita.struts.BaseAction#execute()
	 */
	@Override
	public String execute() {
		this.paged = this.inventoryTrialService.listTrials(this.startAt, this.maxResults);
		return Action.SUCCESS;
	}

	/**
	 * @see org.iita.struts.BaseAction#execute()
	 */
	public String openTrials() {
		this.paged = this.inventoryTrialService.listOpenTrials(0, 10);
		return Action.SUCCESS;
	}

	/**
	 * @see org.iita.struts.BaseAction#execute()
	 */
	public String myTrials() {
		this.paged = this.inventoryTrialService.listTrials(getUser(), 0, 10);
		return Action.SUCCESS;
	}
}
