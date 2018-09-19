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
public class LotVariableUpdate {
	/** Record ID */
	private Long id;
	/** Lot ID */
	private LotVariable lotVariable;
	/** Original quantity */
	private Double originalQty;
	/** Quantity */
	private Double quantity;
	/** Scale */
	private Date variableDate;
	private Lot lot;
	private Variables variable;
	
	/** Quantity update description (the parent record) */
	private LotVariableUpdateBulk description;
	
	/**
	 * @return the description
	 */
	@ManyToOne(optional = false)
	public LotVariableUpdateBulk getDescription() {
		return this.description;
	}
	
	/**
	 * @param description the description to set
	 */
	public void setDescription(LotVariableUpdateBulk description) {
		this.description = description;
	}

	/**
	 * @return the variableDate
	 */
	public Date getVariableDate() {
		return variableDate;
	}

	/**
	 * @param variableDate the variableDate to set
	 */
	public void setVariableDate(Date variableDate) {
		this.variableDate = variableDate;
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
	 * @return the variable
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, optional = false)
	@JoinColumn(name = "variableId")
	public Variables getVariable() {
		return variable;
	}

	/**
	 * @param variable the variable to set
	 */
	public void setVariable(Variables variable) {
		this.variable = variable;
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
	 * @return the lotVariable
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, optional = false)
	@JoinColumn(name = "lotVariableId")
	public LotVariable getLotVariable() {
		return this.lotVariable;
	}

	/**
	 * @param lotVariable the lotVariable to set
	 */
	public void setLotVariable(LotVariable lotVariable) {
		this.lotVariable = lotVariable;
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
