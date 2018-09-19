/**
 * 
 */
package org.iita.inventory.service;

import java.net.InetAddress;
import java.util.Collection;
import java.util.List;

import org.iita.inventory.label.PrinterException;
import org.iita.inventory.printing.PrinterInfo;

/**
 * Declare printer management methods.
 * 
 * @author mobreza
 */
public interface PrinterService {
	List<PrinterInfo> list();

	void store(PrinterInfo printerInfo);

	void delete(PrinterInfo pritnerInfo);

	PrinterInfo find(int id);

	/**
	 * Get printers available to specified remote host.
	 * 
	 * @param remoteAddress Remote host IP address
	 */
	Collection<PrinterInfo> getPrintersForIP(InetAddress remoteAddress, String sessionId);

	/**
	 * Get list of printers registered
	 * 
	 * @return
	 */
	public List<String> getSystemPrinters();

	/**
	 * Get list of all printers defined in application
	 * 
	 * @return list of printers defined in application
	 */
	List<PrinterInfo> getPrinters();

	/**
	 * Select the printer for the session
	 * 
	 * @param sessionId Session ID
	 * @param printer Selected printer
	 * @throws PrinterException 
	 */
	void selectPrinter(String sessionId, PrinterInfo printer) throws PrinterException;

	void deletePrinter(Integer id);
}
