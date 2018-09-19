/**
 * 
 */
package org.iita.inventory.service;

import java.util.List;

import org.iita.inventory.label.ScaleException;
import org.iita.inventory.printing.BalanceScaleInfo;

/**
 * Declare printer management methods.
 * 
 * @author KORaegbunam
 */
public interface BalanceScaleService {
	List<BalanceScaleInfo> list();

	void store(BalanceScaleInfo scaleInfo);

	void delete(BalanceScaleInfo scaleInfo);

	BalanceScaleInfo find(String scaleSessionId);
	
	BalanceScaleInfo find(Integer id);



	/**
	 * Get list of all printers defined in application
	 * 
	 * @return list of printers defined in application
	 */
	List<BalanceScaleInfo> getScales();

	/**
	 * Select the printer for the session
	 * 
	 * @param sessionId Session ID
	 * @param printer Selected printer
	 * @throws PrinterException 
	 */
	void selectBalanceScale(String sessionId, BalanceScaleInfo scale) throws ScaleException;

	void deleteScale(Integer id);
}
