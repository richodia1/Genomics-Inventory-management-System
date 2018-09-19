/**
 * 
 */
package org.iita.inventory.balance;

/**
 * This class implements the protocol used by AND GF balance. This balance used the following format:
 * 
 * <pre>
 * ST,+00000.00 g
 * ST,+00000.00 g
 * ST,+00300.00 g
 * ST,-00100.00 g
 * </pre>
 * 
 * The weight is repeatedly reported on serial port.
 * 
 * @author mobreza
 */
public class ANDGFBalance extends BalanceReader {

	/**
	 * Check if a line of text is parseable by this provider.
	 * 
	 * The full line read from the balance is: <code>ST,+00000.00 g</code>. This method will crash if the line is not in proper format.
	 * 
	 * @param line Raw text line in <code>ST,+00000.00 g</code> format.
	 * @return <code>true</code> if line matches the required format
	 */
	@Override
	protected boolean isParseable(String line) {
		return line != null && line.length() > 0 && line.startsWith("ST") && line.endsWith(" g");
	}

	/**
	 * Convert raw text line to a Double value weight in grams.
	 * 
	 * The full line read from the balance is: <code>ST,+00000.00 g</code>. This method will crash if the line is not in proper format.
	 * 
	 * @param line Raw text line in <code>ST,+00000.00 g</code> format.
	 * @return Parsed weight in grams.
	 */
	@Override
	protected Double parseLine(String line) {
		// ST,+00000.00 g
		return new Double(line.substring(3, 12));
	}
}
