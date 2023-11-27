package com.ikn.ums.admin.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.ikn.ums.admin.entity.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String email;
	private Set<RoleDTO> userRoles = new HashSet<>();
	private int otpCode;
	private boolean twoFactorAuthentication;	
	private byte[] profilePic;
	private boolean isActive;
	private int loginAttempts;
	private LocalDateTime createdDateTime;
	private LocalDateTime modifiedDateTime;
	private String createdBy;
	private String modifiedBy;
	private String createdByEmailId;
	private String modifiedByEmailId;
	private UserRoleMenuItemPermissionMapDTO userRoleMenuItemPermissionMap;

}
