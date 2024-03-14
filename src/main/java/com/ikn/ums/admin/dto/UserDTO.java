package com.ikn.ums.admin.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
	private String encryptedPassword;
	private String previousPassword1;
	private String previousPassword2;
	private String previousPassword;
	private Set<RoleDTO> userRoles = new HashSet<>();
	private int otpCode;
	private boolean twoFactorAuthentication;	
	private byte[] profilePic;
	private boolean isActive;
	private Integer loginAttempts;
	private String loginAttemptedClientIP;
	private String loginAttemptedClientDeviceType;
	private LocalDateTime loginAttemptedDateTime;
	private LocalDateTime createdDateTime;
	private LocalDateTime modifiedDateTime;
	private String createdBy;
	private String modifiedBy;
	private String createdByEmailId;
	private String modifiedByEmailId;
	private List<UserRoleMenuItemPermissionMapDTO> userRoleMenuItemPermissionMap;

}
