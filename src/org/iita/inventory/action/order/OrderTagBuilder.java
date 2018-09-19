/**
 * inventory.Struts Oct 21, 2010
 */
package org.iita.inventory.action.order;

import org.iita.crm.action.TagBuilderAction;
import org.iita.crm.service.TagService;
import org.iita.inventory.model.GenebankOrder;
import org.iita.inventory.service.OrderService;

/**
 * @author mobreza
 */
@SuppressWarnings("serial")
public class OrderTagBuilder extends TagBuilderAction<GenebankOrder> {
	private OrderService orderService;

	/**
	 * @param tagService
	 * @param orderService 
	 */
	public OrderTagBuilder(TagService tagService, OrderService orderService) {
		super(tagService);
		this.orderService = orderService;
	}

	/**
	 * @see org.iita.crm.action.TagBuilderAction#loadProfile(java.lang.Long)
	 */
	@Override
	protected GenebankOrder loadProfile(Long id) {
		return this.orderService.getOrder(id);
	}
	
	public GenebankOrder getOrder() {
		return getProfile();
	}
}
