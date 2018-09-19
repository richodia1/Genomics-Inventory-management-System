/**
 * inventory.Struts Jul 3, 2009
 */
package org.iita.inventory.importer;

/**
 * @author mobreza
 *
 */
public class LotReaderException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5771774500259284244L;

	/**
	 * 
	 */
	public LotReaderException() {
		
	}

	/**
	 * @param arg0
	 */
	public LotReaderException(String arg0) {
		super(arg0);
		
	}

	/**
	 * @param arg0
	 */
	public LotReaderException(Throwable arg0) {
		super(arg0);
		
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public LotReaderException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		
	}

}
