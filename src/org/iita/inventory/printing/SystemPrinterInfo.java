/**
 * 
 */
package org.iita.inventory.printing;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Java Printing Service printer.
 * 
 * @author mobreza
 * 
 */
@Entity
public class SystemPrinterInfo extends PrinterInfo {
	private String printServiceName;

	/**
	 * @return the printServiceName
	 */
	@Column(nullable = false, length = 200)
	public String getPrintServiceName() {
		return this.printServiceName;
	}

	/**
	 * @param printServiceName the printServiceName to set
	 */
	public void setPrintServiceName(String printServiceName) {
		this.printServiceName = printServiceName;
	}

}
