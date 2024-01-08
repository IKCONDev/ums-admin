package com.ikn.ums.admin.service;

import java.util.List;

import com.ikn.ums.admin.dto.UserRoleMenuItemPermissionMapDTO;

public interface UserRoleMenuItemPermissionMapService {

	UserRoleMenuItemPermissionMapDTO createUserRoleMenuItemPermissionMap(
			UserRoleMenuItemPermissionMapDTO userRoleMenuItemPermissionMapDTO);

	UserRoleMenuItemPermissionMapDTO updateUserRoleMenuItemPermissionMap(
			UserRoleMenuItemPermissionMapDTO userRoleMenuItemPermissionMapDTO);

	List<UserRoleMenuItemPermissionMapDTO> getAllUserRoleMenuItemPermissionMap();

	List<UserRoleMenuItemPermissionMapDTO> getUserRoleMenuItemPermissionMapsByUserId(String email);

	void deleteAllUserRoleMenuItemPermissionMapByUserId(String email);

	List<UserRoleMenuItemPermissionMapDTO> updateAllUserRoleMenuItemPermissionMaps(
			List<UserRoleMenuItemPermissionMapDTO> userRoleMenuItemPermissionMapDTOList);

	List<UserRoleMenuItemPermissionMapDTO> saveAllUserRoleMenuItemPermissionMaps(
			List<UserRoleMenuItemPermissionMapDTO> userRoleMenuItemPermissionMapDTOList);

}
