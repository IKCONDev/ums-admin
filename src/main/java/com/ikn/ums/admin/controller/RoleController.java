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

import com.ikn.ums.admin.entity.Permission;
import com.ikn.ums.admin.entity.Role;
import com.ikn.ums.admin.exception.ControllerException;
import com.ikn.ums.admin.exception.EmptyInputException;
import com.ikn.ums.admin.exception.EmptyListException;
import com.ikn.ums.admin.exception.EntityNotFoundException;
import com.ikn.ums.admin.exception.ErrorCodeMessages;
import com.ikn.ums.admin.exception.RoleNameExistsException;
import com.ikn.ums.admin.repository.RoleRepository;
import com.ikn.ums.admin.service.PermissionService;
import com.ikn.ums.admin.service.RoleService;
import com.ikn.ums.admin.service.impl.RoleServiceImpl;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/roles")
public class RoleController {

	@Autowired
	private RoleService roleService;
	
	@Autowired
	private PermissionService permissionService;
	
	@Autowired
	private RoleRepository roleRepo;

	@PostMapping("/create")
	public ResponseEntity<Role> createRole(@RequestBody Role role) {
		log.info("RoleController.createRole() entered with args - role");
		if (role == null || role.equals(null)) {
			log.info("Role Entity Not Found Exception has encountered while creating Role.");
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_ROLE_ENTITY_IS_NULL_CODE,
					ErrorCodeMessages.ERR_ROLE_ENTITY_IS_NULL_MSG);
		}
		try {
			log.info("RoleController.createRole() is under execution.");
			log.info(":Role Object : " + role );
			//assign the corresponding permission object to role
			Optional<Permission> optPermission = permissionService.getPermissionById(role.getPermission().getPermissionId());
			Permission permission = optPermission.get();
			role.setPermission(permission);
			Role createdRole = roleService.createRole(role);
			log.info("RoleController.createRole() executed successfully.");
			return new ResponseEntity<>(createdRole, HttpStatus.CREATED);
		} catch (EntityNotFoundException | RoleNameExistsException roleBusinessException) {
			log.error("Role Business Exception has encountered while creating Role. " + roleBusinessException.getMessage(), roleBusinessException);
			throw roleBusinessException;
		} catch (Exception e) {
			log.error("General Exception has encountered while creating Role. " + e.getMessage(), e);
			ControllerException umsCE = new ControllerException(ErrorCodeMessages.ERR_ROLE_CREATE_UNSUCCESS_CODE,
					ErrorCodeMessages.ERR_ROLE_CREATE_UNSUCCESS_MSG);
			throw umsCE;
		}
	}

	@PutMapping("/update")
	public ResponseEntity<Role> updateRole(@RequestBody Role role) {
		log.info("RoleController.updateRole() entered with args - role");
		if (role == null || role.equals(null)) {
			log.info("Role Entity Not Found Exception has encountered while updating Role.");
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_ROLE_ENTITY_IS_NULL_CODE,
					ErrorCodeMessages.ERR_ROLE_ENTITY_IS_NULL_MSG);
		}
		try {
			log.info("RoleController.updateRole() is under execution...");
			Role updatedRole = roleService.updateRole(role);
			log.info("RoleController.updateRole() executed successfully.");
			return new ResponseEntity<>(updatedRole, HttpStatus.CREATED);
		}catch (EntityNotFoundException roleBusinessException) {
			log.error("Role Business Exception has encountered while updating Role. " + roleBusinessException.getMessage(), roleBusinessException);
			throw roleBusinessException;
		}catch (Exception e) {
			log.error("General Exception has encountered while updating Role. " + e.getMessage(), e);
			ControllerException umsCE = new ControllerException(e.getCause().toString(), e.getMessage());
			throw umsCE;
		}
	}

	@DeleteMapping("/{ids}")
	public ResponseEntity<?> deleteSelectedRoles(@PathVariable("ids") List<Long> roleIds) {
		boolean isRolesDeleted = false;
		log.info("RoleController.deleteSelectedRoles() entered with args - ids : roleIds size (): " + roleIds.size());
		if (roleIds.equals(null) || roleIds == null || roleIds.size() <= 0 ) {
			log.info("RoleController.deleteSelectedRoles() EmptyInputException : role Id is empty");
			throw new EmptyInputException(ErrorCodeMessages.ERR_ROLE_ID_IS_EMPTY_CODE,
					ErrorCodeMessages.ERR_ROLE_ID_IS_EMPTY_MSG);
		}
		try {
			log.info("RoleController.deleteSelectedRoles() is under execution...");
			roleService.deleteSelectedRolesByIds(roleIds);
			isRolesDeleted = true;
			log.info("RoleController.deleteSelectedRoles() executed successfully");
			return new ResponseEntity<>(isRolesDeleted, HttpStatus.OK);
		}catch (EmptyListException businessException) {
			throw businessException;
		} 
		catch (Exception e) {
			ControllerException umsCE = new ControllerException(ErrorCodeMessages.ERR_ROLE_DELETE_UNSUCCESS_CODE,
					ErrorCodeMessages.ERR_ROLE_DELETE_UNSUCCESS_MSG);
			throw umsCE;
		}
	}
	
	@GetMapping("/all")
	public ResponseEntity<?> getAllRoles() {
		log.info("RoleController.getAllRoles() ENTERED.");
		try {
			log.info("RoleController.getAllRoles() is under execution...");
			List<Role> rolesList = roleService.getAllRoles();
			log.info("RoleController.getAllRoles() executed successfully");
			return new ResponseEntity<>(rolesList, HttpStatus.OK);
		}catch (EmptyListException businessException) {
			throw businessException;
		} 
		catch (Exception e) {
			// TODO: handle exception
			log.info("RoleController.getAllRoles() exited with exception : Exception occured fetching roles list."
					+ e.getMessage());
			throw new ControllerException(ErrorCodeMessages.ERR_ROLE_GET_UNSUCCESS_CODE,
					ErrorCodeMessages.ERR_ROLE_GET_UNSUCCESS_MSG);
		}

	}

	@GetMapping("/{roleId}")
	public ResponseEntity<?> getRoleById(@PathVariable Long roleId) {
		log.info("RoleController.getRoleById() ENTERED : roleId : " + roleId);
		if (roleId <= 0)
			throw new EmptyInputException(ErrorCodeMessages.ERR_ROLE_ID_IS_EMPTY_CODE,
					ErrorCodeMessages.ERR_ROLE_ID_IS_EMPTY_MSG);
		try {
			log.info("RoleController.getRoleById() is under execution...");
			Optional<Role> role = roleService.getRoleById(roleId);
			log.info("RoleController.getRoleById() executed successfully");
			return role.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
		}catch (EmptyInputException businessException) {
			throw businessException;
		}
		catch (Exception e) {
			ControllerException umsCE = new ControllerException(ErrorCodeMessages.ERR_ROLE_GET_UNSUCCESS_CODE,
					ErrorCodeMessages.ERR_ROLE_GET_UNSUCCESS_MSG);
			throw umsCE;
		}
	}

//Testing
	
//	@GetMapping("/dynamicMenuItems")
//	private String[] getDynamicAntMatchers() {
//		// Fetch dynamic menu items based on the user's roles
//		//RoleController roleController = new RoleController();
//		
//		RoleService roleService = new RoleServiceImpl();
//		
//		List<Role> roleList = roleService.getAllRoles();
//	
//		return roleList.stream().map(menuItem -> menuItem.getMenuItems()).toArray(String[]::new);
//		
//		//List<Role> dynamicMenuItems = roleController.getAllRoles();
//
//		// Convert dynamic menu items to antMatchers strings
//		//return dynamicMenuItems.stream().map(menuItem -> "/v1/" + menuItem.getPath()) // Adjust the path as needed
//				//.toArray(String[]::new);
//	}

}
