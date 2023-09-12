package com.ikn.ums.authentication.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVO {
	
	private String email;
	
	private String encryptedPassword;
	
	private String userRole;
	
	private int otpCode;
	
	private boolean twoFactorAuthentication;

	private EmployeeVO employee;
}
