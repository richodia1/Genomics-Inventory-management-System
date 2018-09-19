/**
 * 
 */
package org.iita.inventory.balance;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Balance connected to a local serial port.
 * 
 * Example configuration:
 * 
 * <pre>
 * &lt;bean name=&quot;balanceGF1200&quot; class=&quot;org.iita.inventory.balance.SerialPortBalance&quot;&gt;
 * 	&lt;property name=&quot;name&quot; value=&quot;GF-1200 on GRU-LABEL&quot; /&gt;
 * 	&lt;property name=&quot;driver&quot; value=&quot;org.iita.inventory.balance.ANDGFBalance&quot; /&gt;
 * 	&lt;property name=&quot;device&quot; value=&quot;/dev/ttyUSB0&quot; /&gt;
 * &lt;/bean&gt;
 * </pre>
 * 
 * @author mobreza
 * 
 */
public class SerialPortBalance extends AbstractBalance {

	private String device;
	private File file;
	private FileOutputStream outputStream;
	private FileInputStream inputStream;

	/**
	 * Set local device name. Example "/dev/ttyUSB0"
	 * 
	 * @param device
	 */
	public void setDevice(String device) {
		this.device = device;
	}

	public String getDevice() {
		return device;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.iita.inventory.balance.Balance#connect()
	 */
	@Override
	public boolean connect() throws BalanceException {
		if (this.file != null)
			throw new BalanceException("Already connected to device \"" + this.device + "\".");

		File file = new File(this.device);
		if (!file.exists())
			throw new BalanceException("Device \"" + this.device + "\" does not exist.");
		if (!file.canRead())
			throw new BalanceException("Cannot read device \"" + this.device + "\". Check permissions.");

		this.file = file;

		try {
			this.inputStream = new FileInputStream(this.file);
			this.outputStream = new FileOutputStream(this.file);
			return true;
		} catch (FileNotFoundException e) {
			throw new BalanceException(e.getMessage(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.iita.inventory.balance.Balance#close()
	 */
	@Override
	public void close() throws Exception {
		if (this.outputStream != null) {
			this.outputStream.flush();
			this.outputStream.close();
			this.outputStream = null;
		}
		if (this.inputStream != null) {
			this.inputStream.close();
			this.inputStream = null;
		}
		this.file = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.iita.inventory.balance.Balance#getInputStream()
	 */
	@Override
	public InputStream getInputStream() throws IOException {
		return this.inputStream;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.iita.inventory.balance.Balance#getOutputStream()
	 */
	@Override
	public OutputStream getOutputStream() throws IOException {
		return this.outputStream;
	}

}
