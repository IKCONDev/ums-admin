package com.ikn.ums.users.role.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ikn.ums.users.role.entity.Role;
import com.ikn.ums.users.role.repository.RoleRepository;
import com.ikn.ums.users.role.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;
    
	@Override
	public List<Role> getAllRoles() {
		return roleRepository.findAll();
	}

	@Override
	public Optional<Role> getRoleById(Long id) {
		return roleRepository.findById(id);
	}

	@Override
	public Optional<Role> getRoleByName(String name) {
		 return roleRepository.findByName(name);
	}

	@Override
	public Role createRole(Role role) {
		 return roleRepository.save(role);
	}

	@Override
	public Role updateRole(Long roleId, Role updatedRole) {
		return null;
		//TODO: Implement
	}

	@Override
	public void deleteRole(Long roleId) {
		roleRepository.deleteById(roleId);
	}

}
