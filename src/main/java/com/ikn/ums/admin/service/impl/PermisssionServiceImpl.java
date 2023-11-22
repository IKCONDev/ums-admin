package com.ikn.ums.admin.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Strings;
import com.ikn.ums.admin.dto.PermissionDTO;
import com.ikn.ums.admin.entity.MenuItem;
import com.ikn.ums.admin.entity.Permission;
import com.ikn.ums.admin.exception.EmptyInputException;
import com.ikn.ums.admin.exception.EmptyListException;
import com.ikn.ums.admin.exception.EntityNotFoundException;
import com.ikn.ums.admin.exception.ErrorCodeMessages;
import com.ikn.ums.admin.exception.PermissionInUsageException;
import com.ikn.ums.admin.exception.PermissionNameExistsException;
import com.ikn.ums.admin.repository.PermissionRepository;
import com.ikn.ums.admin.service.PermissionService;
import com.ikn.ums.admin.utils.AdminConstants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PermisssionServiceImpl implements PermissionService {

	@Autowired
	private PermissionRepository permissionRepository;

	@Autowired
	private ModelMapper mapper;

	@Transactional
	@Override
	public Permission createPermission(PermissionDTO permissionDTO) {
		log.info("PermisssionServiceImpl.createPermission() ENTERED");

		if (permissionDTO == null) {
			log.info("Permission Object is null .... ");
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_PERMISSION_ENTITY_IS_NULL_CODE,
					ErrorCodeMessages.ERR_PERMISSION_ENTITY_IS_NULL_MSG);
		}
		if (Strings.isNullOrEmpty(permissionDTO.getPermissionValue())) {
			log.info("Permission Value is null .... ");
			throw new EmptyInputException(ErrorCodeMessages.ERR_PERMISSION_VALUE_IS_EMPTY_CODE,
					ErrorCodeMessages.ERR_PERMISSION_VALUE_IS_EMPTY_MSG);
		}
		if (isPermissionValueExists(permissionDTO.getPermissionValue())) {
			log.info("Permission Value, " + permissionDTO.getPermissionValue() + " already exists.");
			throw new PermissionNameExistsException(ErrorCodeMessages.ERR_PERMISSION_VALUE_EXISTS_CODE,
					ErrorCodeMessages.ERR_PERMISSION_VALUE_EXISTS_MSG);
		}

		log.info("PermisssionServiceImpl.createPermission() is under execution...");

		Permission permission = new Permission();
		permission.setPermissionId(permissionDTO.getPermissionId());
		permission.setPermissionValue(permissionDTO.getPermissionValue());
		permission.setPermissionDescription(permissionDTO.getPermissionDescription());
		permission.setCreatedDateTime(LocalDateTime.now());
		permission.setPermissionStatus(AdminConstants.STATUS_ACTIVE);

		Permission createdPermission = permissionRepository.save(permission);
		log.info("PermisssionServiceImpl.createPermission() executed successfully");
		return createdPermission;
	}

	@Transactional
	@Override
	public Permission updatePermission(PermissionDTO permissionDTO) {

		log.info("PermisssionServiceImpl.updatePermission() ENTERED");
		if (permissionDTO == null) {
			log.info("Permission data cannot be null !");
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_PERMISSION_ENTITY_IS_NULL_CODE,
					ErrorCodeMessages.ERR_PERMISSION_ENTITY_IS_NULL_MSG);
		}

		Long permissionId = permissionDTO.getPermissionId();
		Optional<Permission> optPermission = permissionRepository.findById(permissionId);

		if (!optPermission.isPresent()) {
			throw new EntityNotFoundException(permissionId.toString(),
					"The permission with the id = %s has not been found");
		}

//		if (isPermissionValueExists(permissionDTO.getPermissionValue())) {
//			log.info("Exists already a permission with the Value %s. Use another Value. "
//					+ permissionDTO.getPermissionValue());
//			throw new PermissionNameExistsException(ErrorCodeMessages.ERR_PERMISSION_VALUE_EXISTS_CODE,
//					ErrorCodeMessages.ERR_PERMISSION_VALUE_EXISTS_CODE);
//		}
		log.info("PermisssionServiceImpl.updatePermission() is under execution...");
    
		Permission permission = null;
		if(optPermission.isPresent()) {
			permission = optPermission.get();
		}
		permission.setPermissionValue(permissionDTO.getPermissionValue());
		permission.setPermissionDescription(permissionDTO.getPermissionDescription());
		permission.setModifiedDateTime(LocalDateTime.now());
        permission.setPermissionStatus(AdminConstants.STATUS_ACTIVE);
		Permission updatedPermission = permissionRepository.save(permission);
		log.info("PermisssionServiceImpl.updatePermission() executed successfully");
		return updatedPermission;

	}

	@Transactional
	@Override
	public void deletePermissionById(Long permissionId) {
		log.info("PermisssionServiceImpl.deletePermissionById() ENTERED ");
		if (permissionId <= 0) {
			log.info("Permission Id cannot be empty permissionId : " + permissionId);
			throw new EmptyInputException(ErrorCodeMessages.ERR_PERMISSION_ID_IS_EMPTY_CODE,
					ErrorCodeMessages.ERR_PERMISSION_ID_IS_EMPTY_MSG);
		}

		log.info("PermisssionServiceImpl.deletePermissionById() is under execution...");
		Optional<Permission> optPermission = permissionRepository.findById(permissionId);

		if (!optPermission.isPresent() || optPermission == null) {
			log.info("Permission is not found");
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_PERMISSION_ENTITY_IS_NULL_CODE,
					ErrorCodeMessages.ERR_PERMISSION_ENTITY_IS_NULL_MSG);
		}

		Permission permission = optPermission.get();

		//Check Permissions Usage
//		Long rowsFound = permissionRepository.countPermissionUsage(permission.getPermissionId());
//		if (rowsFound > 0) {
//			log.info("Permissions are assigned to Role and cannot be deleted ! : " + rowsFound );
//			// permission cannot be deleted as they are already in use
//			throw new PermissionInUsageException(ErrorCodeMessages.ERR_PERMISSION_IS_IN_USAGE_CODE,
//					ErrorCodeMessages.ERR_PERMISSION_IS_IN_USAGE_MSG);
//		}
		
		//TODO: Need to implement the logic for each Permission Id, we have to check if the menu item is in usage
		//PermissionDTO permissionDTO = new PermissionDTO();
		permission.setPermissionStatus(AdminConstants.STATUS_IN_ACTIVE);
		permissionRepository.save(permission);
		 //Calling Update method to set Status to In Active
		log.info("PermisssionServiceImpl.deletePermissionById() executed successfully !");
		
	}

	@Override
	public void deleteSelectedPermissionsByIds(List<Long> permissionIds) {
		
		log.info("PermisssionServiceImpl.deleteSelectedPermissionsByIds() ENTERED " );
		
		if ( permissionIds.size() <= 0 ) {
			log.info(": permissionIds Size : " + permissionIds.size() );
			throw new EmptyListException(ErrorCodeMessages.ERR_ROLE_LIST_IS_EMPTY_CODE,
					ErrorCodeMessages.ERR_ROLE_LIST_IS_EMPTY_MSG);	
		}
				
		List<Permission> permissionList = permissionRepository.findAllById(permissionIds);
		
		//TODO: Need to implement the logic for each Permission Id, we have to check if the permission is in usage
		
		if (permissionList.size() > 0) {
			permissionList.forEach(permission -> {
				permission.setPermissionStatus(AdminConstants.STATUS_IN_ACTIVE);
			});
		}
	}

	@Override
	public Optional<Permission> getPermissionById(Long permissionId) {
		log.info("PermisssionServiceImpl.getPermissionById() ENTERED ");
		
		if (permissionId <= 0) {
			log.info("Permission Id is null !");
			throw new EmptyInputException(ErrorCodeMessages.ERR_PERMISSION_ID_IS_EMPTY_CODE,
					ErrorCodeMessages.ERR_PERMISSION_ID_IS_EMPTY_MSG);
		}
		
		Optional<Permission> optPermission = permissionRepository.findById(permissionId);

		if (!optPermission.isPresent() || optPermission == null) {
			log.info("Permission is not found for Permission id : " + permissionId);
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_PERMISSION_ENTITY_IS_NULL_CODE,
					ErrorCodeMessages.ERR_PERMISSION_ENTITY_IS_NULL_MSG);
		}
		
		return optPermission;
	}
	
	@Override
	public List<Permission> getAllPermissions() {
		log.info(" PermisssionServiceImpl.getAllPermissions() ENTERED " );
		List<Permission> permissionList = null;
		log.info("getAllPermissions() is under execution...");
		permissionList = permissionRepository.findAllPermissions(AdminConstants.STATUS_ACTIVE);
		if ( permissionList == null || permissionList.isEmpty() || permissionList.size() == 0 )
			throw new EmptyListException(ErrorCodeMessages.ERR_PERMISSION_LIST_IS_EMPTY_CODE,
					ErrorCodeMessages.ERR_PERMISSION_LIST_IS_EMPTY_MSG);
		log.info("getAllPermissions() : Total Permissions Count : " + permissionList.size());
		log.info("getAllPermissions() executed successfully");
		return permissionList;
	}

	public boolean isPermissionValueExists(String permissionValue) {
		boolean isPermissionNameExists = false;

		if (permissionValue == null) {
			log.info("PermisssionServiceImpl.isPermissionValueExists() Permission Name is empty");
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_PERMISSION_VALUE_IS_NULL_CODE,
					ErrorCodeMessages.ERR_PERMISSION_VALUE_IS_NULL_MSG);
		} else {
			log.info("PermisssionServiceImpl.isPermissionValueExists()  : permissionValue : " + permissionValue);
			Optional<Permission> optPermission = permissionRepository.findByPermissionValue(permissionValue);

			isPermissionNameExists = optPermission.isPresent();
			log.info("PermisssionServiceImpl  : isPermissionValueExists : " + isPermissionNameExists);
		}
		log.info("PermisssionServiceImpl.isPermissionValueExists() executed successfully");
		return isPermissionNameExists;
	}
	
}
