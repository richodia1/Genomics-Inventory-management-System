/**
 * 
 */
package org.iita.inventory.label;

import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.Attribute;
import javax.print.attribute.standard.JobName;

/**
 * @author mobreza
 * 
 */
public class JavaPrinter {
	public static void main(String[] arg) {
		try {
			javax.print.attribute.PrintRequestAttributeSet attributes = new javax.print.attribute.HashPrintRequestAttributeSet();
			attributes.add(new JobName("Genebank barcode", null));

			DocFlavor flavDoc = DocFlavor.STRING.TEXT_PLAIN;
			PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
			if (services == null) {
				System.err.println("No services");
				return;
			}
			PrintService service = null;
			for (PrintService ps : services) {
				System.out.println(ps.getName());

				if (ps.getName().equals("ZM400")) {
					service = ps;
					break;
				}
			}

			if (service != null) {
				DocFlavor[] flavs = service.getSupportedDocFlavors();
				if (flavs.length == 0)
					System.out.println("No Flavors supported!!!");
				for (int i = 0; i < flavs.length; i++) {
					System.out.println(flavs[i].getMimeType() + "-->" + flavs[i].getRepresentationClassName());
				}

				Attribute[] serviceAttrs = service.getAttributes().toArray();
				for (Attribute serviceAttr : serviceAttrs) {
					System.out.println(serviceAttr.getName() + ": " + serviceAttr.toString());
				}

				String toPrint = "H^XA^FO180,40^ADN,40,16^FDTZm^FS^FO180,80^ADN,40,16^FD1269^FS^FO590,40^ADN,40,16^FDTZm^FS^FO590,80^ADN,40,16^FD1520^FS^PQ1,0,1,Y^XZ";

				SimpleDoc prnDoc = new SimpleDoc(toPrint, flavDoc, null);
				DocPrintJob prnJob = service.createPrintJob();
				prnJob.print(prnDoc, attributes);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
