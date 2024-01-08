package com.ikn.ums.admin.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidateOtpRequestModel {
	
	private String email;
	private String otpCode;

}
