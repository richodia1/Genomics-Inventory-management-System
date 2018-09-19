package org.iita.inventory.balance;

/**
 * AbstractBalance defines core properties of all implementing Balance classes.
 * 
 * Example configuration:
 * 
 * <pre>
 * &lt;bean name=&quot;balanceGF1200&quot; class=&quot;org.iita.inventory.balance.TCPBalance&quot;&gt;
 * 	&lt;property name=&quot;name&quot; value=&quot;GF-1200 on GRU-LABEL&quot; /&gt;
 * 	&lt;property name=&quot;driver&quot; value=&quot;org.iita.inventory.balance.ANDGFBalance&quot; /&gt;
 * 	... other properties, specific to balance class
 * &lt;/bean&gt;
 * </pre>
 * 
 * @author mobreza
 * 
 */
public abstract class AbstractBalance implements Balance {

	/** Friendly name */
	private String name;
	/** Driver className */
	private String driver;

	public AbstractBalance() {
		super();
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the driver
	 */
	public String getDriver() {
		return this.driver;
	}

	/**
	 * @param driver the driver to set
	 */
	public void setDriver(String driver) {
		this.driver = driver;
	}

	/**
	 * Creates and returns an instance of {@link BalanceReader} that is able to parse the input provided by {@link Balance}.
	 * 
	 * @see BalanceReader
	 * @return An instance of "driver" class, configured to use this {@link Balance}
	 */
	public BalanceReader createInstance(String host, String port) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		BalanceReader reader = (BalanceReader) Class.forName(this.driver).newInstance();
		reader.setBalance(this);
		return reader;
	}

}