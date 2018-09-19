/**
 * inventory.Struts Jun 29, 2010
 */
package org.iita.inventory.remote;

import java.io.Serializable;
import java.util.Date;

/**
 * Lot information entity provided to/by remote systems.
 * 
 * @author mobreza
 */
public class Lot implements Serializable {
	private static final long serialVersionUID = -5149423288864173924L;

	// identifying
	private Long id;
	private Long barCode;
	private String itemName;

	// qty
	private double quantity;
	private String scale;

	// location
	private String location;
	private Long locationId;
	private String locationDetail;

	// audit
	private String createdBy;
	private Date lastUpdated;
	private String lastUpdatedBy;
	private String line;
	private Date introductionDate;
	private String origin;
	
	private String container;
	private Date subCulturedDate;
	private Boolean virusFree=false;

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
	 * @return the barCode
	 */
	public Long getBarCode() {
		return this.barCode;
	}

	/**
	 * @param barCode the barCode to set
	 */
	public void setBarCode(Long barCode) {
		this.barCode = barCode;
	}

	/**
	 * @return the itemName
	 */
	public String getItemName() {
		return this.itemName;
	}

	/**
	 * @param itemName the itemName to set
	 */
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	/**
	 * @return the quantity
	 */
	public double getQuantity() {
		return this.quantity;
	}

	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	/**
	 * @return the scale
	 */
	public String getScale() {
		return this.scale;
	}

	/**
	 * @param scale the scale to set
	 */
	public void setScale(String scale) {
		this.scale = scale;
	}

	/**
	 * @return the location
	 */
	public String getLocation() {
		return this.location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * @return the locationDetail
	 */
	public String getLocationDetail() {
		return this.locationDetail;
	}

	/**
	 * @param locationDetail the locationDetail to set
	 */
	public void setLocationDetail(String locationDetail) {
		this.locationDetail = locationDetail;
	}

	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * @return the createdBy
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * @return the lastUpdated
	 */
	public Date getLastUpdated() {
		return this.lastUpdated;
	}

	/**
	 * @param lastUpdated the lastUpdated to set
	 */
	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	/**
	 * @return the lastUpdatedBy
	 */
	public String getLastUpdatedBy() {
		return this.lastUpdatedBy;
	}

	/**
	 * @param lastUpdatedBy the lastUpdatedBy to set
	 */
	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	/**
	 * @param line the line to set
	 */
	public void setLine(String line) {
		this.line = line;
	}

	/**
	 * @return the line
	 */
	public String getLine() {
		return line;
	}

	/**
	 * @param introductionDate the introductionDate to set
	 */
	public void setIntroductionDate(Date introductionDate) {
		this.introductionDate = introductionDate;
	}

	/**
	 * @return the introductionDate
	 */
	public Date getIntroductionDate() {
		return introductionDate;
	}

	/**
	 * @param origin the origin to set
	 */
	public void setOrigin(String origin) {
		this.origin = origin;
	}

	/**
	 * @return the origin
	 */
	public String getOrigin() {
		return origin;
	}

	/**
	 * @param container the container to set
	 */
	public void setContainer(String container) {
		this.container = container;
	}

	/**
	 * @return the container
	 */
	public String getContainer() {
		return container;
	}

	/**
	 * @param subCulturedDate the subCulturedDate to set
	 */
	public void setSubCulturedDate(Date subCulturedDate) {
		this.subCulturedDate = subCulturedDate;
	}

	/**
	 * @return the subCulturedDate
	 */
	public Date getSubCulturedDate() {
		return subCulturedDate;
	}

	/**
	 * @param virusFree the virusFree to set
	 */
	public void setVirusFree(Boolean virusFree) {
		this.virusFree = virusFree;
	}

	/**
	 * @return the virusFree
	 */
	public Boolean isVirusFree() {
		return virusFree;
	}

	/**
	 * @param locationId the locationId to set
	 */
	public void setLocationId(Long locationId) {
		this.locationId = locationId;
	}

	/**
	 * @return the locationId
	 */
	public Long getLocationId() {
		return locationId;
	}
}
