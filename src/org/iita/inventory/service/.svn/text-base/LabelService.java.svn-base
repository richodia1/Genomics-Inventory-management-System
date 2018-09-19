/**
 * 
 */
package org.iita.inventory.service;

import java.util.List;

import org.iita.inventory.label.NoPrinterException;
import org.iita.inventory.label.PrinterException;
import org.iita.inventory.model.Lot;
import org.iita.inventory.printing.LabelInfo;

/**
 * @author mobreza
 * 
 */
public interface LabelService {
	void print(List<? extends Lot> collection) throws PrinterException, NoPrinterException;

	public LabelInfo find(int id);

	public List<LabelInfo> findAll();
}
