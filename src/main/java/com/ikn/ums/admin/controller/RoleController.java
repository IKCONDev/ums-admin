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

import com.ikn.ums.admin.entity.Role;
import com.ikn.ums.admin.exception.ControllerException;
import com.ikn.ums.admin.exception.EmptyInputException;
import com.ikn.ums.admin.exception.EntityNotFoundException;
import com.ikn.ums.admin.exception.ErrorCodeMessages;
import com.ikn.ums.admin.exception.RoleNameExistsException;
import com.ikn.ums.admin.service.RoleService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/roles")
public class RoleController {

	@Autowired
	private RoleService roleService;

	@GetMapping("/all")
	public ResponseEntity<?> getAllRoles() {
		log.info("RoleController.getAllRoles() ENTERED.");
		try {
			log.info("RoleController.getAllRoles() is under execution...");
			List<Role> rolesList = roleService.getAllRoles();
			log.info("RoleController.getAllRoles() executed successfully");
			return new ResponseEntity<>(rolesList, HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			log.info("RoleController.getAllRoles() exited with exception : Exception occured fetching roles list."
					+ e.getMessage());
			throw new ControllerException(ErrorCodeMessages.ERR_ROLE_LIST_IS_EMPTY_CODE,
					ErrorCodeMessages.ERR_ROLE_LIST_IS_EMPTY_MSG);
		}

	}

	@GetMapping("/{roleId}")
	public ResponseEntity<?> getRoleById(@PathVariable Long roleId) {
		log.info("RoleController.getRoleById() ENTERED : roleId : " + roleId);
		if (roleId < 1)
			throw new EmptyInputException(ErrorCodeMessages.ERR_ROLE_ID_IS_EMPTY_CODE,
					ErrorCodeMessages.ERR_ROLE_ID_IS_EMPTY_MSG);
		Optional<Role> role = roleService.getRoleById(roleId);
		return role.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PostMapping("/create")
	public ResponseEntity<Role> createRole(@RequestBody Role role) {
		log.info("RoleController.createRole() entered with args - role");
		if (role == null || role.equals(null)) {
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_ROLE_ENTITY_IS_NULL_CODE,
					ErrorCodeMessages.ERR_ROLE_ENTITY_IS_NULL_MSG);
		}
		try {
			log.info("RoleController.createRole() is under execution...");
			Role createdRole = roleService.createRole(role);
			log.info("RoleController.createRole() executed successfully.");
			return new ResponseEntity<>(createdRole, HttpStatus.CREATED);
		} catch (Exception e) {
			log.info(" in exception before calling controller exception .... ");
//			ControllerException umsCE = new ControllerException(ErrorCodeMessages.ERR_ROLE_CREATE_UNSUCCESS_CODE,
//					ErrorCodeMessages.ERR_ROLE_CREATE_UNSUCCESS_MSG);
			ControllerException umsCE = new ControllerException(ErrorCodeMessages.ERR_ROLE_CREATE_UNSUCCESS_CODE,
					ErrorCodeMessages.ERR_ROLE_CREATE_UNSUCCESS_MSG);
			throw umsCE;
		}
	}

	@PutMapping("/update")
	public ResponseEntity<Role> updateRole(@RequestBody Role role) {
		log.info("RoleController.updateRole() entered with args - role");
		if (role == null || role.equals(null)) {
			log.info("UserController.updateRole() EntityNotFoundException : Updated Role object is null ");
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_ROLE_ENTITY_IS_NULL_CODE,
					ErrorCodeMessages.ERR_ROLE_ENTITY_IS_NULL_MSG);
		}
		try {
			log.info("RoleController.updateRole() is under execution...");
			Role updatedRole = roleService.updateRole(role);
			log.info("RoleController.updateRole() executed successfully.");
			return new ResponseEntity<>(updatedRole, HttpStatus.CREATED);
		} catch (Exception e) {
			log.info("RoleController.updateRole() exited with exception : Exception occured while updating user.");
			ControllerException umsCE = new ControllerException(e.getCause().toString(), e.getMessage());
			throw umsCE;
		}
	}

	@DeleteMapping("/delete/{roleId}")
	public ResponseEntity<?> deleteRole(@PathVariable Long roleId) {

		boolean isDeleted = false;
		log.info("RoleController.deleteRole() entered with args - roleId : " + roleId);
		if (roleId.equals("") || roleId == null || roleId <= 0) {
			log.info("RoleController.deleteRole() EmptyInputException : role Id is empty");
			throw new EmptyInputException(ErrorCodeMessages.ERR_ROLE_ID_IS_EMPTY_CODE,
					ErrorCodeMessages.ERR_ROLE_ID_IS_EMPTY_MSG);
		}
		try {
			log.info("RoleController.deleteRole() is under execution...");
			roleService.deleteRole(roleId);
			isDeleted = true;
			log.info("RoleController.deleteRole() executed successfully");
			return new ResponseEntity<>(isDeleted, HttpStatus.OK);
		} catch (Exception e) {
			log.info("RoleController.deleteRole() exited with exception : Exception occured while deleting role.");
			ControllerException umsCE = new ControllerException(e.getCause().toString(), e.getMessage());
			throw umsCE;
		}
	}

	@DeleteMapping("/all/{ids}")
	public ResponseEntity<?> deleteSelectedRoles(@PathVariable List<Long> ids) {

		boolean isDeleted = false;
		log.info("RoleController.deleteSelectedRoles() entered with args - ids : ");
		if (ids.equals(null) || ids == null || ids.size() < 1) {
			log.info("RoleController.deleteSelectedRoles() EmptyInputException : role Id is empty");
			throw new EmptyInputException(ErrorCodeMessages.ERR_ROLE_ID_IS_EMPTY_CODE,
					ErrorCodeMessages.ERR_ROLE_ID_IS_EMPTY_MSG);
		}
		try {
			log.info("RoleController.deleteSelectedRoles() is under execution...");
			roleService.deleteRolesbyIds(ids);
			isDeleted = true;
			log.info("RoleController.deleteSelectedRoles() executed successfully");
			return new ResponseEntity<>(isDeleted, HttpStatus.OK);
		} catch (Exception e) {
			log.info(
					"RoleController.deleteSelectedRoles() exited with exception : Exception occured while deleting role.");
			ControllerException umsCE = new ControllerException(e.getCause().toString(), e.getMessage());
			throw umsCE;
		}
	}

}
