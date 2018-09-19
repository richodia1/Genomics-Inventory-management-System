/**
 * 
 */
package org.iita.inventory.label;

/**
 * Thrown when there are no printers available
 * 
 * @author mobreza
 */
public class NoPrinterException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5380967510296927861L;

	public NoPrinterException() {
		super();
	}

	public NoPrinterException(String message) {
		super(message);
	}

	public NoPrinterException(String message, Exception innerException) {
		super(message, innerException);
	}
}
