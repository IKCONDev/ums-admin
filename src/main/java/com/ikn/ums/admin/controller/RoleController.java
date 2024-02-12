package com.ikn.ums.admin.controller;

import java.util.List;

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
import com.ikn.ums.admin.dto.RoleDTO;
import com.ikn.ums.admin.exception.ControllerException;
import com.ikn.ums.admin.exception.EmptyInputException;
import com.ikn.ums.admin.exception.EmptyListException;
import com.ikn.ums.admin.exception.EntityNotFoundException;
import com.ikn.ums.admin.exception.ErrorCodeMessages;
import com.ikn.ums.admin.exception.RoleInUsageException;
import com.ikn.ums.admin.exception.RoleNameExistsException;
import com.ikn.ums.admin.service.PermissionService;
import com.ikn.ums.admin.service.RoleService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/roles")
public class RoleController {

	@Autowired
	private RoleService roleService;
	
	@Autowired
	private PermissionService permissionService;

	@PostMapping("/create")
	public ResponseEntity<RoleDTO> createRole(@RequestBody RoleDTO role) {
		log.info("createRole() entered with args - role");
		if (role == null || role.equals(null)) {
			log.info("createRole() EntityNotFoundException : Role object is null or empty.");
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_ROLE_ENTITY_IS_NULL_CODE,
					ErrorCodeMessages.ERR_ROLE_ENTITY_IS_NULL_MSG);
		}
		try {
			log.info("createRole() is under execution.");
			log.info(":Role Object : " + role );
			//assign the corresponding permission object to role
			var permission = permissionService.getPermissionById(role.getPermission().getPermissionId());
			role.setPermission(permission);
			var createdRole = roleService.createRole(role);
			log.info("createRole() executed successfully.");
			return new ResponseEntity<>(createdRole, HttpStatus.CREATED);
		} catch (EntityNotFoundException | RoleNameExistsException roleBusinessException) {
			log.error("Role Business Exception has encountered while creating Role. " + roleBusinessException.getMessage(), roleBusinessException);
			throw roleBusinessException;
		} catch (Exception e) {
			log.error("General Exception has encountered while creating Role. " + e.getMessage(), e);
			throw new ControllerException(ErrorCodeMessages.ERR_ROLE_CREATE_UNSUCCESS_CODE,
					ErrorCodeMessages.ERR_ROLE_CREATE_UNSUCCESS_MSG);
		}
	}

	@PutMapping("/update")
	public ResponseEntity<RoleDTO> updateRole(@RequestBody RoleDTO role) {
		log.info("updateRole() entered with args - role");
		if (role == null || role.equals(null)) {
			log.info("updateRole() EntityNotFoundException Role object is null or empty.");
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_ROLE_ENTITY_IS_NULL_CODE,
					ErrorCodeMessages.ERR_ROLE_ENTITY_IS_NULL_MSG);
		}
		try {
			log.info("updateRole() is under execution...");
			var updatedRole = roleService.updateRole(role);
			log.info("updateRole() executed successfully.");
			return new ResponseEntity<>(updatedRole, HttpStatus.CREATED);
		}catch (EntityNotFoundException roleBusinessException) {
			log.error("Role Business Exception has encountered while updating Role. " + roleBusinessException.getMessage(), roleBusinessException);
			throw roleBusinessException;
		}catch (Exception e) {
			log.error("General Exception has encountered while updating Role. " + e.getMessage(), e);
			throw new ControllerException(ErrorCodeMessages.ERR_ROLE_UPDATE_UNSUCCESS_CODE, 
					ErrorCodeMessages.ERR_ROLE_UPDATE_UNSUCCESS_MSG);
		}
	}

	@DeleteMapping("/{ids}")
	public ResponseEntity<Boolean> deleteSelectedRoles(@PathVariable("ids") List<Long> roleIds) {
		log.info("deleteSelectedRoles() entered with args - ids : roleIds size (): " + roleIds.size());
		if (roleIds == null || roleIds.isEmpty() ) {
			log.info("deleteSelectedRoles() EmptyInputException : role Id is empty");
			throw new EmptyInputException(ErrorCodeMessages.ERR_ROLE_ID_IS_EMPTY_CODE,
					ErrorCodeMessages.ERR_ROLE_ID_IS_EMPTY_MSG);
		}
		try {
			log.info("deleteSelectedRoles() is under execution...");
			roleService.deleteSelectedRolesByIds(roleIds);
			log.info("deleteSelectedRoles() executed successfully");
			return new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);
		}catch (EmptyListException | RoleInUsageException businessException) {
			log.error("deleteSelectedRoles() exited with Business exception : Exception occured fetching roles list."
					+ businessException.getMessage(), businessException);
			throw businessException;
		} 
		catch (Exception e) {
			log.error("deleteSelectedRoles() exited with exception : Exception occured fetching roles list."
					+ e.getMessage(), e);
			throw new ControllerException(ErrorCodeMessages.ERR_ROLE_DELETE_UNSUCCESS_CODE,
					ErrorCodeMessages.ERR_ROLE_DELETE_UNSUCCESS_MSG);
		}
	}
	
	@GetMapping("/all")
	public ResponseEntity<List<RoleDTO>> getAllRoles() {
		log.info("getAllRoles() ENTERED.");
		try {
			log.info("getAllRoles() is under execution...");
			var rolesList = roleService.getAllRoles();
			log.info("getAllRoles() executed successfully");
			return new ResponseEntity<>(rolesList, HttpStatus.OK);
		}catch (EmptyListException businessException) {
			log.error("getAllRoles() exited with exception : Exception occured fetching roles list."
					+ businessException.getMessage(), businessException);
			throw businessException;
		} 
		catch (Exception e) {
			log.error("getAllRoles() exited with exception : Exception occured fetching roles list."
					+ e.getMessage());
			throw new ControllerException(ErrorCodeMessages.ERR_ROLE_GET_UNSUCCESS_CODE,
					ErrorCodeMessages.ERR_ROLE_GET_UNSUCCESS_MSG);
		}

	}

	@GetMapping("/{roleId}")
	public ResponseEntity<RoleDTO> getRoleById(@PathVariable Long roleId) {
		log.info("getRoleById() ENTERED : roleId : " + roleId);
		if (roleId <= 0|| roleId == null) {
			log.info("getRoleById() EmptyInputException : role Id is empty");
			throw new EmptyInputException(ErrorCodeMessages.ERR_ROLE_ID_IS_EMPTY_CODE,
					ErrorCodeMessages.ERR_ROLE_ID_IS_EMPTY_MSG);
		}
		try {
			log.info("getRoleById() is under execution...");
			var role = roleService.getRoleById(roleId);
			log.info("getRoleById() executed successfully");
			return new ResponseEntity<>(role, HttpStatus.OK);
		}catch (EmptyInputException businessException) {
			log.error("getRoleById() exited with exception : Exception occured fetching role."
					+ businessException.getMessage(), businessException);
			throw businessException;
		}
		catch (Exception e) {
			log.error("getRoleById() exited with exception : Exception occured fetching roles list."
					+ e.getMessage());
			throw new ControllerException(ErrorCodeMessages.ERR_ROLE_GET_UNSUCCESS_CODE,
					ErrorCodeMessages.ERR_ROLE_GET_UNSUCCESS_MSG);
		}
	}

}
