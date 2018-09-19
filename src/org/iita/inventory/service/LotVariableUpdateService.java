/**
 * 
 */
package org.iita.inventory.service;

import org.iita.inventory.model.Lot;
import org.iita.inventory.model.LotVariable;
import org.iita.inventory.model.LotVariableUpdateBulk;
import org.iita.inventory.model.Variables;

/**
 * @author KOraegbunam
 *
 */
public interface LotVariableUpdateService {

	void store(LotVariableUpdateBulk lotVariableUpdate);

	void commit(LotVariableUpdateBulk lotVariableUpdate);
	
	boolean verifyLotVariable(Variables variable, Lot lot);
	
	LotVariable loadForUpdate(Variables variable, Lot lot);
}
