/**
 * 
 */
package org.iita.inventory.printing;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Version;

/**
 * Label printer configuration.
 * 
 * @author mobreza
 * 
 */
@Entity
public class BalanceScaleInfo {
	private Integer id;
	private Integer version;
	private String host;
	private int port;

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
	 * @return the host
	 */
	@Column(nullable = false, length = 200)
	public String getHost() {
		return this.host;
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
	 * @param host the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}
	
	/**
	 * @return the port
	 */
	public int getPort() {
		return this.port;
	}
	
	/**
	 * @param port the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.host + " @ " + this.port + " (id=" + this.id + ")";
	}
}
