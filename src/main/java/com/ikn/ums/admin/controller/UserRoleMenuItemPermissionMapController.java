package com.ikn.ums.admin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ikn.ums.admin.dto.UserRoleMenuItemPermissionMapDTO;
import com.ikn.ums.admin.exception.EntityNotFoundException;
import com.ikn.ums.admin.exception.ErrorCodeMessages;
import com.ikn.ums.admin.service.UserRoleMenuItemPermissionMapService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/userRoleMenuPermissionMap")
@Slf4j
public class UserRoleMenuItemPermissionMapController {
	
	@Autowired
	private UserRoleMenuItemPermissionMapService userRoleMenuItemPermissionMapService;
	
	@GetMapping("/all/{userId}")
	public ResponseEntity<?> getAllUserRoleMenuItemPermissionMaps(@PathVariable("userId") String emailId){
		log.info("getAllUserRoleMenuItemPermissionMaps() entered with args : userId/emailId - "+emailId);
		log.info("getAllUserRoleMenuItemPermissionMaps() is under execution...");
		List<UserRoleMenuItemPermissionMapDTO> dtoList = userRoleMenuItemPermissionMapService.getUserRoleMenuItemPermissionMapsByUserId(emailId);
		log.info("getAllUserRoleMenuItemPermissionMaps() executed succesfully");
		return new ResponseEntity<>(dtoList, HttpStatus.OK);
	}
	
	@PutMapping("/update")
	public ResponseEntity<?> updateUserRoleMenuItemPermissionMap(@RequestBody UserRoleMenuItemPermissionMapDTO dto){
		log.info("updateUserRoleMenuItemPermissionMap() entered with args : userRoleMenuItemPermissionMapDTO");
		if(dto == null) {
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_USER_RPM_IS_NULL_CODE, 
					ErrorCodeMessages.ERR_USER_RPM_IS_NULL_MSG);
		}
		log.info("updateUserRoleMenuItemPermissionMap() is under execution...");
		UserRoleMenuItemPermissionMapDTO updatedDTO = userRoleMenuItemPermissionMapService.updateUserRoleMenuItemPermissionMap(dto);
		log.info("updateUserRoleMenuItemPermissionMap() executed successfully.");
		return new ResponseEntity<>(updatedDTO, HttpStatus.PARTIAL_CONTENT);
	}
	
	@PostMapping("/create")
	public ResponseEntity<?> createUserRoleMenuItemPermissionMap(@RequestBody UserRoleMenuItemPermissionMapDTO dto){
		log.info("createUserRoleMenuItemPermissionMap() entered with args : userRoleMenuItemPermissionMapDTO");
		if(dto == null) {
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_USER_RPM_IS_NULL_CODE, 
					ErrorCodeMessages.ERR_USER_RPM_IS_NULL_MSG);
		}
		log.info("createUserRoleMenuItemPermissionMap() is under execution...");
		UserRoleMenuItemPermissionMapDTO updatedDTO = userRoleMenuItemPermissionMapService.createUserRoleMenuItemPermissionMap(dto);
		log.info("createUserRoleMenuItemPermissionMap() executed successfully.");
		return new ResponseEntity<>(updatedDTO, HttpStatus.CREATED);
	}
	
}
