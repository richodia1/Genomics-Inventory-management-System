package org.iita.inventory.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * Genebank Order item includes source Lot information.
 * 
 * @author mobreza
 */
@Entity
public class GenebankOrderItem extends OrderItem {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 7947481844212130646L;

	/** The lot. */
	private Lot lot;

	private GenebankOrder order;

	/**
	 * Gets the lot.
	 * 
	 * @return the lot
	 */
	@ManyToOne(cascade = {})
	public Lot getLot() {
		return lot;
	}

	/**
	 * Sets the lot.
	 * 
	 * @param lot the new lot
	 */
	public void setLot(Lot lot) {
		this.lot = lot;
	}

	@Override
	@ManyToOne(cascade = {}, optional = false)
	public GenebankOrder getOrder() {
		return this.order;
	}

	public void setOrder(GenebankOrder order) {
		this.order=order;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("%1$s %2$f %3$s", getItemName(), getQuantity(), getScale());
	}
}
