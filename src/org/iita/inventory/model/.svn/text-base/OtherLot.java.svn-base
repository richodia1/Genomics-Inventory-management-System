/**
 * inventory.Struts Dec 14, 2010
 */
package org.iita.inventory.model;

import javax.persistence.Entity;
import javax.persistence.Transient;

import org.hibernate.search.annotations.Indexed;

/**
 * A more "generic" lot entity
 * 
 * @author mobreza
 */
@Entity
@Indexed
public class OtherLot extends Lot {
	private static final long serialVersionUID = -4426812058101476946L;

	private static final String[] FIELD_UOMS = new String[] { "tuber", "plant", "hill", "pcs", "g" };
	

	@Override
	@Transient
	public String[] getUoms() {
		return FIELD_UOMS;
	}
}
