package com.ikn.ums.admin.service;

import java.util.List;

import com.ikn.ums.admin.dto.UserRoleMenuItemPermissionMapDTO;
import com.ikn.ums.admin.entity.UserRoleMenuItemPermissionMap;

public interface UserRoleMenuItemPermissionMapService {
	
	UserRoleMenuItemPermissionMapDTO createUserRoleMenuItemPermissionMap(UserRoleMenuItemPermissionMapDTO userRoleMenuItemPermissionMapDTO);
	UserRoleMenuItemPermissionMap updateUserRoleMenuItemPermissionMap(UserRoleMenuItemPermissionMap userRoleMenuItemPermissionMap);
	List<UserRoleMenuItemPermissionMap> getAllUserRoleMenuItemPermissionMap();
	UserRoleMenuItemPermissionMap getUserRoleMenuItemPermissionMapByUserId(String email);
	void deleteUserRoleMenuItemPermissionMapByUserId(String email);
	
}
