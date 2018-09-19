package org.iita.inventory.action.order;

import java.util.ArrayList;
import java.util.List;

import org.iita.inventory.action.BaseAction;
import org.iita.inventory.model.GenebankOrder;
import org.iita.inventory.model.Order;
import org.iita.inventory.model.Order.OrderState;
import org.iita.inventory.model.OrderTag;
import org.iita.inventory.service.OrderService;
import org.iita.util.PagedResult;

import com.opensymphony.xwork2.Action;

/**
 * Action to display order list
 * 
 * @author mobreza
 */
@SuppressWarnings("serial")
public class OrderListAction extends BaseAction {
	private static final int maxRequests = 30;
	private OrderService orderService;
	private int startAt = 0;
	private PagedResult<? extends Order> paged;
	private OrderState state = OrderState.ALL;

	public OrderListAction(OrderService orderService) {
		this.orderService = orderService;
	}

	public void setStartAt(int startAt) {
		this.startAt = startAt;
	}

	public PagedResult<? extends Order> getPaged() {
		return paged;
	}

	public int getStartAt() {
		return startAt;
	}
	
	/**
	 * @param state the state to set
	 */
	public void setState(OrderState state) {
		this.state = state;
	}
	
	/**
	 * @return the state
	 */
	public OrderState getState() {
		return this.state;
	}

	/**
	 * Find existing tag
	 * 
	 * @param tag
	 * @return
	 */
	public List<OrderTag> findTagByCategory(GenebankOrder order, String category) {
		LOG.debug("Looking up tags for categrory: " + category);
		ArrayList<OrderTag> tags = new ArrayList<OrderTag>();
		for (OrderTag entityTag : order.getTags()) {
			if (entityTag.isInCategory(category))
				tags.add(entityTag);
		}
		return tags;
	}

	/**
	 * Default action method lists orders
	 */
	@Override
	public String execute() {
		if(!getUser().hasRole("ROLE_BREEDER"))
			this.paged = this.orderService.listOrders(this.state, startAt, maxRequests);
		else
			this.paged = this.orderService.listOrders(this.state, startAt, maxRequests, getUser());
		
		return Action.SUCCESS;
	}
}
