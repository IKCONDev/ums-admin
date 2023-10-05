package com.ikn.ums.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ikn.ums.admin.entity.UserDetailsEntity;
import com.ikn.ums.admin.exception.ControllerException;
import com.ikn.ums.admin.exception.EntityNotFoundException;
import com.ikn.ums.admin.exception.ErrorCodeMessages;
import com.ikn.ums.admin.service.UsersService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/admin")
@Slf4j
public class AdminController {
	
	@Autowired
	private UsersService userService;
	
	@PostMapping("/user/save")
	public ResponseEntity<?> createUser(@RequestBody UserDetailsEntity user) {
		log.info("AdminController.createUser() entered with args - user");
		if(user == null || user.equals(null)) {
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_USER_ENTITY_IS_NULL_CODE,
					ErrorCodeMessages.ERR_USER_ENTITY_IS_NULL_MSG);
		}
		try {
			log.info("AdminController.createUser() is under execution...");
			UserDetailsEntity savedUser = userService.createUser(user);
			log.info("AdminController.createUser() executed successfully.");
			return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
		} catch (Exception e) {
			log.info("AdminController.createUser() exited with exception : Exception occured while saving user.");
			ControllerException umsCE = new ControllerException(ErrorCodeMessages.USER_SAVE_UNSUCCESS_CODE,
					ErrorCodeMessages.USER_SAVE_UNSUCCESS_MSG);
			throw umsCE;
		}
	}
	


}
