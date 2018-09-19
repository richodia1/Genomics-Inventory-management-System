/**
 * inventory.Struts Nov 3, 2009
 */
package org.iita.inventory.action.order;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

import org.iita.inventory.action.BaseAction;
import org.iita.inventory.model.GenebankOrder;
import org.iita.inventory.model.GenebankOrderItem;
import org.iita.inventory.model.Item;
import org.iita.inventory.model.Lot;
import org.iita.inventory.model.LotTraitLastValue;
import org.iita.inventory.model.Order;
import org.iita.inventory.service.InventoryTrialService;
import org.iita.inventory.service.LotService;
import org.iita.inventory.service.OrderService;

import com.opensymphony.xwork2.Action;

/**
 * Action to assign source Lots to requested items
 * 
 * @author mobreza
 */
@SuppressWarnings("serial")
public class OrderLotsAction extends BaseAction {
	private OrderService orderService;
	private GenebankOrder order = null;
	private Long id;
	private LotService lotService;
	private Dictionary<Item, List<Lot>> availableLots;

	// data settings
	private List<Long> itemId = new ArrayList<Long>();
	private List<Double> quantity = new ArrayList<Double>();
	private List<Long> lotId = new ArrayList<Long>();
	private InventoryTrialService inventoryTrialService;

	/**
	 * @param orderService
	 * @param lotService
	 * @param inventoryTrialService 
	 * 
	 */
	public OrderLotsAction(OrderService orderService, LotService lotService, InventoryTrialService inventoryTrialService) {
		this.orderService = orderService;
		this.lotService = lotService;
		this.inventoryTrialService=inventoryTrialService;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the order
	 */
	public Order getOrder() {
		return this.order;
	}

	/**
	 * @param lotId the lotId to set
	 */
	public void setLotId(List<Long> lotId) {
		this.lotId = lotId;
	}

	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(List<Double> quantity) {
		this.quantity = quantity;
	}

	/**
	 * @param itemId the itemId to set
	 */
	public void setItemId(List<Long> itemId) {
		this.itemId = itemId;
	}
	
	public LotTraitLastValue getTraitLastValue(String var, Lot lot) {
		LotTraitLastValue traitLastValue = this.inventoryTrialService.getTraitLastValue(LotTraitLastValue.class, var, lot);
		LOG.info("Got trait last value: " + traitLastValue);
		return traitLastValue;
	}
	/**
	 * @see org.iita.inventory.action.BaseAction#prepare()
	 */
	@Override
	public void prepare() {
		if (this.id != null)
			this.order = this.orderService.getOrder(this.id);
	}

	/**
	 * @return the availableLots
	 */
	public Dictionary<Item, List<Lot>> getAvailableLots() {
		return this.availableLots;
	}

	/**
	 * Default action method will render available Lots and quantities and show selected lots
	 * 
	 * @see org.iita.struts.BaseAction#execute()
	 */
	@Override
	public String execute() {
		if (this.order == null) {
			addActionError("Order not selected");
			return Action.ERROR;
		}

		this.availableLots = this.lotService.getLotsForItems(this.order.getItemElements());

		return "input";
	}

	/**
	 * Update Lot - Item mappings
	 * 
	 * @return
	 */
	public String update() {
		try {
			//System.out.println("ITEM: " + this.order.getItems().size());
			for (GenebankOrderItem item : this.order.getItems()) {
				//System.out.println("ITEM: " + item.getItemName());
				updateLotAssignment(item);
			}
			this.orderService.updateOrder(this.order);
			return "toorder";
		} catch (Exception e) {
			LOG.error(e);
			e.printStackTrace();
			addActionError(e.getMessage());
			return Action.ERROR;
		}
	}

	/**
	 * @param item
	 */
	private void updateLotAssignment(GenebankOrderItem item) {
		Long __id = item.getId();
		int myIndex = -1;
		
		for (int i = itemId.size() - 1; i >= 0; i--) {
			//System.out.println("ITEM ID Size 1 : " + itemId.size());
			if (itemId.get(i) == null){
				continue;
			}
			if (itemId.get(i).equals(__id)) {
				myIndex = i;
				//System.out.println("itemId.get(i) == null: Item not included in lot assignment __ID: " + __id + " / myIndex: " + myIndex );
				//System.out.println("MyIndex 2-- : " + myIndex);
				break;
			}
		}
		
		if (myIndex==-1) {
			LOG.debug("Item not included in lot assignment");
			//System.out.println("Item not included in lot assignment __ID: " + __id + " / myIndex: " + myIndex );
			return;
		}

		Lot lot = null;
		if (myIndex < this.lotId.size() && this.lotId.get(myIndex) != null){
			lot = this.lotService.load(this.lotId.get(myIndex));
			//System.out.println("LOT: " + lot);
			//System.out.println("myIndex: " + myIndex);
		}

		if (lot != null) {
			if (myIndex < this.quantity.size() && this.quantity.get(myIndex)!=null)
				item.setQuantity(this.quantity.get(myIndex));
			else
				item.setQuantity(1d);
			item.setLot(lot);
			item.setScale(lot.getScale());
			//System.out.println("All Assigned!");
		} else {
			//System.out.println("All null!");
			item.setQuantity(null);
			item.setScale(null);
			item.setLot(null);
		}
	}
}
