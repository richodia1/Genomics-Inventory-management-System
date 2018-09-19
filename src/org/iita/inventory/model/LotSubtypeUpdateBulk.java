/**
 * 
 */
package org.iita.inventory.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.iita.entity.VersionedEntity;
import org.iita.util.NaturalOrderComparator;

/**
 * @author KOraegbunam
 *
 */
@Entity
public class LotSubtypeUpdateBulk extends VersionedEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6549195011358495088L;
	/** Status of bulk quantity update */
	private int status = 0;
	/** Title */
	private String title;
	/** Description */
	private String description;
	/** Date of update */
	private Date date = new Date();
	/** Transaction type */
	private Transaction2.Type transactionType = Transaction2.Type.OUT;
	/** Transaction type name */
	private String transactionSubtype;
	/** Lot variable update */
	private List<LotSubtypeUpdate> lotSubtypes = new ArrayList<LotSubtypeUpdate>();
	/** Does it affect Lot quantity in inventory */
	boolean affectingInventory = true;
	
	/**
	 * @return the date
	 */
	@Column(nullable = false)
	public Date getDate() {
		return this.date;
	}

	/**
	 * @return the description
	 */
	@Lob
	public String getDescription() {
		return this.description;
	}

	/**
	 * @return the lot
	 */
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "description")
	public List<LotSubtypeUpdate> getLotSubtypes() {
		return this.lotSubtypes;
	}
	
	/**
	 * @param lotVariables the lotVariables to set
	 */
	public void setLotSubtypes(List<LotSubtypeUpdate> lotSubtypes) {
		this.lotSubtypes = lotSubtypes;
	}
	
	/**
	 * @return the title
	 */
	@Column(nullable = false, length = 250)
	public String getTitle() {
		return this.title;
	}

	/**
	 * 0 - READY, 1 - COMMITED, 100 -- FINALIZED
	 * 
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}
	
	/**
	 * 0 - READY, 1 - COMMITED
	 * 
	 * @return Status of the bulk update.
	 */
	public int getStatus() {
		return this.status;
	}
	
	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	@Transient
	public List<LotSubtypeUpdate> getSortedLots() {
		// sort collection
		try {
			Collections.sort(this.getLotSubtypes(), new NaturalOrderComparator<LotSubtypeUpdate>() {
				@Override
				public int compare(LotSubtypeUpdate o1, LotSubtypeUpdate o2) {
					String s1 = "", s2 = "";
					try {
						s1 = o1.getLot().getItem().getName().toLowerCase();
					} catch (Exception e) {

					}
					try {
						s2 = o2.getLot().getItem().getName().toLowerCase();
					} catch (Exception e) {
						e.printStackTrace();
					}
					return naturalCompare(s1, s2);
				}
			});
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return this.getLotSubtypes();
	}

	public static String getNaturalName(Object a1) {
		if (a1 instanceof QuantityUpdate) {
			return ((QuantityUpdate) a1).getLot().getItem().getName();
		} else
			return a1.toString();
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Get transaction type
	 * 
	 * @return the transactionType
	 */
	@Enumerated(EnumType.STRING)
	public Transaction2.Type getTransactionType() {
		return this.transactionType;
	}

	/**
	 * Set transaction type
	 * 
	 * @param transactionType the transactionType to set
	 */
	public void setTransactionType(Transaction2.Type transactionType) {
		this.transactionType = transactionType;
	}

	public void setTransactionSubtype(String transactionSubtype) {
		this.transactionSubtype = transactionSubtype;
	}

	/**
	 * Get name used for Transaction.name
	 * 
	 * @see Transaction2#getName()
	 * @see Transaction2#setName()
	 * @return the transaction type name
	 */
	public String getTransactionSubtype() {
		return transactionSubtype;
	}

	/**
	 * @return the affectingInventory
	 */
	@Column(columnDefinition = "BIT DEFAULT 1")
	public boolean isAffectingInventory() {
		return this.affectingInventory;
	}

	/**
	 * @param affectingInventory the affectingInventory to set
	 */
	public void setAffectingInventory(boolean affectingInventory) {
		this.affectingInventory = affectingInventory;
	}
}
