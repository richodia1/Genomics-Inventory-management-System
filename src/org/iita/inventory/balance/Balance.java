/**
 * 
 */
package org.iita.inventory.balance;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Balance interface defines the basic methods of connecting to weighing balances. Currently the only implementation is {@link TCPBalance} as all balances are
 * connected to clients and accessible via <b>ser2net</b> service.
 * 
 * @see TCPBalance
 * 
 * @author mobreza
 * 
 */
public interface Balance {

	/**
	 * Connect to the balance.
	 * 
	 * @return <code>true</code> if connection was successfully established.
	 * @throws BalanceException if connection failed
	 */
	public abstract boolean connect(String host, int port) throws BalanceException;

	/**
	 * Get InputStream to read from the connected balance.
	 * 
	 * @return the input stream
	 * @throws IOException
	 */
	public abstract InputStream getInputStream() throws IOException;

	/**
	 * OutputStream to connected balance.
	 * 
	 * @return the output stream
	 * @throws IOException
	 */
	public abstract OutputStream getOutputStream() throws IOException;

	/**
	 * Close streams
	 * 
	 * @throws Exception
	 */
	public abstract void close() throws Exception;

	/**
	 * Create and return an instance of {@link BalanceReader}.
	 * 
	 * @return a {@link BalanceReader}
	 * @throws InstantiationException could not create instance
	 * @throws IllegalAccessException constructor not accessible
	 * @throws ClassNotFoundException "driver" class not found
	 */
	public abstract BalanceReader createInstance(String host, int port) throws InstantiationException, IllegalAccessException, ClassNotFoundException;
}