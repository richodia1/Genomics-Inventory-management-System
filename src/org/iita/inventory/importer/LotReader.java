/**
 * inventory.Struts Jul 3, 2009
 */
package org.iita.inventory.importer;

import java.io.File;
import java.util.List;

import org.iita.inventory.model.Lot;

/**
 * @author mobreza
 *
 */
public interface LotReader {
	List<? extends Lot> readLots(File sourceFile) throws LotReaderException;
}
