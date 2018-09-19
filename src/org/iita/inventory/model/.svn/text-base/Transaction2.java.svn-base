package org.iita.inventory.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.iita.entity.MySqlBaseEntity;

/**
 * New transaction model for inventory system. Supporting only IN/OUT transactions with brief description.
 * 
 * @author mobreza
 * 
 */
@Entity(name = "Transaction2")
public class Transaction2 extends MySqlBaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1335715276709739549L;

	/**
	 * The two supported transaction types: IN and OUT
	 */
	public enum Type {
		OUT, IN, RSET
	}

	/**
	 * Transaction source determines how to use the {@link Transaction2#rel} field.
	 */
	public enum Source {
		SYSTEM, BULK;
	}

	/** Lot of transaction */
	private Lot lot;
	/** Date of transaction */
	private Date date;
	/** Quantity */
	private Double quantity;
	/** Scale */
	private String scale;
	/** Transaction type name */
	private String subtype;
	/** Source of transaction */
	private Source source = Source.SYSTEM;
	/** Source object ID */
	private Long rel;

	

	/**
	 * Lot for which the transaction was created
	 */
	@ManyToOne(optional = false)
	@JoinColumn(name = "lot")
	public Lot getLot() {
		return lot;
	}

	/**
	 * Set lot for which the transaction is created
	 * 
	 * @param lot the lot
	 */
	public void setLot(Lot lot) {
		this.lot = lot;
	}

	/**
	 * Get transaction type based on the value of quantity. If quantity is negative, the transaction is OUT (outgoing) otherwise it is IN (incoming).
	 * 
	 * @see Type
	 */
	@Transient
	public Type getType() {
		return this.quantity < 0 ? Type.OUT : Type.IN;
	}

	/**
	 * Get date of transaction.
	 * 
	 * @return date of transaction
	 */
	@Temporal(TemporalType.TIMESTAMP)
	public Date getDate() {
		return date;
	}

	/**
	 * Set date of transaction. Defaults to now.
	 * 
	 * @param date date of transaction
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * The change in lot quantity. If the value is negative (<0) the transaction is OUT (outgoing) otherwise it is IN (incoming).
	 * 
	 * @return quantity
	 */
	@Column(nullable = false, updatable = false)
	public Double getQuantity() {
		return quantity;
	}

	/**
	 * Set quantity of transaction.
	 * 
	 * @param quantity
	 * @throws TransactionException
	 */
	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	/**
	 * Get scale / unit of measurement
	 * 
	 * @return scale
	 */
	@Column(nullable = false, length = 20, updatable = false)
	public String getScale() {
		return scale;
	}

	/**
	 * Set unit of measurement
	 * 
	 * @param scale
	 */
	public void setScale(String scale) {
		this.scale = scale;
	}

	/**
	 * Get short transaction description.
	 * 
	 * @return short transaction description
	 */
	@Column(updatable = false, length = 50, nullable = false)
	public String getSubtype() {
		return subtype;
	}

	/**
	 * Set transaction description.
	 * 
	 * @param name the description
	 */
	public void setSubtype(String name) {
		this.subtype = name;
	}

	/**
	 * Get transaction source
	 * 
	 * @return source
	 */
	@Enumerated(EnumType.STRING)
	public Source getSource() {
		return source;
	}

	/**
	 * Set transaction source
	 * 
	 * @param source
	 */
	public void setSource(Source source) {
		this.source = source;
	}

	/**
	 * Get source object's ID
	 * 
	 * @return source object's ID
	 */
	public Long getRel() {
		return rel;
	}

	/**
	 * Set source object's ID
	 */
	public void setRel(Long rel) {
		this.rel = rel;
	}
}
