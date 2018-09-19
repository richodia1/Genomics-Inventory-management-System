/**
 * 
 */
package org.iita.inventory.label;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Implementation of {@link Printer} interface supporting TCP enabled printers.
 * 
 * In our case we used <b>xinetd</b> to network enable Zebra printer connected to a PC's parallel port.
 * 
 * @author mobreza
 */
public class TCPPrinter implements Printer {

	/** Host name */
	private String host;
	/** TCP Port */
	private int port;

	/** Socket */
	private Socket socket = null;
	/** Connection timeout */
	private int timeout = 1000;

	/**
	 * @return the host
	 */
	public String getHost() {
		return this.host;
	}

	/**
	 * @param host the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return this.port;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * Connect to printer
	 * 
	 * @throws Exception
	 * 
	 * @see org.iita.inventory.label.Printer#connect()
	 */
	public boolean connect() throws PrinterException {
		if (socket != null && socket.isConnected())
			throw new PrinterException("TCPPrinter socket already connected. Make sure you call close() from your code.");

		try {
			socket = new Socket();
			socket.connect(new InetSocketAddress(this.host, this.port), this.timeout);
			return true;
		} catch (UnknownHostException e) {
			this.socket = null;
			throw new PrinterException("Unknown host '" + this.host + "' for this printer.", e);
		} catch (IOException e) {
			this.socket = null;
			throw new PrinterException(e.getMessage(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.iita.inventory.label.Printer#getInputStream()
	 */
	public InputStream getInputStream() throws IOException {
		return socket.getInputStream();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.iita.inventory.label.Printer#getOutputStream()
	 */
	public OutputStream getOutputStream() throws IOException {
		return socket.getOutputStream();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.iita.inventory.label.LabelPrinter#close()
	 */
	public void close() throws PrinterException {
		if (this.socket != null) {
			try {
				this.socket.getOutputStream().flush();
				this.socket.close();
			} catch (Exception ex) {
				throw new PrinterException("Error closing printer socket. " + ex.getMessage(), ex);
			}
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
}
