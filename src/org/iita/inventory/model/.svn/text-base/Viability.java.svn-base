package org.iita.inventory.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.iita.entity.MySqlBaseEntity;

/**
 * Viability shows the results of performed viability testing on individual lots.
 * 
 * @author mobreza
 */
@Entity
public class Viability extends MySqlBaseEntity {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -5655559423711284955L;

	/** The lot. */
	private Lot lot;

	/** The test date. */
	private Date testDate;

	/** The viability. */
	private double viability;

	/**
	 * Gets the lot.
	 * 
	 * @return the lot
	 */
	@ManyToOne(cascade = {}, optional = false)
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

	/**
	 * Gets the test date.
	 * 
	 * @return the test date
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	public Date getTestDate() {
		return testDate;
	}

	/**
	 * Sets the test date.
	 * 
	 * @param testDate the new test date
	 */
	public void setTestDate(Date testDate) {
		this.testDate = testDate;
	}

	/**
	 * Gets the viability.
	 * 
	 * @return the viability
	 */
	public double getViability() {
		return viability;
	}

	/**
	 * Sets the viability.
	 * 
	 * @param viability the new viability
	 */
	public void setViability(double viability) {
		this.viability = viability;
	}
}
