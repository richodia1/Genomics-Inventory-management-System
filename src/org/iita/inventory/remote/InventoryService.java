/**
 * inventory.Struts Jun 29, 2010
 */
package org.iita.inventory.remote;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.iita.inventory.service.InventoryException;

/**
 * Definition of remoting service to access inventory data
 * 
 * @author mobreza
 */
@WebService
public interface InventoryService {
	@WebMethod
	public String whoAmI();

	/**
	 * Get Lot information
	 */
	public List<org.iita.inventory.remote.Lot> getLotsById(long ... ids);

	/**
	 * Get lots by their barcodes
	 * 
	 * @param barCodes
	 * @return
	 */
	List<Lot> getLotsByBarcode(long ... barCodes);

	/**
	 * List all lots at location <code>rootLocationId</code> and its sublocations
	 * 
	 * @param rootLocationId
	 * @return
	 */
	@WebMethod
	List<Lot> getLotsByLocation(long rootLocationId);

	/**
	 * List sublocations of location
	 * 
	 * @param rootLocationId
	 * @return
	 */
	@WebMethod
	List<Location> getLocations(Long rootLocationId);

	/**
	 * Get user's lot lists
	 * 
	 * @return
	 */
	List<LotList> getLotLists();
	
	/**
	 * Get user's lot list by id
	 * 
	 * @return
	 */
	LotList getLotList(Long listId);

	/**
	 * Update user's lot list
	 * 
	 * @param lotList
	 */
	void updateLotList(LotList lotList);

	/**
	 * Update observed quantity
	 */
	void updateLotQuantities(List<LotQuantity> quantities);
	
	//@WebMethod
	List<Variables> getVariableNames();
	
	//@WebMethod
	Variables getVariableName(Long id);

	void updateLotVariables(List<LotVariable> lotVariables) throws InventoryException;
	
	/**
	 * Get lot variables by their lot barcodes
	 * 
	 * @param barCodes
	 * @return
	 */
	List<LotVariable> getLotVariablesByBarcode(Long barCodes);
	
	/**
	 * Get lot field variables by their lot lotId
	 * 
	 * @param lotId
	 * @return
	 */
	List<LotFieldVariable> getLotFieldVariablesByLotId(Long lotId);
	
	/**
	 * Load field variables
	 * 
	 * @return
	 */
	List<FieldVariablesList> getFieldVariablesList();
	
	void updateLotSubtypes(List<LotSubtypeQuantity> lotSubtypes);
	
	void migrateLotToNewLocation(List<NewLocationMigration> newLocationMigration);
	void migrateLotToExistingLocation(List<ExistingLocationMigration> existingLocationMigration);
	
	/**
	 * update lotFieldVariables
	 * 
	 */
	public void updateLotFieldVariables(List<LotFieldVariable> fieldVariable);
	
	public String getServerTimeStamp();

	//void updateLotSubtypes(List<LotSubtypeQuantity> lotSubtypes);
}
