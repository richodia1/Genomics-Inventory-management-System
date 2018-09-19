/**
 * 
 */
package org.iita.inventory.label;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author mobreza
 * 
 */
public class FilePrinter implements Printer {

	private String fileName;
	private FileOutputStream fos = null;

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return this.fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * Connect to printer
	 * 
	 * @throws PrinterException
	 * 
	 * @see org.iita.inventory.label.Printer#connect()
	 */
	public boolean connect() throws PrinterException {
		if (fos != null)
			throw new PrinterException("FilePrinter stream already open.");

		try {
			fos = new FileOutputStream(this.fileName);
			return true;
		} catch (FileNotFoundException e) {
			throw new PrinterException("File '" + this.fileName + "' not found", e);
		}
	}

	/**
	 * Return the printer stream to write to.
	 * 
	 * @see org.iita.inventory.label.Printer#getOutputStream()
	 */
	public OutputStream getOutputStream() throws IOException {
		return fos;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.iita.inventory.label.LabelPrinter#close()
	 */
	public void close() throws PrinterException {
		if (fos != null) {
			try {
				fos.flush();
				fos.close();
			} catch (IOException e) {
				throw new PrinterException("Error closing printer.", e);
			}
		}
		fos = null;
	}
}
