package com.ikn.ums.admin.service;

import java.util.List;

import com.ikn.ums.admin.dto.PermissionDTO;

public interface PermissionService {

	public List<PermissionDTO> getAllPermissions();
    public PermissionDTO getPermissionById(Long id);
    public PermissionDTO createPermission(PermissionDTO permissionDTO) ;
    public PermissionDTO updatePermission(PermissionDTO permissionDTO);	       
    public void deletePermissionById(Long id) ;
    public void deleteSelectedPermissionsByIds(List<Long> ids);

}
