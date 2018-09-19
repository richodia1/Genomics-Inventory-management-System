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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.iita.entity.VersionedEntity;
import org.iita.util.NaturalOrderComparator;

/**
 * QuantityUpdateDescription contains the general information about quantities of germplasm distribution and/or regeneration. It serves as the parent object to
 * {@link QuantityUpdate}.
 * 
 * @author mobreza
 */
@Entity
public class QuantityUpdateBulk extends VersionedEntity {

	private static final long serialVersionUID = -1632367441150551054L;
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
	/** Lot update */
	private List<QuantityUpdate> lots;
	/** Does it affect Lot quantity in inventory */
	boolean affectingInventory = true;

	private GenebankOrder order;

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
	 * @return the lots
	 */
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "description")
	public List<QuantityUpdate> getLots() {
		return this.lots;
	}

	/**
	 * @return the title
	 */
	@Column(nullable = false, length = 450)
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

	/**
	 * @param lots the lots to set
	 */
	public void setLots(List<QuantityUpdate> lots) {
		this.lots = lots;
	}

	@Transient
	public List<QuantityUpdate> getSortedLots() {
		// sort collection
		try {
			Collections.sort(this.getLots(), new NaturalOrderComparator<QuantityUpdate>() {
				@Override
				public int compare(QuantityUpdate o1, QuantityUpdate o2) {
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
		return this.getLots();
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
	 * 0 - READY, 1 - COMMITED
	 * 
	 * @return Status of the bulk update.
	 */
	public int getStatus() {
		return this.status;
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

	/**
	 * @return the order
	 */
	@ManyToOne(cascade = {}, optional = true)
	public GenebankOrder getOrder() {
		return this.order;
	}

	/**
	 * @param order the order to set
	 */
	public void setOrder(GenebankOrder order) {
		this.order = order;
	}

	/**
	 * Return a list of {@link Lot} entities updated by this inventory update
	 * 
	 * @return
	 */
	@Transient
	public List<Lot> getLotList() {
		ArrayList<Lot> list = new ArrayList<Lot>();
		for (QuantityUpdate lotUpdate : this.getSortedLots()) {
			list.add(lotUpdate.getLot());
		}
		return list;
	}
}
