package com.ikn.ums.admin.exception;

public class ControllerException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String errorCode;
	private String errorMessage;

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public ControllerException(String errorCode, String errorMessage) {
		//super();
		super(errorMessage , new Throwable(errorCode) );
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}
	
	public ControllerException() {
		
	}	
}
