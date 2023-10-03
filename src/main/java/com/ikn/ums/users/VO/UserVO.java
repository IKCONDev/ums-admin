package com.ikn.ums.users.VO;

import java.util.HashSet;
import java.util.Set;

import com.ikn.ums.users.role.entity.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVO {
	
	private String email;
	
	private String encryptedPassword;
	
	private Set<Role> userRoles = new HashSet<>();
	
	private int otpCode;
	
	private boolean twoFactorAuthentication;

	private EmployeeVO employee;
	
	private byte[] profilePic;
	
	private boolean isActive;
}
