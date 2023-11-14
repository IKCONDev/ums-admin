package com.ikn.ums.admin.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ikn.ums.admin.entity.Role;
import com.ikn.ums.admin.exception.EmptyInputException;
import com.ikn.ums.admin.exception.EmptyListException;
import com.ikn.ums.admin.exception.EntityNotFoundException;
import com.ikn.ums.admin.exception.ErrorCodeMessages;
import com.ikn.ums.admin.exception.RoleNameExistsException;
import com.ikn.ums.admin.repository.RoleRepository;
import com.ikn.ums.admin.service.RoleService;
import com.ikn.ums.admin.utils.AdminConstants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private ModelMapper mapper;
  
	@Override
	public Role createRole(Role role) {
		log.info("RoleServiceImpl.createRole() ENTERED");
		if (role == null) 
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_ROLE_ENTITY_IS_NULL_CODE,
					ErrorCodeMessages.ERR_ROLE_ENTITY_IS_NULL_MSG);
		if (isRoleNameExists(role)) 
			throw new RoleNameExistsException(ErrorCodeMessages.ERR_ROLE_NAME_EXISTS_CODE,
					ErrorCodeMessages.ERR_ROLE_NAME_EXISTS_MSG);
		log.info("RoleServiceImpl.createRole() is under execution...");
		role.setCreatedDateTime(LocalDateTime.now());
		role.setRoleStatus(AdminConstants.STATUS_ACTIVE);
		Role savedRole = roleRepository.save(role);
		log.info("RoleServiceImpl.createRole() executed successfully");
		return savedRole;
	}
	
	
//	@Transactional (propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT )
	@Transactional 
	@Override
	public Role updateRole(Role role) {
		log.info("RoleServiceImpl.updateRole() entered with args - role");
		if(role == null || role.equals(null)) {
			log.info("RoleServiceImpl.updateRole() EntityNotFoundException : user object is null");
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_ROLE_ENTITY_IS_NULL_CODE, 
					ErrorCodeMessages.ERR_ROLE_ENTITY_IS_NULL_MSG);
		}
		Optional<Role> optRole = roleRepository.findById(role.getRoleId());
		Role dbRole = null;
		if(optRole.isPresent()) {
			dbRole = optRole.get();
		}
		//set modified date time
		role.setModifiedDateTime(LocalDateTime.now());
		mapper.map(role, dbRole);
		log.info("RoleServiceImpl.updateRole() is under execution.");
		Role updatedRole =  roleRepository.save(dbRole);
		log.info("RoleServiceImpl.updateRole() executed successfully.");
		return updatedRole;
	}

	@Transactional 
	@Override
	public void deleteRole(Long roleId) {
		log.info("RoleServiceImpl.deleteRole() ENTERED : roleId : " + roleId);
		if (roleId <= 0)
			throw new EmptyInputException(ErrorCodeMessages.ERR_ROLE_ID_IS_EMPTY_CODE,
					ErrorCodeMessages.ERR_ROLE_ID_IS_EMPTY_MSG);
		//retrieve the role details
		log.info("RoleServiceImpl.deleteRole() is under execution...");
		Optional<Role> optRole = roleRepository.findById(roleId);
		
		if ( !optRole.isPresent() || optRole == null ) {
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_ROLE_ENTITY_IS_NULL_CODE,
					ErrorCodeMessages.ERR_ROLE_ENTITY_IS_NULL_MSG);
		} else {
			optRole.get().setRoleStatus(AdminConstants.STATUS_IN_ACTIVE);
			updateRole(optRole.get());
			log.info("RoleServiceImpl.deleteRole() executed successfully");
		}
	}

	@Transactional 
	@Override
	public void deleteSelectedRolesByIds(List<Long> roleIds) {
		log.info("RoleServiceImpl.deleteSelectedRolesByIds() ENTERED : roleIds Size : " + roleIds.size() );
		if ( roleIds.size() <= 0 )
			throw new EmptyListException(ErrorCodeMessages.ERR_ROLE_LIST_IS_EMPTY_CODE,
					ErrorCodeMessages.ERR_ROLE_LIST_IS_EMPTY_MSG);		
			List<Role> roleList = roleRepository.findAllById(roleIds);
			if(roleList.size() > 0) {
				roleList.forEach(role -> {
					role.setRoleStatus("InActive");
				});
			}
	}
	@Override
	public List<Role> getAllRoles() {
		log.info("getAllRoles() ENTERED.");
		List<Role> rolesList = null;
		log.info("getAllRoles() is under execution...");
		rolesList = roleRepository.findAllRoles( AdminConstants.STATUS_ACTIVE );
		if ( rolesList == null || rolesList.isEmpty() || rolesList.size() == 0 )
			throw new EmptyListException(ErrorCodeMessages.ERR_ROLE_LIST_IS_EMPTY_CODE,
					ErrorCodeMessages.ERR_ROLE_ID_ALREADY_EXISTS_MSG);
		log.info("getAllRoles() : Total Roles Count : " + rolesList.size());
		log.info("getAllRoles() executed successfully");
		return rolesList;
	}

	@Override
	public Optional<Role> getRoleById(Long roleId) {
		log.info("RoleServiceImpl.getRoleById() ENTERED : roleId : " + roleId);
		log.info("RoleServiceImpl.getRoleById() is under execution...");
		if (roleId <= 0)
			throw new EmptyInputException(ErrorCodeMessages.ERR_ROLE_ID_IS_EMPTY_CODE,
					ErrorCodeMessages.ERR_ROLE_ID_IS_EMPTY_MSG);
		log.info("RoleServiceImpl.getRoleById() executed successfully");
		return roleRepository.findById(roleId);
	}

	@Override
	public Optional<Role> getRoleByName(String roleName) {
		log.info("RoleServiceImpl.getRoleByName() ENTERED : roleName : " + roleName);
		log.info("RoleServiceImpl.getRoleByName() is under execution...");
		if (roleName == null || roleName.isEmpty() || roleName.length() == 0)
			throw new EmptyInputException(ErrorCodeMessages.ERR_ROLE_NAME_IS_EMPTY_CODE,
					ErrorCodeMessages.ERR_ROLE_NAME_IS_EMPTY_MSG);
		log.info("RoleServiceImpl.getRoleByName() executed successfully");
		return roleRepository.findByRoleName(roleName);
	}

	public boolean isRoleNameExists(Role role) {
		log.info("RoleServiceImpl.isRoleExists() ENTERED : role : " );
		boolean isRoleNameExists = false;
		
		if (role == null) {
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_ROLE_ENTITY_IS_NULL_CODE,
					ErrorCodeMessages.ERR_ROLE_ENTITY_IS_NULL_MSG);
		} else {
			log.info("RoleServiceImpl  : Role Id : " + role.getRoleId() + " || Role Name : " + role.getRoleName());
			Optional<Role> optRole = roleRepository.findByRoleName( role.getRoleName() );
			isRoleNameExists = optRole.isPresent();
			log.info("RoleServiceImpl  : isRoleNameExists : " + isRoleNameExists);
		}
		log.info("RoleServiceImpl.isRoleExists() executed successfully" );
		return isRoleNameExists;
	}
	
}
