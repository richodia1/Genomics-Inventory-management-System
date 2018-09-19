/**
 * 
 */
package org.iita.inventory.printing;

import org.iita.inventory.label.PrinterException;

/**
 * {@link PrintFactory} provides the logic to send raw data to the selected {@link PrinterInfo} object. Depending on the type of PrinterInfo, the appropriate
 * printing mechanism will be invoked. Currently only supports sending data to {@link SystemPrinter} when {@link SystemPrinterInfo} type is selected.
 * 
 * @author mobreza
 * 
 */
public class PrintFactory {
	/**
	 * Initialize a driver for printer and print!
	 * 
	 * @param selectedPrinter
	 * @param string
	 * @throws PrinterException
	 */
	public static void print(PrinterInfo selectedPrinter, String rawData) throws PrinterException {
		if (selectedPrinter instanceof SystemPrinterInfo) {
			new SystemPrinter((SystemPrinterInfo) selectedPrinter).print(rawData);
		} else {
			org.apache.commons.logging.LogFactory.getLog(PrintFactory.class).error(
					"Don't know how to print to " + selectedPrinter.getName() + " of type " + selectedPrinter.getClass().getName());
			throw new PrinterException("Don't know how to print to " + selectedPrinter.getName() + " of type " + selectedPrinter.getClass().getName());
		}
	}

}
