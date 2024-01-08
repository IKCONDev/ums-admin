package com.ikn.ums.admin.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleMenuItemPermissionMapDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String email;
	private Long roleId;
	private String menuItemIdList;
	private String permissionIdList;
	private LocalDateTime createdDateTime;
	private LocalDateTime modifiedDateTime;
	private String createdBy;
	private String modifiedBy;
	private String createdByEmailId;
	private String modifiedByEmailId;
	private MenuItemDTO menuItem;

}
