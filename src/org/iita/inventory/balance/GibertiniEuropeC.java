/**
 * 
 */
package org.iita.inventory.balance;

/**
 * {@link GibertiniEuropeC} implements the protocol used by Gibertini balances. The full line read from the balance is: <code>     0.0</code> or
 * <code>-    0.1</code> for negative numbers. The current weight is reported repeatedly on the serial line.
 * 
 * @author mobreza
 * 
 */
public class GibertiniEuropeC extends BalanceReader {

	/**
	 * Check if a line of text is parseable by this provider.
	 * 
	 * The full line read from the balance is: <code>     0.0</code> or <code>-    0.1</code> for negative numbers. This method will crash if the line is not in
	 * proper format.
	 * 
	 * @param line Raw text line in <code>     0.0</code> format.
	 * @return <code>true</code> if line matches the required format
	 */
	@Override
	protected boolean isParseable(String line) {
		return line != null && line.length() > 3;
	}

	/**
	 * Convert raw text line to a Double value weight in grams. The input must be in proper format, i.e. one that {@link GibertiniEuropeC.isParseable} returns
	 * true.
	 * 
	 * @see GibertiniEuropeC.isParseable(String)
	 * 
	 * @param line Raw text line in <code>     0.0</code> format.
	 * @return Parsed weight in grams.
	 * @throws BalanceException when parsing errors occur
	 */
	@Override
	protected Double parseLine(String line) throws BalanceException {
		if (line.startsWith("-")) {
			throw new BalanceException("Error reading weight. Balance returned weight less than zero: " + line);
		}

		try {
			return new Double(line);
		} catch (NumberFormatException nfe) {
			System.err.println("Error parsing '" + line + "' to double. " + nfe.getMessage());
			throw new BalanceException("Error parsing to double: " + nfe.getMessage(), nfe);
		}
	}
}
