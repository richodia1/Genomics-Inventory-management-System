/**
 * 
 */
package org.iita.inventory.service;

/**
 * @author mobreza
 * 
 */
public class LocationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public LocationException() {

	}

	/**
	 * @param arg0
	 */
	public LocationException(String arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 */
	public LocationException(Throwable arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public LocationException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
