package com.ikn.ums.authentication.model;

public class ValidateOtpRequestModel {
	
	private String email;
	private String otpCode;
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getOtpCode() {
		return otpCode;
	}
	public void setOtpCode(String otpCode) {
		this.otpCode = otpCode;
	}
	@Override
	public String toString() {
		return "ValidateOtpRequestModel [email=" + email + ", otpCode=" + otpCode + "]";
	}

}
