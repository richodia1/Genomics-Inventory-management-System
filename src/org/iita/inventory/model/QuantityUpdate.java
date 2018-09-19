/**
 * 
 */
package org.iita.inventory.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * StockUpdate contains information about the quantity and scale of stock change for a specific lot. StockUpdate records are grouped with StockUpdateDescription
 * that holds general information about the update (reason, type of stock update, date).
 * 
 * @author mobreza
 * 
 */
@Entity
public class QuantityUpdate {
	/** Record ID */
	private Long id;
	/** Lot ID */
	private Lot lot;
	/** Original quantity */
	private Double originalQty;
	/** Quantity */
	private Double quantity;
	/** Scale */
	private String scale;
	/** Quantity update description (the parent record) */
	private QuantityUpdateBulk description;

	/**
	 * @return the description
	 */
	@ManyToOne(optional = false)
	public QuantityUpdateBulk getDescription() {
		return this.description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(QuantityUpdateBulk description) {
		this.description = description;
	}

	/**
	 * @return the id
	 */
	@Id
	@GeneratedValue
	public Long getId() {
		return this.id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the lot
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, optional = false)
	@JoinColumn(name = "lotId")
	public Lot getLot() {
		return this.lot;
	}

	/**
	 * @param lot the lot to set
	 */
	public void setLot(Lot lot) {
		this.lot = lot;
	}
	
	/**
	 * @return the originalQty
	 */
	public Double getOriginalQty() {
		return this.originalQty;
	}
	
	/**
	 * @param originalQty the originalQty to set
	 */
	public void setOriginalQty(Double originalQty) {
		this.originalQty = originalQty;
	}

	/**
	 * Is always a positive value.
	 * 
	 * @return the quantity
	 */
	@Column(nullable = false)
	public Double getQuantity() {
		return this.quantity;
	}

	/**
	 * Is always a positive value.
	 * 
	 * @param quantity the quantity to set
	 */
	public void setQuantity(Double quantity) {
		if (quantity < 0)
			throw new RuntimeException("Quantity cannot be a negative value");
		this.quantity = quantity;
	}

	/**
	 * @return the scale
	 */
	@Column(nullable = false)
	public String getScale() {
		return this.scale;
	}

	/**
	 * @param scale the scale to set
	 */
	public void setScale(String scale) {
		this.scale = scale;
	}
}
