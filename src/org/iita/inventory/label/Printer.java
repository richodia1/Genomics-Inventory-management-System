/**
 * 
 */
package org.iita.inventory.label;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Printing interface.
 * 
 * @author mobreza
 * 
 */
public interface Printer {
	/**
	 * Connect to printer
	 * 
	 * @return <code>true</code> if connection was established successfully.
	 * @throws Exception
	 */
	public boolean connect() throws PrinterException;

	/**
	 * Get stream where data can be written.
	 * 
	 * @return OutputStream
	 * @throws IOException
	 */
	public OutputStream getOutputStream() throws IOException;

	/**
	 * Close printer.
	 * 
	 * @throws IOException
	 * @throws Exception
	 */
	public void close() throws PrinterException;
}
