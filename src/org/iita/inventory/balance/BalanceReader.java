/**
 * 
 */
package org.iita.inventory.balance;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * BalanceReader opens the data stream to the balance, skips one line, and in 20 attempts tries to obtain a line that can be parsed by the driver (see
 * {@link BalanceReader#isParseable(String)}).
 * 
 * As the input is balance specific, the "drivers" will override methods {@link #isParseable(String)} and {@link #parseLine(String)}.
 * 
 * @see ANDGFBalance
 * @see GibertiniEuropeC
 * 
 * @author mobreza
 * 
 */
public abstract class BalanceReader {
	protected static final Log LOG = LogFactory.getLog(BalanceReader.class);
	protected Balance balance = null;

	/**
	 * 
	 */
	public BalanceReader() {
		super();
	}

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

	/**
	 * Get the current weight as reported by the balance. The returned weight is in grams.
	 * 
	 * @return Weight in grams as reported by the balance
	 * @throws BalanceException when connecting to
	 */
	public Double readBalance(String host, int port) throws BalanceException {
		if (!this.balance.connect(host, port)) {
			System.err.println("Cannot connect to balance");
			LOG.error("Cannot connect to balance. Read failed.");
			return null;
		}

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(this.balance.getInputStream()));

			String line = reader.readLine();
			// another read just in case the first read did not return a full line
			line = reader.readLine();

			// try reading at most 20 times before giving up completely, the first parseable line will be used
			int tries = 0;
			while (line.length() == 0 && !isParseable(line)) {
				if (LOG.isTraceEnabled())
					LOG.trace("Balance: " + line);
				line = reader.readLine();
				if (tries++ > 20) {
					line = null;
					break;
				}
			}

			// parse the line and get weight in grams
			Double result = null;
			if (line != null)
				result = parseLine(line);

			LOG.info("Weight: " + result);
			return result;

		} catch (IOException e) {
			LOG.error(e);
			throw new BalanceException(e.getMessage(), e);
		} finally {
			try {
				if (LOG.isTraceEnabled())
					LOG.trace("Closing connection to balance");
				this.balance.close();
			} catch (Exception e) {
				LOG.error(e);
			}
		}
	}

	/**
	 * Check if the input matches the format pattern supported by the driver.
	 * 
	 * @param line
	 * @return <code>true</code> if line matches input
	 */
	protected abstract boolean isParseable(String line);

	/**
	 * Convert line to weight in grams.
	 * 
	 * @param line
	 * @return
	 * @throws BalanceException thrown when parsing exceptions occur reading the balance. Check innerException for more details.
	 */
	protected abstract Double parseLine(String line) throws BalanceException;
}