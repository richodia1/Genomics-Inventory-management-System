package org.iita.inventory.action;


import org.iita.inventory.service.LotService;
import org.iita.util.PagedResult;

import com.opensymphony.xwork2.Action;

public class ListLotLocationQuantityAction  extends BaseAction  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** Lot service provides access to data */
	private LotService lotService;
	private int startAt = 0;
	private int maxResults = 50;
	private PagedResult<Object[]> paged;
	
	/**
	 * @param startAt the startAt to set
	 */
	public void setStartAt(int startAt) {
		this.startAt = startAt;
	}

	
	public ListLotLocationQuantityAction(LotService lotService) {
		this.lotService = lotService;
	}
	
	/**
	 * Execute
	 */
	@Override
	public String execute() {
		//this.paged = new PagedResult<Object[]>();
		
		this.paged = this.lotService.getListLotTotalQuantityInLocation(this.startAt, this.maxResults);
		return Action.SUCCESS;
	}
	
	/**
	 * Prepare called after properties are set.
	 */
	//@SuppressWarnings("unchecked")
	@Override
	public void prepare() {
		super.prepare();		
	}

	/**
	 * @param lots the lots to set
	 */
	public void setPaged(PagedResult<Object[]> paged) {
		this.paged = paged;
	}

	/**
	 * @return the lots
	 */
	public PagedResult<Object[]> getPaged() {
		return paged;
	}

}
