package org.iita.inventory.action.mobile;

import org.iita.inventory.action.BaseActionInventory;
import org.iita.inventory.model.Lot;
import org.iita.inventory.service.LotService;

import com.opensymphony.xwork2.Action;

/**
 * Renders lot information to profile page
 * 
 * @author mobreza
 */
@SuppressWarnings("serial")
public class LotProfileAction extends BaseActionInventory {
	private LotService lotService;
	private Long barcode, id;
	private Lot lot;
	private double totalLocationQuantity;

	/** Action constructor */
	public LotProfileAction(LotService lotService) {
		this.lotService = lotService;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	/**
	 * Passed in as querystring parameter
	 * 
	 * @param barcode
	 */
	public void setBarcode(Long barcode) {
		this.barcode = barcode;
	}

	/**
	 * Give lot object to JSP
	 * 
	 * @return
	 */
	public Lot getLot() {
		return lot;
	}

	@Override
	public void prepare() {
		if (this.id!=null) {
			this.lot = this.lotService.load(this.id);
		}
		else if (this.barcode != null) {
			// load lot by barcode
			this.lot = this.lotService.loadByBarcode(this.barcode);
		}
	}

	/**
	 * Action method to execute to display lot information
	 */
	@Override
	public String execute() {
		if (this.lot==null) {
			addActionError("No such lot.");
			return Action.ERROR;
		}
		this.totalLocationQuantity=this.lotService.getTotalQuantityInLocation(this.lot);
		return Action.SUCCESS;
	}
	
	/**
	 * @return the totalLocationQuantity
	 */
	public double getTotalLocationQuantity() {
		return this.totalLocationQuantity;
	}
}
