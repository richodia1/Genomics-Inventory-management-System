/**
 * 
 */
package org.iita.inventory.service;

import java.util.List;

import org.iita.inventory.barcode.BarcodePolicy;
import org.iita.inventory.barcode.BarcodingException;
import org.iita.inventory.model.BarCode;
import org.iita.inventory.model.Lot;

/**
 * Barcoding service needs to provide all methods of {@link BarcodePolicy} interface and some additional ones.
 * 
 * @author mobreza
 * 
 */
public interface BarcodingService extends BarcodePolicy {
	/**
	 * Ensure all lots in the list have barcodes assigned
	 * 
	 * @see BarCode
	 * @param selectedLots list of lots
	 * @throws BarcodingException if a barcode cannot be assigned
	 */
	void ensureBarCode(List<? extends Lot> selectedLots) throws BarcodingException;
}
