/**
 * 
 */
package org.iita.inventory.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.iita.inventory.barcode.BarcodePolicy;

/**
 * Class holding information about barcode identifiers in relation to individual lots. The barcoding policy ({@link BarcodePolicy}) used by the inventory system
 * allows a barcode that is no longer associated with a lot to be eventually reused for another lot. As the barcode print width is limited due to label size,
 * only up to 8 digits are used for barcode identifier (99999999 maximum).
 * 
 * @see BarCodeManager
 * @author mobreza
 */
@Entity
public class BarCode {
	/** Unique identifier */
	private Long id;

	/** Lot */
	private Lot lot;

	/** Date assigned */
	private Date dateAssigned;

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
	@OneToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "lotId", unique = true)
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
	 * @return the dateAssigned
	 */
	public Date getDateAssigned() {
		return this.dateAssigned;
	}

	/**
	 * @param dateAssigned the dateAssigned to set
	 */
	public void setDateAssigned(Date dateAssigned) {
		this.dateAssigned = dateAssigned;
	}
}
