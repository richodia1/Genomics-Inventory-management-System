/**
 * inventory.Struts Oct 23, 2009
 */
package org.iita.inventory.service;

/**
 * @author mobreza
 * 
 */
public class InventoryException extends Exception {

	private static final long serialVersionUID = 8295326170119066579L;

	/**
	 * 
	 */
	public InventoryException() {

	}

	public InventoryException(String message) {
		super(message);
	}

	public InventoryException(Throwable t) {
		super(t);
	}

	public InventoryException(String message, Throwable t) {
		super(message, t);
	}
}
