package com.ikn.ums.admin.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ikn.ums.admin.dto.PermissionDTO;
import com.ikn.ums.admin.entity.Permission;
import com.ikn.ums.admin.exception.ControllerException;
import com.ikn.ums.admin.exception.EmptyInputException;
import com.ikn.ums.admin.exception.EmptyListException;
import com.ikn.ums.admin.exception.EntityNotFoundException;
import com.ikn.ums.admin.exception.ErrorCodeMessages;
import com.ikn.ums.admin.exception.PermissionInUsageException;
import com.ikn.ums.admin.exception.PermissionNameExistsException;
import com.ikn.ums.admin.service.PermissionService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/permission")
public class PermissionController {

	@Autowired
	private PermissionService permissionService;

	@PostMapping("/create")
	public ResponseEntity<Permission> createPermission(@RequestBody PermissionDTO permissionDTO) {
		
		log.info("PermissionController.createPermission() entered ");
		if (permissionDTO == null || permissionDTO.equals(null)) {
			log.info("Permission Entity Not Found Exception has encountered while creating Role.");
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_PERMISSION_ENTITY_IS_NULL_CODE,
					ErrorCodeMessages.ERR_PERMISSION_ENTITY_IS_NULL_MSG);
		}
		try {
			log.info("PermissionController.createPermission() is under execution.");
			log.info(":Permission Object : " + permissionDTO );
			Permission createdPermission = permissionService.createPermission(permissionDTO);
			log.info("PermissionController.createPermission() executed successfully.");
			return new ResponseEntity<>(createdPermission, HttpStatus.CREATED);
		} catch (EntityNotFoundException | PermissionNameExistsException permissionBusinessException) {
			log.info("Permission Business Exception has encountered while creating permission. " + permissionBusinessException.getMessage());
			throw permissionBusinessException;
		} catch (Exception e) {
			log.error("General Exception has encountered while creating Permission. " + e.getMessage(), e);
			ControllerException umsCE = new ControllerException(ErrorCodeMessages.ERR_PERMISSION_CREATE_UNSUCCESS_CODE,
					ErrorCodeMessages.ERR_PERMISSION_CREATE_UNSUCCESS_MSG);
			throw umsCE;
		}
	}

	@PutMapping("/update")
	public ResponseEntity<Permission> updatePermission(@RequestBody PermissionDTO permissionDTO) {
		log.info("PermissionController.updatePermission() entered with args");
		if (permissionDTO == null || permissionDTO.equals(null)) {
			log.info("Permission Entity Not Found Exception has encountered while updating Role.");
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_PERMISSION_ENTITY_IS_NULL_CODE,
					ErrorCodeMessages.ERR_PERMISSION_ENTITY_IS_NULL_MSG);
		}
		try {
			log.info("PermissionController.updatePermission() is under execution.");
			Permission updatedPermission = permissionService.updatePermission(permissionDTO);
			log.info("PermissionController.updatePermission() is executed sucessfully.");
			return new ResponseEntity<>(updatedPermission, HttpStatus.PARTIAL_CONTENT);
		}catch (EntityNotFoundException permissionBusinessException) {
			log.error("Permission Business Exception has encountered while updating Permission. " + permissionBusinessException.getMessage(), permissionBusinessException);
			throw permissionBusinessException;
		}catch (Exception e) {
			log.error("General Exception has encountered while updating Permission. " + e.getMessage(), e);
			ControllerException umsCE = new ControllerException(ErrorCodeMessages.ERR_PERMISSION_UPDATE_UNSUCCESS_CODE, 
					ErrorCodeMessages.ERR_MENU_ITEM_UPDATE_UNSUCCESS_MSG);
			throw umsCE;
		}
	}

	@DeleteMapping("/{ids}")
	public ResponseEntity<?> deleteSelectedPermissions(@PathVariable("ids") List<Long> permissionIds) {
		boolean isPermissionsDeleted = false;
		log.info("PermissionController.deleteSelectedPermissions() entered ");
		if (permissionIds.equals(null) || permissionIds == null || permissionIds.size() <= 0 ) {
			log.info("PermissionController.deleteSelectedPermissions() EmptyInputException : permission Id is empty");
			throw new EmptyInputException(ErrorCodeMessages.ERR_PERMISSION_ID_IS_EMPTY_CODE,
					ErrorCodeMessages.ERR_PERMISSION_ID_IS_EMPTY_MSG);
		}
		try {
			log.info("PermissionController.deleteSelectedPermissions() is under execution...");
			
			permissionService.deleteSelectedPermissionsByIds(permissionIds);
			isPermissionsDeleted = true;
			log.info("PermissionController.deleteSelectedRoles() executed successfully");
			return new ResponseEntity<>(isPermissionsDeleted, HttpStatus.OK);
		}catch (EmptyListException businessException) {
			throw businessException;
		} 
		catch (Exception e) {
			ControllerException umsCE = new ControllerException(ErrorCodeMessages.ERR_PERMISSION_DELETE_UNSUCCESS_CODE,
					ErrorCodeMessages.ERR_PERMISSION_DELETE_UNSUCCESS_MSG);
			throw umsCE;
		}
	}
	
	@GetMapping("/all")
	public ResponseEntity<?> getAllPermissions() {
		log.info("PermissionController.getAllPermissions() ENTERED.");
		try {
			log.info("PermissionController.getAllPermissions() is under execution...");
			List<Permission> rolesList = permissionService.getAllPermissions();
			log.info("PermissionController.getAllPermissions() executed successfully");
			return new ResponseEntity<>(rolesList, HttpStatus.OK);
		}catch (EmptyListException businessException) {
			throw businessException;
		} 
		catch (Exception e) {
			// TODO: handle exception
			log.error("PermissionController.getAllPermissions() exited with exception : Exception occured fetching permissions list."
					+ e.getMessage(), e);
			throw new ControllerException(ErrorCodeMessages.ERR_PERMISSION_GET_UNSUCCESS_CODE,
					ErrorCodeMessages.ERR_PERMISSION_GET_UNSUCCESS_MSG);
		}

	}

	@GetMapping("/{permissionId}")
	public ResponseEntity<?> getPermissionById(@PathVariable ("permissionId") Long permissionId) {
	
		if (permissionId <= 0) {
			log.info("PermissionController.getPermissionById() permissionId <0 exception ");
			throw new EmptyInputException(ErrorCodeMessages.ERR_ROLE_ID_IS_EMPTY_CODE,
					ErrorCodeMessages.ERR_ROLE_ID_IS_EMPTY_MSG);
		}
		try {
			log.info("PermissionController.getPermissionById() is under execution...");
			log.info("PermissionController.getPermissionById() ENTERED : permissionId : " + permissionId);
			Optional<Permission> permission = permissionService.getPermissionById(permissionId);
			log.info("PermissionController.getPermissionById() executed successfully");
			return permission.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
		}catch (EmptyInputException businessException) {
			log.error("PermissionController.getPermissionById() exited with exception : Business Exception occured while fetching permission."+ businessException.getMessage(), businessException);
			throw businessException;
		}
		catch (Exception e) {
			log.error("PermissionController.getPermissionById() exited with general exception : Exception occured while fetching permission."+ e.getMessage(), e);
			ControllerException umsCE = new ControllerException(ErrorCodeMessages.ERR_PERMISSION_GET_UNSUCCESS_CODE,
					ErrorCodeMessages.ERR_PERMISSION_GET_UNSUCCESS_MSG);
			throw umsCE;
		}
	}
	
	@DeleteMapping("/delete/{permissionId}")
	public ResponseEntity<?> deleteUserByUserId(@PathVariable("permissionId") Long permissionId){
		boolean isDeleted = false;
		log.info("PermissionController.deleteUserByUserId() entered with args - emailId");
		if(permissionId.equals("") || permissionId == null) {
			log.info("PermissionController.deleteUserByUserId() EmptyInputException : userid/emailid is empty");
			throw new EmptyInputException(ErrorCodeMessages.ERR_PERMISSION_ID_IS_EMPTY_CODE,
					ErrorCodeMessages.ERR_PERMISSION_ID_IS_EMPTY_MSG);
		}
		try {
			log.info("PermissionController.deleteUserByUserId() is under execution...");
			permissionService.deletePermissionById(permissionId);
			isDeleted = true;
			log.info("PermissionController.deleteUserByUserId() executed successfully");
			return new ResponseEntity<>(isDeleted, HttpStatus.OK);
		}catch (EmptyInputException | PermissionInUsageException businessException) {
			log.error("PermissionController.getPermissionById() exited with exception : Business Exception occured while fetching permission."+ businessException.getMessage(), businessException);
			throw businessException;
		}catch (Exception e) {
			log.error("PermissionController.deleteUserByUserId() exited with exception : Exception occured while deleting user."+ e.getMessage(), e);
			ControllerException umsCE = new ControllerException(ErrorCodeMessages.ERR_PERMISSION_DELETE_UNSUCCESS_CODE,
			ErrorCodeMessages.ERR_PERMISSION_DELETE_UNSUCCESS_MSG);
			throw umsCE;
		}
	}

}
