package com.ikn.ums.admin.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ikn.ums.admin.dto.MenuItemDTO;
import com.ikn.ums.admin.dto.RoleDTO;
import com.ikn.ums.admin.entity.MenuItem;
import com.ikn.ums.admin.entity.Role;
import com.ikn.ums.admin.exception.EmptyInputException;
import com.ikn.ums.admin.exception.EmptyListException;
import com.ikn.ums.admin.exception.EntityNotFoundException;
import com.ikn.ums.admin.exception.ErrorCodeMessages;
import com.ikn.ums.admin.exception.RoleInUsageException;
import com.ikn.ums.admin.exception.RoleNameExistsException;

import com.ikn.ums.admin.repository.RoleRepository;
import com.ikn.ums.admin.service.RoleService;
import com.ikn.ums.admin.utils.AdminConstants;
import com.netflix.servo.util.Strings;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private ModelMapper mapper;
  
	@Override
	public RoleDTO createRole(RoleDTO role) {
		log.info("createRole() ENTERED");
		if (role == null) 
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_ROLE_ENTITY_IS_NULL_CODE,
					ErrorCodeMessages.ERR_ROLE_ENTITY_IS_NULL_MSG);
		if (isRoleNameExists(role)) 
			throw new RoleNameExistsException(ErrorCodeMessages.ERR_ROLE_NAME_EXISTS_CODE,
					ErrorCodeMessages.ERR_ROLE_NAME_EXISTS_MSG);
		log.info("createRole() is under execution...");
		role.setCreatedDateTime(LocalDateTime.now());
		role.setRoleStatus(AdminConstants.STATUS_ACTIVE);	
		Role entity = new Role();
		mapper.map(role, entity);
		Role savedRole = roleRepository.save(entity);
		RoleDTO savedRoleDTO = new RoleDTO();
		mapper.map(savedRole, savedRoleDTO);
		log.info("createRole() executed successfully");
		return savedRoleDTO;
	}
	
	
	@Transactional 
	@Override
	public RoleDTO updateRole(RoleDTO role) {
		log.info("updateRole() entered with args - role");
		if(role == null || role.equals(null)) {
			log.info("updateRole() EntityNotFoundException : user object is null");
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_ROLE_ENTITY_IS_NULL_CODE, 
					ErrorCodeMessages.ERR_ROLE_ENTITY_IS_NULL_MSG);
		}
		log.info("updateRole() is under execution...");
		Optional<Role> optRole = roleRepository.findById(role.getRoleId());
		Role dbRole = null;
		if(optRole.isEmpty()) {
			log.info("updateRole() EntityNotFoundException : DB user object is null");
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_ROLE_DBROLE_ENTITY_NOTFOUND_CODE, 
					ErrorCodeMessages.ERR_ROLE_DBROLE_ENTITY_NOTFOUND_MSG);
		}
		dbRole = optRole.get();
		//set modified date time
		role.setModifiedDateTime(LocalDateTime.now());
		mapper.map(role, dbRole);
		List<MenuItemDTO> menuItemsDTOList = role.getMenuItems();
		List<MenuItem> menuItemList = new ArrayList<>();
		menuItemsDTOList.forEach(mdto -> {
			MenuItem mentity = new MenuItem();
			mapper.map(mdto, mentity);
			menuItemList.add(mentity);
		});
		dbRole.setMenuItems(menuItemList);
		Role updatedRole =  roleRepository.save(dbRole);
		RoleDTO updatedRoleDTO = new RoleDTO();
		mapper.map(updatedRole, updatedRoleDTO);
		log.info("updateRole() executed successfully.");
		return updatedRoleDTO;
	}

	@Transactional 
	@Override
	public void deleteRole(Long roleId) {
		log.info("deleteRole() ENTERED : roleId : " + roleId);
		if (roleId <= 0)
			throw new EmptyInputException(ErrorCodeMessages.ERR_ROLE_ID_IS_EMPTY_CODE,
					ErrorCodeMessages.ERR_ROLE_ID_IS_EMPTY_MSG);
		//retrieve the role details
		log.info("deleteRole() is under execution...");
		Optional<Role> optRole = roleRepository.findById(roleId);
		
		if ( !optRole.isPresent() || optRole == null ) {
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_ROLE_DBROLE_ENTITY_NOTFOUND_CODE,
					ErrorCodeMessages.ERR_ROLE_DBROLE_ENTITY_NOTFOUND_MSG);
		} else {
			Long roleCount = roleRepository.findAssignedRoleCount(roleId);
			if(roleCount > 0) {
				throw new RoleInUsageException(ErrorCodeMessages.ERR_ROLE_IS_IN_USAGE_CODE, 
						ErrorCodeMessages.ERR_ROLE_IS_IN_USAGE_MSG);
			}
			roleRepository.deleteById(roleId);
		}
		log.info("deleteRole() executed successfully");
	}

	@Transactional 
	@Override
	public void deleteSelectedRolesByIds(List<Long> roleIds) {
		log.info("deleteSelectedRolesByIds() ENTERED : roleIds Size : " + roleIds.size() );
		if ( roleIds.size() <= 0 )
			throw new EmptyListException(ErrorCodeMessages.ERR_ROLE_LIST_IS_EMPTY_CODE,
					ErrorCodeMessages.ERR_ROLE_LIST_IS_EMPTY_MSG);		
		//check if role is attached to user
		log.info("deleteSelectedRolesByIds() is under execution..." );
		roleIds.forEach(roleId -> {
			Long roleCount = roleRepository.findAssignedRoleCount(roleId);
			if(roleCount > 0) {
				throw new RoleInUsageException(ErrorCodeMessages.ERR_ROLE_IS_IN_USAGE_CODE, 
						ErrorCodeMessages.ERR_ROLE_IS_IN_USAGE_MSG);
			}
		});
		log.info("deleteSelectedRolesByIds() executed successfully" );
		roleRepository.deleteAllById(roleIds);
	}
	@Override
	public List<RoleDTO> getAllRoles() {
		log.info("getAllRoles() entered.");
		List<Role> roleList = null;
		log.info("getAllRoles() is under execution...");
		roleList = roleRepository.findAllRoles( AdminConstants.STATUS_ACTIVE );
		List<RoleDTO> roleDTOList = new ArrayList<>();
		roleList.forEach(role -> {
			RoleDTO dto = new RoleDTO();
			mapper.map(role, dto);
			roleDTOList.add(dto);
		});
		log.info("getAllRoles() executed successfully");
		return roleDTOList;
	}

	@Override
	public RoleDTO getRoleById(Long roleId) {
		log.info("RoleServiceImpl.getRoleById() ENTERED : roleId : " + roleId);
		if (roleId <= 0) {
			log.info("RoleServiceImpl.getRoleById() EmptyInputException : roleId is empty or zero.");
			throw new EmptyInputException(ErrorCodeMessages.ERR_ROLE_ID_IS_EMPTY_CODE,
					ErrorCodeMessages.ERR_ROLE_ID_IS_EMPTY_MSG);
		}
		log.info("getRoleById() is under execution...");
		Optional<Role> optRole = roleRepository.findById(roleId);
	    Role role = optRole.orElseThrow(() -> new EntityNotFoundException(
	            ErrorCodeMessages.ERR_ROLE_DBROLE_ENTITY_NOTFOUND_CODE,
	            ErrorCodeMessages.ERR_ROLE_DBROLE_ENTITY_NOTFOUND_MSG));
	    RoleDTO roleDTO = new RoleDTO();
	    mapper.map(role, roleDTO);
	    log.info("getRoleById() executed successfully");
	    return roleDTO;
	}

	@Override
	public RoleDTO getRoleByName(String roleName) {
		log.info("getRoleByName() ENTERED : roleName : " + roleName);
		log.info("getRoleByName() is under execution...");
		if ( Strings.isNullOrEmpty(roleName)|| roleName.isEmpty()) {
		    log.info("getRoleByName() EmptyInputException : roleName is empty or null.");
			throw new EmptyInputException(ErrorCodeMessages.ERR_ROLE_NAME_IS_EMPTY_CODE,
					ErrorCodeMessages.ERR_ROLE_NAME_IS_EMPTY_MSG);
		}
		Optional<Role> optRole = roleRepository.findByRoleName(roleName);
	    Role role = optRole.orElseThrow(() -> new EntityNotFoundException(
	            ErrorCodeMessages.ERR_ROLE_DBROLE_ENTITY_NOTFOUND_CODE,
	            ErrorCodeMessages.ERR_ROLE_DBROLE_ENTITY_NOTFOUND_MSG));
	    RoleDTO roleDTO = new RoleDTO();
	    mapper.map(role, roleDTO);
		log.info("getRoleByName() executed successfully");
	    return roleDTO;
	}

	public boolean isRoleNameExists(RoleDTO role) {
		log.info("isRoleExists() ENTERED : role : " );
		boolean isRoleNameExists = false;	
		if (role == null) {
			  log.info("RoleServiceImpl.getRoleByName() EmptyInputException : role object is null.");
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_ROLE_ENTITY_IS_NULL_CODE,
					ErrorCodeMessages.ERR_ROLE_ENTITY_IS_NULL_MSG);
		} else {
			log.info("isRoleExists() is under execution..." );
			log.info("RoleServiceImpl  : Role Id : " + role.getRoleId() + " || Role Name : " + role.getRoleName());
			Optional<Role> optRole = roleRepository.findByRoleName( role.getRoleName() );
			isRoleNameExists = optRole.isPresent();
			log.info("isRoleNameExists : " + isRoleNameExists);
		}
		log.info("isRoleExists() executed successfully" );
		return isRoleNameExists;
	}
	
}
