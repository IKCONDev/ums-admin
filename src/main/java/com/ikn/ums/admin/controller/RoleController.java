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
import com.ikn.ums.admin.exception.EmptyInputException;
import com.ikn.ums.admin.exception.ErrorCodeMessages;
import com.ikn.ums.admin.service.RoleService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/roles")
public class RoleController {
    
	@Autowired
    private RoleService roleService;

    @GetMapping
    public List<Role> getAllRoles() {
    	log.info("RoleController.getAllRoles() ENTERED.");
    	return roleService.getAllRoles();
    }

    @GetMapping("/{roleId}")
    public ResponseEntity<?> getRoleById(@PathVariable Long roleId) {
    	log.info("RoleController.getRoleById() ENTERED : roleId : " + roleId);
		if (roleId <= 0)
			throw new EmptyInputException(ErrorCodeMessages.ERR_ROLE_ID_IS_EMPTY_CODE,
					ErrorCodeMessages.ERR_ROLE_ID_IS_EMPTY_MSG);
		Optional<Role> role = roleService.getRoleById(roleId);
		return role.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Role> createRole(@RequestBody Role role) {
    	log.info("RoleController.createRole() ENTERED.");
        Role createdRole = roleService.createRole(role);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRole);
    }

    @PutMapping("/{roleId}")
    public ResponseEntity<Role> updateRole(@PathVariable Long roleId, @RequestBody Role updatedRole) {
    	log.info("RoleController.updateRole() ENTERED.");
        Role role = roleService.updateRole(roleId, updatedRole);
        return ResponseEntity.ok(role);
    }

    @DeleteMapping("/{roleId}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long roleId) {
    	log.info("RoleController.deleteRole() ENTERED.");
        roleService.deleteRole(roleId);
        return ResponseEntity.noContent().build();
    }
}
