/**
 * inventory.Struts Oct 22, 2009
 */
package org.iita.inventory.service;

import java.util.Date;
import java.util.List;

/**
 * Service definition to deal with Viability test results.
 * 
 * @author mobreza
 */
public interface ViabilityService {
	/**
	 * Utility class to help with data submission
	 * 
	 * @author mobreza
	 */
	public class ViabilityResult {
		public Long lotId;
		public Double viability;
	}

	/**
	 * Add results of viability tests
	 * 
	 * @param testDate
	 * @param viability
	 */
	void addViabilityResult(Date testDate, List<ViabilityResult> viabilityResults);
}
