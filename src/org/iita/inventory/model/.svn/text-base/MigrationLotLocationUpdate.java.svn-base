/**
 * 
 */
package org.iita.inventory.model;

import javax.persistence.CascadeType;
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
public class MigrationLotLocationUpdate {
	private Long id;
	private String toLocation;
	private Long fromLocation;
	private Long toLocationId;
	private Lot lot;
	/** lot location update description (the parent record) */
	private MigrationLotLocationUpdateBulk description;
	
	/**
	 * @return the id
	 */
	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return the fromLocation
	 */
	public Long getFromLocation() {
		return fromLocation;
	}
	/**
	 * @param fromLocation the fromLocation to set
	 */
	public void setFromLocation(Long fromLocation) {
		this.fromLocation = fromLocation;
	}
	/**
	 * @return the toLocation
	 */
	public String getToLocation() {
		return toLocation;
	}
	/**
	 * @param toLocation the toLocation to set
	 */
	public void setToLocation(String toLocation) {
		this.toLocation = toLocation;
	}
	
	/**
	 * @return the toLocationId
	 */
	public Long getToLocationId() {
		return toLocationId;
	}

	/**
	 * @param toLocationId the toLocationId to set
	 */
	public void setToLocationId(Long toLocationId) {
		this.toLocationId = toLocationId;
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
	 * @return the description
	 */
	@ManyToOne(optional = false)
	public MigrationLotLocationUpdateBulk getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(MigrationLotLocationUpdateBulk description) {
		this.description = description;
	}
}
