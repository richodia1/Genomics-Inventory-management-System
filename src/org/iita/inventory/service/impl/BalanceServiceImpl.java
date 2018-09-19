/**
 * 
 */
package org.iita.inventory.service.impl;

import org.iita.inventory.balance.Balance;
import org.iita.inventory.balance.BalanceException;
import org.iita.inventory.balance.BalanceReader;
import org.iita.inventory.service.BalanceService;

/**
 * {@link BalanceService} implementation. Currently only supports one balance connected to the system.
 * 
 * TODO Need to add support for multiple balances connected to the network!
 * TODO Find an available balance for the client IP address
 * TODO Use user preferences and have the selected balance name associated with the user
 * 
 * @author mobreza
 * 
 */
public class BalanceServiceImpl implements BalanceService {
	private Balance balance;

	/**
	 * @return the balance
	 */
	public Balance getBalance() {
		return this.balance;
	}

	/**
	 * @param balance the balance to set
	 */
	public void setBalance(Balance balance) {
		this.balance = balance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.iita.inventory.service.BalanceService#getCurrentWeight()
	 */
	@Override
	public Double getCurrentWeight(String host, int port) throws BalanceException {
		BalanceReader reader = null;
		try {
			reader = this.balance.createInstance(host, port);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BalanceException("Error creating BalanceReader.", e);
		}

		return reader.readBalance(host, port);
	}

}