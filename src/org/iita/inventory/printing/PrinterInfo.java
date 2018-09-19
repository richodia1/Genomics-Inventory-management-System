/**
 * 
 */
package org.iita.inventory.printing;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Version;

/**
 * Label printer configuration.
 * 
 * @author mobreza
 * 
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class PrinterInfo {
	private Integer id;
	private Integer version;
	private LabelInfo labelInfo;
	private String name;
	private String location;
	private String allowedIPaddresses;

	/**
	 * @return the id
	 */
	@Id
	@GeneratedValue
	public Integer getId() {
		return this.id;
	}

	/**
	 * @return the version
	 */
	@Version
	public Integer getVersion() {
		return this.version;
	}

	/**
	 * @return the labelInfo
	 */
	@OneToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "labelInfo_id")
	public LabelInfo getLabelInfo() {
		return this.labelInfo;
	}

	/**
	 * @return the name
	 */
	@Column(nullable = false, length = 200)
	public String getName() {
		return this.name;
	}

	/**
	 * @return the location
	 */
	@Column(nullable = false, length = 200)
	public String getLocation() {
		return this.location;
	}

	/**
	 * @return the allowedIPaddresses
	 */
	@Lob
	public String getAllowedIPaddresses() {
		return this.allowedIPaddresses;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(Integer version) {
		this.version = version;
	}

	/**
	 * @param labelInfo the labelInfo to set
	 */
	public void setLabelInfo(LabelInfo labelInfo) {
		this.labelInfo = labelInfo;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * @param allowedIPaddresses the allowedIPaddresses to set
	 */
	public void setAllowedIPaddresses(String allowedIPaddresses) {
		this.allowedIPaddresses = allowedIPaddresses;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.name + " @ " + this.location + " (id=" + this.id + ")";
	}
}
