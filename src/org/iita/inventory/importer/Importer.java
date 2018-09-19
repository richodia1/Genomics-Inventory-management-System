/**
 * 
 */
package org.iita.inventory.importer;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.util.List;

import org.iita.inventory.model.Lot;
import org.iita.inventory.service.DataAccessService;
import org.iita.inventory.service.ItemService;

/**
 * @author mobreza
 * 
 */
public interface Importer {
	public void setDao(DataAccessService dao);

	/**
	 * @param itemService the itemService to set
	 */
	public void setItemService(ItemService itemService);

	public abstract List<? extends Lot> getLots() throws ImportException;

	/**
	 * @param f
	 * @throws FileNotFoundException
	 */
	public abstract void setFile(File f) throws FileNotFoundException;

	/**
	 * @param f
	 * @param charset 
	 * @throws FileNotFoundException
	 */
	public abstract void setFile(File f, Charset charset) throws FileNotFoundException;

	public String getErrors();
}