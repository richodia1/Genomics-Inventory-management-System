/**
 * inventory.Struts Jul 10, 2010
 */
package org.iita.inventory.service;

import java.util.List;

import org.iita.inventory.model.Item;
import org.iita.inventory.model.Lot;
import org.iita.inventory.model.LotTraitLastValue;
import org.iita.inventory.model.LotTraitValue;
import org.iita.inventory.model.LotTrial;
import org.iita.trial.service.TrialService;

/**
 * @author mobreza
 */
public interface InventoryTrialService extends TrialService<Lot, LotTrial, LotTraitValue, LotTraitLastValue> {

	/**
	 * List last trait values for an Item
	 * 
	 * @param returnType
	 * @param item
	 * @return
	 */
	List<LotTraitLastValue> getTraitLastValues(Item item);
}
