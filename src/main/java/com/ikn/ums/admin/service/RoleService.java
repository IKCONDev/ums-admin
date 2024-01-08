package com.ikn.ums.admin.service;

import java.util.List;

import com.ikn.ums.admin.dto.RoleDTO;
public interface RoleService {

    public List<RoleDTO> getAllRoles();
    public RoleDTO getRoleById(Long id);
    public RoleDTO getRoleByName(String name) ;
    public RoleDTO createRole(RoleDTO role) ;
    public RoleDTO updateRole(RoleDTO role);	       
    public void deleteRole(Long id) ;
    public void deleteSelectedRolesByIds(List<Long> ids);
}
