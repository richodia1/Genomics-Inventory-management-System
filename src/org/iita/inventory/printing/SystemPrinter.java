/**
 * 
 */
package org.iita.inventory.printing;

import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.standard.JobName;

import org.iita.inventory.label.PrinterException;

/**
 * Use Java Printing Service to print labels. The printer needs to be registered on the server running the application.
 * 
 * @author mobreza
 * 
 */
public class SystemPrinter {

	/**
	 * Name used with Java Printing Service for print jobs
	 */
	private static final String PRINTJOB_NAME = "Genebank barcodes";
	private SystemPrinterInfo printer;

	/**
	 * @param selectedPrinter
	 */
	public SystemPrinter(SystemPrinterInfo selectedPrinter) {
		this.printer = selectedPrinter;
	}

	/**
	 * @param string
	 * @throws PrinterException Printing related exceptions
	 */
	public void print(String rawData) throws PrinterException {
		javax.print.attribute.PrintRequestAttributeSet attributes = new javax.print.attribute.HashPrintRequestAttributeSet();
		attributes.add(new JobName(PRINTJOB_NAME, null));

		DocFlavor flavDoc = DocFlavor.STRING.TEXT_PLAIN;
		PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
		if (services == null) {
			throw new PrinterException("No printing services on server.");
		}
		PrintService service = null;

		for (PrintService ps : services) {
			System.out.println(ps.getName());

			if (ps.getName().equals(this.printer.getPrintServiceName())) {
				service = ps;
				break;
			}
		}

		if (service != null) {
			SimpleDoc prnDoc = new SimpleDoc(rawData, flavDoc, null);
			DocPrintJob prnJob = service.createPrintJob();
			try {
				prnJob.print(prnDoc, attributes);
			} catch (PrintException e) {
				throw new PrinterException(e.getMessage(), e);
			}
		} else {
			throw new PrinterException("No system printer '" + this.printer.getPrintServiceName() + "' on server.");
		}
	}

}
