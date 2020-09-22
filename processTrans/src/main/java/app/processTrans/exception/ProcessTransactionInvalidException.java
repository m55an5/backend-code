package main.java.app.processTrans.exception;

public class ProcessTransactionInvalidException extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ProcessTransactionInvalidException(final String message) {
		super(message);
	}
	
	public String getErrorMessage(final String message) {
		return message;
	}

}
