package com.ikn.ums.admin.service;

import java.util.List;
import java.util.Optional;

import com.ikn.ums.admin.dto.PermissionDTO;
import com.ikn.ums.admin.entity.Permission;

public interface PermissionService {

	public List<Permission> getAllPermissions();
    public Optional<Permission> getPermissionById(Long id);
  //  public Optional<Permission> getPermissionByName(String name) ;
    public Permission createPermission(PermissionDTO permissionDTO) ;
    public Permission updatePermission(PermissionDTO permissionDTO);	       
    public void deletePermissionById(Long id) ;
    public void deleteSelectedPermissionsByIds(List<Long> ids);

}
