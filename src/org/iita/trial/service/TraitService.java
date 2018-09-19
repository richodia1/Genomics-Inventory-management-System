/**
 * inventory.Struts Jun 30, 2010
 */
package org.iita.trial.service;

import org.iita.trial.model.Trait;
import org.iita.util.PagedResult;

/**
 * @author mobreza
 */
public interface TraitService {
	Trait findTrait(Long id);

	/**
	 * @param startAt
	 * @param maxRecords
	 * @return
	 */
	PagedResult<Trait> listTraits(int startAt, int maxRecords);
}
