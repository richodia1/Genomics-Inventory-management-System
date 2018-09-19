/**
 * 
 */
package org.iita.inventory.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import org.iita.entity.MySqlBaseEntity;

/**
 * Lot migration record stores data relevant to track lot location changes.
 * 
 * @author mobreza
 * 
 */
@Entity
public class Migration extends MySqlBaseEntity {
	private static final long serialVersionUID = -3410493982761419083L;

	/** Lot */
	private Lot lot;

	/** Migration date+time */
	private Date migrationDate;

	/** Old location id */
	private Long oldLocationId;

	/** Old location name */
	private String oldLocationName;

	/** New location */
	private Long newLocationId;

	/** New location name */
	private String newLocationName;

	private String reason;

	/**
	 * @return the lot
	 */
	@ManyToOne(optional = false, cascade = CascadeType.ALL)
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
	 * @return the migrationDate
	 */
	@Column(nullable = false)
	public Date getMigrationDate() {
		return this.migrationDate;
	}

	/**
	 * @param migrationDate the migrationDate to set
	 */
	public void setMigrationDate(Date migrationDate) {
		this.migrationDate = migrationDate;
	}

	/**
	 * @return the oldLocationId
	 */
	@Column(nullable = true)
	public Long getOldLocationId() {
		return this.oldLocationId;
	}

	/**
	 * @param oldLocationId the oldLocationId to set
	 */
	public void setOldLocationId(Long oldLocationId) {
		this.oldLocationId = oldLocationId;
	}

	/**
	 * @return the oldLocationName
	 */
	@Column(nullable = true)
	public String getOldLocationName() {
		return this.oldLocationName;
	}

	/**
	 * @param oldLocationName the oldLocationName to set
	 */
	public void setOldLocationName(String oldLocationName) {
		this.oldLocationName = oldLocationName;
	}

	/**
	 * @return the newLocationId
	 */
	@Column(nullable = false)
	public Long getNewLocationId() {
		return this.newLocationId;
	}

	/**
	 * @param newLocationId the newLocationId to set
	 */
	public void setNewLocationId(Long newLocationId) {
		this.newLocationId = newLocationId;
	}

	/**
	 * @return the newLocationName
	 */
	@Column(nullable = false)
	public String getNewLocationName() {
		return this.newLocationName;
	}

	/**
	 * @param newLocationName the newLocationName to set
	 */
	public void setNewLocationName(String newLocationName) {
		this.newLocationName = newLocationName;
	}

	/**
	 * @return the reason
	 */
	@Lob
	public String getReason() {
		return this.reason;
	}

	/**
	 * @param reason the reason to set
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}
}
