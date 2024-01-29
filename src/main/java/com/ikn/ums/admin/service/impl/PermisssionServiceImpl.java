package com.ikn.ums.admin.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Strings;
import com.ikn.ums.admin.dto.PermissionDTO;
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
	public PermissionDTO createPermission(PermissionDTO permissionDTO) {
		log.info("createPermission() ENTERED");

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

		log.info("createPermission() is under execution...");

		Permission permission = new Permission();
		permission.setPermissionId(permissionDTO.getPermissionId());
		permission.setPermissionValue(permissionDTO.getPermissionValue());
		permission.setPermissionDescription(permissionDTO.getPermissionDescription());
		//permission.setCreatedDateTime(LocalDateTime.now());
		permission.setPermissionStatus(AdminConstants.STATUS_ACTIVE);

		Permission createdPermission = permissionRepository.save(permission);
		PermissionDTO createdPermissionDTO = new PermissionDTO();
		mapper.map(createdPermission, createdPermissionDTO);
		log.info("createPermission() executed successfully");
		return createdPermissionDTO;
	}

	@Transactional
	@Override
	public PermissionDTO updatePermission(PermissionDTO permissionDTO) {

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
		log.info("updatePermission() is under execution...");
    
		Permission permission = null;
		if(optPermission.isEmpty()) {
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_PERMISSION_DB_ENTITY_NOTFOUND_CODE, 
					ErrorCodeMessages.ERR_PERMISSION_DB_ENTITY_NOTFOUND_MSG);
		}
		permission = optPermission.get();
		permission.setPermissionValue(permissionDTO.getPermissionValue());
		permission.setPermissionDescription(permissionDTO.getPermissionDescription());
		//permission.setModifiedDateTime(LocalDateTime.now());
        permission.setPermissionStatus(AdminConstants.STATUS_ACTIVE);
		Permission updatedPermission = permissionRepository.save(permission);
		log.info("updatePermission() executed successfully");
		PermissionDTO updatedPermissionDTO = new PermissionDTO();
		mapper.map(updatedPermission, updatedPermissionDTO);
		return updatedPermissionDTO;

	}

	@Transactional
	@Override
	public void deletePermissionById(Long permissionId) {
		log.info("deletePermissionById() ENTERED ");
		if (permissionId <= 0) {
			log.info("Permission Id cannot be empty permissionId : " + permissionId);
			throw new EmptyInputException(ErrorCodeMessages.ERR_PERMISSION_ID_IS_EMPTY_CODE,
					ErrorCodeMessages.ERR_PERMISSION_ID_IS_EMPTY_MSG);
		}

		log.info("deletePermissionById() is under execution...");
		Optional<Permission> optPermission = permissionRepository.findById(permissionId);

		if (optPermission.isEmpty()) {
			log.info("Permission is not found");
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_PERMISSION_ENTITY_IS_NULL_CODE,
					ErrorCodeMessages.ERR_PERMISSION_ENTITY_IS_NULL_MSG);
		}
		Permission permission = optPermission.get();
		//Check Permissions Usage
		Long rowsFound = permissionRepository.countPermissionUsage(permission.getPermissionId());
		if (rowsFound > 0) {
			log.info("Permissions are assigned to Role and cannot be deleted ! : " + rowsFound );
			// permission cannot be deleted as they are already in use
			throw new PermissionInUsageException(ErrorCodeMessages.ERR_PERMISSION_IS_IN_USAGE_CODE,
					ErrorCodeMessages.ERR_PERMISSION_IS_IN_USAGE_MSG);
		}
		permission.setPermissionStatus(AdminConstants.STATUS_IN_ACTIVE);
		permissionRepository.save(permission);
		log.info("deletePermissionById() executed successfully !");
		
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
		
		permissionIds.forEach(id -> {
			Long rowsFound = permissionRepository.countPermissionUsage(id);
			if (rowsFound > 0) {
				log.info("Permissions are assigned to Role and cannot be deleted ! : " + rowsFound );
				// permission cannot be deleted as they are already in use
				throw new PermissionInUsageException(ErrorCodeMessages.ERR_PERMISSION_IS_IN_USAGE_CODE,
						ErrorCodeMessages.ERR_PERMISSION_IS_IN_USAGE_MSG);
			}
		});
		
		if (permissionList.size() > 0) {
			permissionList.forEach(permission -> {
				permission.setPermissionStatus(AdminConstants.STATUS_IN_ACTIVE);
			});
		}
	}

	@Override
	public PermissionDTO getPermissionById(Long permissionId) {
		log.info("PermisssionServiceImpl.getPermissionById() ENTERED ");
		if (permissionId <= 0) {
			log.info("Permission Id is null !");
			throw new EmptyInputException(ErrorCodeMessages.ERR_PERMISSION_ID_IS_EMPTY_CODE,
					ErrorCodeMessages.ERR_PERMISSION_ID_IS_EMPTY_MSG);
		}
		Optional<Permission> optPermission = permissionRepository.findById(permissionId);

		if (!optPermission.isPresent()) {
			log.info("Permission is not found for Permission id : " + permissionId);
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_PERMISSION_ENTITY_IS_NULL_CODE,
					ErrorCodeMessages.ERR_PERMISSION_ENTITY_IS_NULL_MSG);
		}
		Permission permission = optPermission.get();
		PermissionDTO permissionDTO = new PermissionDTO();
		mapper.map(permission, permissionDTO);
		return permissionDTO;
	}
	
	@Override
	public List<PermissionDTO> getAllPermissions() {
		List<PermissionDTO> permissionDTOList = new ArrayList<>();
		log.info("getAllPermissions() ENTERED " );
		List<Permission> permissionList = null;
		log.info("getAllPermissions() is under execution...");
		permissionList = permissionRepository.findAllPermissions(AdminConstants.STATUS_ACTIVE);
		if ( permissionList == null || permissionList.isEmpty() || permissionList.size() == 0 )
			throw new EmptyListException(ErrorCodeMessages.ERR_PERMISSION_LIST_IS_EMPTY_CODE,
					ErrorCodeMessages.ERR_PERMISSION_LIST_IS_EMPTY_MSG);
		permissionList.forEach(p -> {
			PermissionDTO dto = new PermissionDTO();
			mapper.map(p, dto);
			permissionDTOList.add(dto);
		});
		log.info("getAllPermissions() : Total Permissions Count : " + permissionList.size());
		log.info("getAllPermissions() executed successfully");
		return permissionDTOList;
	}

	public boolean isPermissionValueExists(String permissionValue) {
		boolean isPermissionNameExists = false;

		if (permissionValue == null) {
			log.info("isPermissionValueExists() Permission Name is empty");
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_PERMISSION_VALUE_IS_NULL_CODE,
					ErrorCodeMessages.ERR_PERMISSION_VALUE_IS_NULL_MSG);
		} else {
			log.info("isPermissionValueExists()  : permissionValue : " + permissionValue);
			Optional<Permission> optPermission = permissionRepository.findByPermissionValue(permissionValue);

			isPermissionNameExists = optPermission.isPresent();
			log.info("PermisssionServiceImpl  : isPermissionValueExists : " + isPermissionNameExists);
		}
		log.info("isPermissionValueExists() executed successfully");
		return isPermissionNameExists;
	}
	
}
