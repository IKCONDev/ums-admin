package com.ikn.ums.admin.VO;

import java.util.HashSet;
import java.util.Set;

import com.ikn.ums.admin.dto.UserRoleMenuItemPermissionMapDTO;
import com.ikn.ums.admin.entity.Role;

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
	
	private UserRoleMenuItemPermissionMapDTO userRoleMenuItemPermissionMap;
}
