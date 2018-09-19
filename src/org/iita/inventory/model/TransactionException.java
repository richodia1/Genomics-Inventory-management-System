package org.iita.inventory.model;

/**
 * Transaction exception
 * 
 * @author mobreza
 * 
 */
public class TransactionException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8252007451450756619L;

	public TransactionException() {

	}

	public TransactionException(String message) {
		super(message);
	}

	public TransactionException(String message, Throwable cause) {
		super(message, cause);
	}
}
