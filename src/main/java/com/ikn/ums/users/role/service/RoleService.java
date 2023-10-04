package com.ikn.ums.users.role.service;

import java.util.List;
import java.util.Optional;

import com.ikn.ums.users.role.entity.Role;

public interface RoleService {

    public List<Role> getAllRoles();
    public Optional<Role> getRoleById(Long id);
    public Optional<Role> getRoleByName(String name) ;
    public Role createRole(Role role) ;
    public Role updateRole(Long id, Role updatedRole);	       
    public void deleteRole(Long id) ;
}