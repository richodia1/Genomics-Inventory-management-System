/**
 * 
 */
package org.iita.inventory.balance;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Balance connected via TCP socket. In most deployments, the balance is connected to the client machine via serial port which is then exposed to the network
 * via <b>ser2net</b> service.
 * 
 * The data read from the TCP socket needs to be parsed. Parsing is done with "drivers"; instances of {@link BalanceReader}.
 * 
 * Example configuration:
 * 
 * <pre>
 * &lt;bean name=&quot;balanceGF1200&quot; class=&quot;org.iita.inventory.balance.TCPBalance&quot;&gt;
 * 	&lt;property name=&quot;name&quot; value=&quot;GF-1200 on GRU-LABEL&quot; /&gt;
 * 	&lt;property name=&quot;driver&quot; value=&quot;org.iita.inventory.balance.ANDGFBalance&quot; /&gt;
 * 	&lt;property name=&quot;host&quot; value=&quot;172.30.100.151&quot; /&gt;
 * 	&lt;property name=&quot;port&quot; value=&quot;8889&quot; /&gt;
 * &lt;/bean&gt;
 * </pre>
 * 
 * In this example, a GF-1200 model is available at 172.30.100.151:8889, uses {@link ANDGFBalance} driver.
 * 
 * @author mobreza
 * 
 */
public class TCPBalance extends AbstractBalance implements Balance {
	protected static final Log LOG = LogFactory.getLog(TCPBalance.class);

	//private String host;
	//private int port;

	private Socket socket = null;
	private int timeout = 2000;
	private String driver;
	
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
	 * @return the host
	 */
	//public String getHost() {
//		return this.host;
	//}

	/**
	 * @param host the host to set
	 */
	//public void setHost(String host) {
	//	this.host = host;
	//}

	/**
	 * @return the port
	 */
	//public int getPort() {
	//	return this.port;
	//}

	/**
	 * @param port the port to set
	 */
	//public void setPort(int port) {
	//	this.port = port;
	//}

	/**
	 * Connects to specifed host and TCP port.
	 * 
	 * @see org.iita.inventory.balance.Balance#connect()
	 * @throws BalanceException if connection cannot be established
	 */
	public boolean connect(String host, int port) throws BalanceException {
		if (socket != null && socket.isConnected())
			throw new BalanceException("TCPBalance socket already connected. Make sure to properly close the socket!");

		try {
			socket = new Socket();
			socket.connect(new InetSocketAddress(host, port), this.timeout);//socket.connect(new InetSocketAddress(this.host, this.port), this.timeout);
			socket.setSoTimeout(2000);
			return true;
		} catch (UnknownHostException e) {
			LOG.error(e);
			this.socket = null;
			throw new BalanceException("Misconfigured TCP host '" + host + "' for balance " + this.getName(), e);//throw new BalanceException("Misconfigured TCP host '" + this.host + "' for balance " + this.getName(), e);
		} catch (Exception e) {
			LOG.error(e);
			this.socket = null;
			throw new BalanceException(e.getMessage(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.iita.inventory.balance.Balance#getInputStream()
	 */
	public InputStream getInputStream() throws IOException {
		return socket.getInputStream();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.iita.inventory.balance.Balance#getOutputStream()
	 */
	public OutputStream getOutputStream() throws IOException {
		return socket.getOutputStream();
	}

	/**
	 * Flush output stream and close socket. 
	 * 
	 * @see org.iita.inventory.balance.Balance#close()
	 */
	public void close() throws Exception {
		if (this.socket != null) {
			try {
				this.socket.getOutputStream().flush();
			} catch (Exception ex) {

			}
			this.socket.close();
		}
		this.socket = null;
	}

	/**
	 * @return the timeout
	 */
	public int getTimeout() {
		return this.timeout;
	}

	/**
	 * @param timeout the timeout to set
	 */
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	@Override
	public BalanceReader createInstance(String host, int port) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		BalanceReader reader = (BalanceReader) Class.forName(this.driver).newInstance();
		reader.setBalance(this);
		return reader;
	}
}
