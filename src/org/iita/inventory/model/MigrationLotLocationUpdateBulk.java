package org.iita.inventory.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.iita.entity.VersionedEntity;

/**
 * @author KOraegbunam
 *
 */
@Entity
public class MigrationLotLocationUpdateBulk extends VersionedEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** Status of bulk quantity update */
	private int status = 0;
	/** Date of update */
	private Date date = new Date();
	/** Lot variable update */
	private List<MigrationLotLocationUpdate> lotLocationUpdate = new ArrayList<MigrationLotLocationUpdate>();
	/** Does it affect Lot quantity in inventory */
	boolean affectingInventory = true;

	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}
	
	/**
	 * @return the date
	 */
	@Column(nullable = false)
	public Date getDate() {
		return this.date;
	}

	/**
	 * @return the lot
	 */
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "description")
	public List<MigrationLotLocationUpdate> getLotLocationUpdate() {
		return this.lotLocationUpdate;
	}
	
	/**
	 * @param lot the lot to set
	 */
	public void setLotLocationUpdate(List<MigrationLotLocationUpdate> lotLocationUpdate) {
		this.lotLocationUpdate = lotLocationUpdate;
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

	public static String getNaturalName(Object a1) {
		if (a1 instanceof QuantityUpdate) {
			return ((QuantityUpdate) a1).getLot().getItem().getName();
		} else
			return a1.toString();
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
