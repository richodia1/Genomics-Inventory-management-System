/**
 * inventory.Struts Jul 6, 2010
 */
package org.iita.inventory.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Transient;

import org.hibernate.search.annotations.Indexed;

/**
 * Field lot information.
 * 
 * @author mobreza
 */
@Entity
@Indexed
public class FieldLot extends Lot {
	private static final long serialVersionUID = -3435661259732860257L;
	/**
	 * 
	 */
	private static final String[] FIELD_UOMS = new String[] { "stem", "plant", "hill" };

	private Date plantingDate;
	private Double latitude;
	private Double longitude;
	private Date geoposDate;
	private Long pegNumber;

	/**
	 * Get planting date
	 * 
	 * @return the plantingDate
	 */
	public Date getPlantingDate() {
		return this.plantingDate;
	}

	/**
	 * Set planting date
	 * 
	 * @param plantingDate the plantingDate to set
	 */
	public void setPlantingDate(Date plantingDate) {
		this.plantingDate = plantingDate;
	}

	/**
	 * Get latitude
	 * 
	 * @return the latitude
	 */
	public Double getLatitude() {
		return this.latitude;
	}

	/**
	 * Set latitude
	 * 
	 * @param latitude the latitude to set
	 */
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	/**
	 * Get longitude
	 * 
	 * @return the longitude
	 */
	public Double getLongitude() {
		return this.longitude;
	}

	/**
	 * Set longitude
	 * 
	 * @param longitude the longitude to set
	 */
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	/**
	 * Get date of geopositioning
	 * 
	 * @return the geoposDate
	 */
	public Date getGeoposDate() {
		return this.geoposDate;
	}

	/**
	 * Set date of geopositioning
	 * 
	 * @param geoposDate the geoposDate to set
	 */
	public void setGeoposDate(Date geoposDate) {
		this.geoposDate = geoposDate;
	}
	
	/**
	 * @return the pegNumber
	 */
	public Long getPegNumber() {
		return this.pegNumber;
	}
	
	/**
	 * @param pegNumber the pegNumber to set
	 */
	public void setPegNumber(Long pegNumber) {
		this.pegNumber = pegNumber;
	}

	/**
	 * @see org.iita.inventory.model.Lot#copyFrom(org.iita.inventory.model.Lot)
	 */
	@Override
	public void copyFrom(Lot lot) {
		super.copyFrom(lot);
		if (lot instanceof FieldLot) {
			FieldLot other = (FieldLot) lot;
			this.geoposDate = other.geoposDate;
			this.latitude = other.latitude;
			this.longitude = other.longitude;
			this.plantingDate = other.plantingDate;
		}
	}

	@Override
	@Transient
	public String[] getUoms() {
		return FIELD_UOMS;
	}
}
