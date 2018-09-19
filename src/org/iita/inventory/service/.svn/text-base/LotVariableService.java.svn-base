/**
 * 
 */
package org.iita.inventory.service;

import java.util.List;

import org.iita.inventory.model.FieldVariablesList;
import org.iita.inventory.model.Lot;
import org.iita.inventory.model.LotVariable;

/**
 * @author KOraegbunam
 *
 */
public interface LotVariableService {
	
	/**
	 * Load lotVariable information from underlying storage.
	 * 
	 * @param id LotVariable identifier
	 * @return LotVariable
	 */
	public LotVariable loadLotVariable(long id);
	
	/**
	 * List all lot variables
	 * 
	 * @return
	 */
	public List<LotVariable> loadLotVariables(Lot lot);
	
	/**
	 * List all field variables list
	 * 
	 * @return
	 */
	public List<FieldVariablesList> loadFieldVariablesList();
	
	/**
	 * update lotVariables
	 * 
	 */
	public void updateLotVariables(LotVariable lotVariable);
	
	/**
	 * delete lotVariables
	 * 
	 */
	public void deleteLotVariables(LotVariable lotVariable);

	public List<LotVariable> getLotVariablesByBarcode(Long lotBarcodes);
	
	public LotVariable loadLotVariable(Long lotVarid);
}
