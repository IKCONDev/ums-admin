package com.ikn.ums.admin.exception;

public class EmptyOTPException extends RuntimeException {

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

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public EmptyOTPException(String errorCode, String errorMessage) {
		super(errorMessage , new Throwable(errorCode) );
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}
	
	public EmptyOTPException() {
		
	}
}
