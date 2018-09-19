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
public class NewLocationMigration implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long lotId;
	private String toLocation;
	private Long fromLocation;
	private Date lastUpdated;
	private String lastUpdatedBy;
	private Long parendId;
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
	 * @param parendId the parendId to set
	 */
	public void setParendId(Long parendId) {
		this.parendId = parendId;
	}
	/**
	 * @return the parendId
	 */
	public Long getParendId() {
		return parendId;
	}
}
