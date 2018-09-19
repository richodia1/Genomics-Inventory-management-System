/**
 * 
 */
package org.iita.inventory.remote;

import java.io.Serializable;

/**
 * @author KOraegbunam
 *
 */
public class Variables implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6793088252528432857L;
	private Long id;
	private String name;
	
	
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

}
