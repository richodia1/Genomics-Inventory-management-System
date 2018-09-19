/**
 * 
 */
package org.iita.inventory.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Version;

/**
 * @author mobreza
 * 
 *         Quantity tracking transaction
 */
@Entity
public class Transaction {

	/**
	 * Transaction type allows for better analysis of transaction data.
	 * 
	 * @author mobreza
	 */
	public enum Type {
		/*
		 * If adding types, they must be added to the end of the list and must have consecutive indexes as Hibernate stores the ordinal value of enum!
		 */
		AUTOMATIC, REWEIGHED, NECROSIS, CONTAMINATION, DISTRIBUTION, LOSS, REGENERATION, INTERNALDISTRIBUTION, ROLLBACK, SUBCULTURING, SUBCULTURED;

		/**
		 * Return a friendly (english) name for the transaction type
		 */
		public String toString() {
			switch (this) {
			case AUTOMATIC:
				return "System generated";
			case REWEIGHED:
				return "Re-weighed";
			case NECROSIS:
				return "Necrosis";
			case CONTAMINATION:
				return "Contamination";
			case DISTRIBUTION:
				return "Distribution";
			case LOSS:
				return "Loss";
			case REGENERATION:
				return "Regeneration";
			case INTERNALDISTRIBUTION:
				return "Internal";
			case ROLLBACK:
				return "Rollback";
			case SUBCULTURING:
				return "For Subculturing";
			case SUBCULTURED:
				return "Subcultured";
			}
			return this.name();
		}
	}

	/** ID */
	@Id
	@GeneratedValue
	private Long id;

	/** Version */
	@Version
	private Integer version;

	/** Lot */
	@ManyToOne(cascade = CascadeType.ALL, optional = false)
	@JoinColumn(name = "lot")
	private Lot lot;

	/** Date of transaction */
	@Column(nullable = false)
	private Date transactionDate;

	/** Transaction type */
	private Type type = Type.AUTOMATIC;

	/** Previous quantity */
	@Column(nullable = false)
	private Double oldQuantity;

	/** Previous scale */
	@Column(nullable = false, length = 10)
	private String oldScale;

	/** New quantity */
	@Column(nullable = false)
	private Double newQuantity;

	/** New scale */
	@Column(nullable = false, length = 10)
	private String newScale;

	/** Person responsible for change */
	@Column(nullable = true, length = 100)
	private String userName;

	/** Notes */
	@Column(nullable = true)
	@Lob
	private String notes;

	/**
	 * @return the id
	 */
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
	 * @return the version
	 */
	public Integer getVersion() {
		return this.version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(Integer version) {
		this.version = version;
	}

	/**
	 * @return the transactionDate
	 */
	public Date getTransactionDate() {
		return this.transactionDate;
	}

	/**
	 * @param transactionDate the transactionDate to set
	 */
	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	/**
	 * @return the oldQuantity
	 */
	public Double getOldQuantity() {
		return this.oldQuantity;
	}

	/**
	 * @param oldQuantity the oldQuantity to set
	 */
	public void setOldQuantity(Double oldQuantity) {
		this.oldQuantity = oldQuantity;
	}

	/**
	 * @return the oldScale
	 */
	public String getOldScale() {
		return this.oldScale;
	}

	/**
	 * @param oldScale the oldScale to set
	 */
	public void setOldScale(String oldScale) {
		this.oldScale = oldScale;
	}

	/**
	 * @return the newQuantity
	 */
	public Double getNewQuantity() {
		return this.newQuantity;
	}

	/**
	 * @param newQuantity the newQuantity to set
	 */
	public void setNewQuantity(Double newQuantity) {
		this.newQuantity = newQuantity;
	}

	/**
	 * @return the newScale
	 */
	public String getNewScale() {
		return this.newScale;
	}

	/**
	 * @param newScale the newScale to set
	 */
	public void setNewScale(String newScale) {
		this.newScale = newScale;
	}

	/**
	 * @return the notes
	 */
	public String getNotes() {
		return this.notes;
	}

	/**
	 * @param notes the notes to set
	 */
	public void setNotes(String notes) {
		this.notes = notes;
	}

	/**
	 * @return the lot
	 */
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
	 * @return the userName
	 */
	public String getUserName() {
		return this.userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the type
	 */
	@Enumerated
	public Type getType() {
		return this.type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(Type type) {
		this.type = type;
	}

}
