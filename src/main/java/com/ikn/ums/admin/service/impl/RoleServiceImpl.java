package com.ikn.ums.admin.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ikn.ums.admin.entity.Role;
import com.ikn.ums.admin.entity.User;
import com.ikn.ums.admin.exception.EmptyInputException;
import com.ikn.ums.admin.exception.EmptyListException;
import com.ikn.ums.admin.exception.EntityNotFoundException;
import com.ikn.ums.admin.exception.ErrorCodeMessages;
import com.ikn.ums.admin.repository.RoleRepository;
import com.ikn.ums.admin.service.RoleService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;
    
	@Override
	public List<Role> getAllRoles() {
		log.info("RoleServiceImpl.getAllRoles() ENTERED.");
		List<Role> rolesList = null;
		rolesList = roleRepository.findAll();
		if ( rolesList == null || rolesList.isEmpty())
			throw new EmptyListException(ErrorCodeMessages.ERR_ROLE_LIST_IS_EMPTY_CODE,
					ErrorCodeMessages.ERR_ROLE_ID_ALREADY_EXISTS_MSG);
		return rolesList;
	}

	@Override
	public Optional<Role> getRoleById(Long roleId) {
		log.info("RoleServiceImpl.getRoleById() ENTERED : roleId : " + roleId);
		if (roleId <= 0)
			throw new EmptyInputException(ErrorCodeMessages.ERR_ROLE_ID_IS_EMPTY_CODE,
					ErrorCodeMessages.ERR_ROLE_ID_IS_EMPTY_MSG);
		return roleRepository.findById(roleId);
	}

	@Override
	public Optional<Role> getRoleByName(String roleName) {
		log.info("RoleServiceImpl.getRoleByName() ENTERED : roleName : " + roleName);
		if (roleName == null || roleName.isEmpty() || roleName.length() == 0)
			throw new EmptyInputException(ErrorCodeMessages.ERR_ROLE_NAME_IS_EMPTY_CODE,
					ErrorCodeMessages.ERR_ROLE_NAME_IS_EMPTY_MSG);
		return roleRepository.findByRoleName(roleName);
	}

	@Override
	public Role createRole(Role role) {
		log.info("RoleServiceImpl.createRole() ENTERED");
		if (role == null) 
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_ROLE_ENTITY_IS_NULL_CODE,
					ErrorCodeMessages.ERR_ROLE_ENTITY_IS_NULL_MSG);
		Role savedRole = roleRepository.save(role);
		return savedRole;
	}

	@Override
	public Role updateRole(Role role) {
		log.info("RoleServiceImpl.updateRole() entered with args - role");
		if(role == null || role.equals(null)) {
			log.info("RoleServiceImpl.updateRole() EntityNotFoundException : user object is null");
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_ROLE_ENTITY_IS_NULL_CODE, 
					ErrorCodeMessages.ERR_ROLE_ENTITY_IS_NULL_MSG);
		}
		log.info("RoleServiceImpl.updateRole() is under execution...");
		Role updatedRole =  roleRepository.save(role);
		log.info("RoleServiceImpl.updateRole() executed successfully.");
		return updatedRole;
	}

	@Override
	public void deleteRole(Long roleId) {
		log.info("RoleServiceImpl.deleteRole() ENTERED : roleId : " + roleId);
		if (roleId <= 0)
			throw new EmptyInputException(ErrorCodeMessages.ERR_ROLE_ID_IS_EMPTY_CODE,
					ErrorCodeMessages.ERR_ROLE_ID_IS_EMPTY_MSG);
		roleRepository.deleteById(roleId);
	}

}
