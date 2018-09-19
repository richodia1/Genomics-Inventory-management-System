/**
 * 
 */
package org.iita.inventory.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.Dictionary;
import java.util.List;

import org.iita.inventory.model.FieldVariables;
import org.iita.inventory.model.Item;
import org.iita.inventory.model.Location;
import org.iita.inventory.model.Lot;
import org.iita.inventory.model.Migration;
import org.iita.inventory.model.QuantityUpdateBulk;
import org.iita.inventory.model.Transaction2;
import org.iita.security.model.User;
import org.iita.service.impl.XLSImportException;
import org.iita.util.PagedResult;

/**
 * Inventory lot management service.
 * 
 * @author mobreza
 */
public interface LotService {
	/**
	 * Load lot information from underlying storage.
	 * 
	 * @param id Lot identifier
	 * @return Lot
	 */
	public Lot load(long id);

	/**
	 * Load lot information by the barcode assigned to the lot.
	 * 
	 * @param barCode Barcode identifier (Scanned barcode value)
	 * @return Lot
	 */
	public Lot loadByBarcode(long barCode);

	/**
	 * Persist lot information.
	 * 
	 * @param lot Lot to persist.
	 */
	public void store(Lot lot);

	/**
	 * Persists all lots in collection.
	 * 
	 * @param lot Lots to persist
	 */
	public void store(List<Lot> lots);

	/**
	 * Import lot information. This method will check all referenced objects and persist those as well.
	 * 
	 * @param lot Lot to persist.
	 */
	public void importLot(Lot lot);

	/**
	 * Permanently delete lot and related information. Use with caution.
	 * 
	 * @param id
	 */
	public void delete(Lot lot);

	/**
	 * Get lots for specified location.
	 * 
	 * @param location Location for which to list lots
	 * @param startAt First record to return
	 * @param pageSize Max results to return
	 * @return List of lots at specified location.
	 */
	public PagedResult<Lot> list(Location location, int startAt, int pageSize, boolean showHiddenLots);

	/**
	 * Get lots for specified location.
	 * 
	 * @param location Location for which to list lots
	 * @param maxDepth Include lots in sublocations to maximum of maxDepth
	 * @param startAt First record to return
	 * @param pageSize Max results to return
	 * @return List of lots at specified location.
	 */
	public PagedResult<Lot> list(Location location, int maxDepth, int startAt, int pageSize, boolean showHiddenLots);

	/**
	 * Get list of Lot objects of the selected list
	 * 
	 * @param selectedLots
	 * @return
	 */
	public List<Lot> getLots(List<Long> selectedLots);

	/**
	 * @param lots
	 */
	public void importLots(List<? extends Lot> lots);

	/**
	 * @param lot
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws InventoryException
	 */
	public Lot duplicate(Lot lot) throws InstantiationException, IllegalAccessException, InventoryException;

	/**
	 * Migrate lot to selected destination location
	 * 
	 * @param lot Lot to migrate
	 * @param destination New location of the lot
	 * @param reason Reason for lot migration
	 * @throws LocationException Thrown if location is <c>null</c> or lot already at that location
	 */
	public Migration migrate(Lot lot, Location destination, String reason) throws LocationException, NullPointerException;

	/**
	 * @param lots
	 * @param array
	 * @throws Exception
	 */
	public List<Lot> updateFields(List<? extends Lot> lots, String[] array) throws Exception;

	/**
	 * Take source <b>bulk</b> list of Lots and generate new Lots based on quantities provided in this bulk.
	 * 
	 * @param bulk Source bulk
	 * @return A new bulk update that contains the newly duplicated lots
	 * @throws InventoryException
	 */
	QuantityUpdateBulk registerDuplicatedLots(QuantityUpdateBulk bulk) throws InventoryException;

	/**
	 * List transactions by transaction date desc
	 * 
	 * @param startAt
	 * @param maxRecords
	 * @return
	 */
	public PagedResult<Transaction2> listTransactions(int startAt, int maxRecords);

	/**
	 * Get available lots for the list of items
	 * 
	 * @param itemElements
	 * @return
	 */
	public Dictionary<Item, List<Lot>> getLotsForItems(List<Item> itemElements);

	/**
	 * List all transactions between start and end date
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<Transaction2> listTransactions(Calendar startDate, Calendar endDate);

	/**
	 * @param lot
	 * @return
	 */
	public double getTotalQuantityInLocation(Lot lot);

	/**
	 * @param lotBarcodes
	 * @return
	 */
	public List<Lot> getLotsByBarcode(List<Long> lotBarcodes);

	/**
	 * Get list of all lots with positive quantity in location and its sublocations, limited to maxrecords
	 * 
	 * @param location
	 * @param maxRecords
	 * @return
	 */
	public List<Lot> list(Location location, int maxRecords);
	
	/**
	 * Merge all lot field Variables.
	 * 
	 * @param fieldVariable Lots to merge
	 */
	public void updateFieldVariables(Lot lot);
	
	/**
	 * remove all lot field Variables.
	 * 
	 * @param fieldVariable Lots to delete
	 */
	public void deleteFieldVariables(Lot lot);
	
	public List<FieldVariables> getLotFieldVariablesByLotId(Long lotId);

	List<FieldVariables> getLotFieldVariablesByLot(Lot lot);

	public FieldVariables loadLotFieldVariable(Lot lot, String var);

	public void mergeLotFieldVariable(FieldVariables lvu);

	public PagedResult<Object[]> getListLotTotalQuantityInLocation(int startAt, int maxResults);

	List<Lot> previewXLSLots(File file, List<Object[]> failedLotMappings, User user) throws FileNotFoundException,
	IOException, XLSImportException;
	
	void importXLSLots(List<Lot> lots, User user, List<Object[]> failedLotMappings);
}
