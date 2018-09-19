/**
 * 
 */
package org.iita.inventory.barcode;

import org.iita.inventory.model.BarCode;
import org.iita.inventory.model.Lot;

/**
 * Interface defines only one method: {@link #assignBarCode(Lot)}. The implementing class will
 * 
 * @author mobreza
 * 
 */
public interface BarcodePolicy {
	/**
	 * Assign a barcode to the Lot
	 * 
	 * @param lot
	 * @return barcode assigned to the lot
	 * @throws BarcodingException
	 */
	BarCode assignBarCode(Lot lot) throws BarcodingException;

	/**
	 * Unassign the barcode from Lot
	 * 
	 * @param lot
	 */
	void removeBarCode(Lot lot);
}
