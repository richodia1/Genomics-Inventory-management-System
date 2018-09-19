/**
 * 
 */
package org.iita.inventory.service;

import org.iita.inventory.balance.BalanceException;

/**
 * @author mobreza
 * 
 */
public interface BalanceService {

	/**
	 * Get current weight
	 * 
	 * @return
	 * @throws BalanceException
	 */
	Double getCurrentWeight(String host, int port) throws BalanceException;
}
