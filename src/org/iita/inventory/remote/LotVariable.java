/**
 * 
 */
package org.iita.inventory.remote;

import java.io.Serializable;
import java.util.Date;

/**
 * @author KOraegbunam
 *
 */
public class LotVariable implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private  Long id;
	private Date lastUpdated;
	private String lastUpdatedBy;
	private Date variabledate;
	private Long lotId;
	private Long variableId;
	private String variableName;
	private double quantity;
	private Long barCode;
	private String itemName;
	private String lotLine;
	
	/**
	 * @return the id
	 */
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
	 * @return the lastUpdated
	 */
	public Date getLastUpdated() {
		return lastUpdated;
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
		return lastUpdatedBy;
	}
	/**
	 * @param lastUpdatedBy the lastUpdatedBy to set
	 */
	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}
	/**
	 * @return the variabledate
	 */
	public Date getVariabledate() {
		return variabledate;
	}
	/**
	 * @param variabledate the variabledate to set
	 */
	public void setVariabledate(Date variabledate) {
		this.variabledate = variabledate;
	}
	/**
	 * @return the lotId
	 */
	public Long getLotId() {
		return lotId;
	}
	/**
	 * @param lotId the lotId to set
	 */
	public void setLotId(Long lotId) {
		this.lotId = lotId;
	}
	/**
	 * @return the variableId
	 */
	public Long getVariableId() {
		return variableId;
	}
	/**
	 * @param variableId the variableId to set
	 */
	public void setVariableId(Long variableId) {
		this.variableId = variableId; 
	}
	/**
	 * @return the variableName
	 */
	public String getVariableName() {
		return variableName;
	}
	/**
	 * @param variableName the variableName to set
	 */
	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}
	/**
	 * @return the quantity
	 */
	public double getQuantity() {
		return quantity;
	}
	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}
	/**
	 * @return the barCode
	 */
	public Long getBarCode() {
		return barCode;
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
		return itemName;
	}
	/**
	 * @param itemName the itemName to set
	 */
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	
	/**
	 * @return the lotLine
	 */
	public String getLotLine() {
		return lotLine;
	}
	/**
	 * @param lotLine the lotLine to set
	 */
	public void setLotLine(String lotLine) {
		this.lotLine = lotLine;
	}
}
