package com.ikn.ums.admin.service;

import java.util.List;
import java.util.Optional;

import com.ikn.ums.admin.entity.Role;
public interface RoleService {

    public List<Role> getAllRoles();
    public Optional<Role> getRoleById(Long id);
    public Optional<Role> getRoleByName(String name) ;
    public Role createRole(Role role) ;
    public Role updateRole(Role role);	       
    public void deleteRole(Long id) ;
}
