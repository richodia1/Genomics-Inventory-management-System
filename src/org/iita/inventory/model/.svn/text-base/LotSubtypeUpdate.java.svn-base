/**
 * 
 */
package org.iita.inventory.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * @author KOraegbunam
 *
 */
@Entity
public class LotSubtypeUpdate {
	/** Record ID */
	private Long id;
	/** Lot ID */
	/** Original quantity */
	private Double originalQty;
	/** Quantity */
	private Double quantity;
	private Date date;
	private Lot lot;
	
	/** Quantity update description (the parent record) */
	private LotSubtypeUpdateBulk description;
	
	/**
	 * @return the description
	 */
	@ManyToOne(optional = false)
	public LotSubtypeUpdateBulk getDescription() {
		return this.description;
	}
	
	/**
	 * @param description the description to set
	 */
	public void setDescription(LotSubtypeUpdateBulk description) {
		this.description = description;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return the lot
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, optional = false)
	@JoinColumn(name = "lotId")
	public Lot getLot() {
		return lot;
	}

	/**
	 * @param lot the lot to set
	 */
	public void setLot(Lot lot) {
		this.lot = lot;
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

}
