/**
 * inventory.Struts Oct 21, 2010
 */
package org.iita.inventory.action.order;

import org.iita.crm.action.TagAction;
import org.iita.crm.service.TagService;
import org.iita.inventory.model.GenebankOrder;
import org.iita.inventory.model.OrderTag;

import com.opensymphony.xwork2.conversion.annotations.TypeConversion;

/**
 * @author mobreza
 */
@SuppressWarnings("serial")
public class InventoryTagAction extends TagAction {
	/**
	 * @param tagService
	 */
	public InventoryTagAction(TagService tagService) {
		super(tagService);
	}

	private GenebankOrder order;

	/**
	 * @param order the order to set
	 */
	@TypeConversion(converter = "genericConverter")
	public void setOrder(GenebankOrder order) {
		this.order = order;
	}

	/**
	 * @see org.iita.crm.action.TagAction#prepare()
	 */
	@Override
	public void prepare() {
		if (this.id != null) {
			if (this.order != null)
				this.tag = this.tagService.loadTag(OrderTag.class, this.id);
		} else {
			if (this.order != null) {
				OrderTag orderTag;
				this.tag = orderTag = new OrderTag();
				orderTag.setEntity(this.order);
			}
		}
	}
	
	/**
	 * @see org.iita.crm.action.TagAction#update()
	 */
	@Override
	public String update() {
		super.update();
		return "order";
	}
	
	/**
	 * @see org.iita.crm.action.TagAction#remove()
	 */
	@Override
	public String remove() {
		super.remove();
		return "order";
	}
}
