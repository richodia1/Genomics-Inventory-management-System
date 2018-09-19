/**
 * inventory.Struts Oct 21, 2010
 */
package org.iita.inventory.model;

import javax.persistence.Entity;

import org.iita.crm.model.EntityTag;

/**
 * @author mobreza
 */
@Entity
public class OrderTag extends EntityTag<GenebankOrder> {

	/**
	 * Default constructor
	 */
	public OrderTag() {

	}

	/**
	 * Copy constructor
	 * 
	 * @param tag
	 */
	public OrderTag(OrderTag tag) {
		super(tag);
	}

}
