/**
 * inventory.Struts Nov 19, 2010
 */
package org.iita.inventory.remote;

import java.io.Serializable;

/**
 * @author mobreza
 */
public class LotSubtypeQuantity implements Serializable {
	private static final long serialVersionUID = -6258153981708708991L;
	private long id;
	private double quantity;
	private long LotId;
	private String subtype;
	private String updatedBy;

	/**
	 * @return the id
	 */
	public long getId() {
		return this.id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the quantity
	 */
	public double getQuantity() {
		return this.quantity;
	}

	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	/**
	 * @param lotId the lotId to set
	 */
	public void setLotId(long lotId) {
		LotId = lotId;
	}

	/**
	 * @return the lotId
	 */
	public long getLotId() {
		return LotId;
	}

	/**
	 * @param subtype the subtype to set
	 */
	public void setSubtype(String subtype) {
		this.subtype = subtype;
	}

	/**
	 * @return the subtype
	 */
	public String getSubtype() {
		return subtype;
	}

	/**
	 * @param updatedBy the updatedBy to set
	 */
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	/**
	 * @return the updatedBy
	 */
	public String getUpdatedBy() {
		return updatedBy;
	}
}
