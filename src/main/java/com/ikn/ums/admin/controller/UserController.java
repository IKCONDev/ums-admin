package com.ikn.ums.admin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ikn.ums.admin.entity.User;
import com.ikn.ums.admin.exception.ControllerException;
import com.ikn.ums.admin.exception.EmptyInputException;
import com.ikn.ums.admin.exception.EntityNotFoundException;
import com.ikn.ums.admin.exception.ErrorCodeMessages;
import com.ikn.ums.admin.service.UserService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/save")
	public ResponseEntity<?> createUser(@RequestBody User user) {
		log.info("UserController.createUser() entered with args - user");
		if(user == null || user.equals(null)) {
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_USER_ENTITY_IS_NULL_CODE,
					ErrorCodeMessages.ERR_USER_ENTITY_IS_NULL_MSG);
		}
		try {
			log.info("UserController.createUser() is under execution...");
			User savedUser = userService.saveUser(user);
			log.info("UserController.createUser() executed successfully.");
			return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
		} catch (Exception e) {
			log.info("UserController.createUser() exited with exception : Exception occured while saving user.");
			ControllerException umsCE = new ControllerException(ErrorCodeMessages.ERR_USER_CREATE_UNSUCCESS_CODE,
					ErrorCodeMessages.ERR_USER_CREATE_UNSUCCESS_MSG);
			throw umsCE;
		}
	}
	
	

	@PutMapping("/update/{userId}")
	public ResponseEntity<?> updateUser(@PathVariable("userId") String emailId, @RequestBody User user) {
		log.info("AdminController.updateUser() entered with args - user");
		if(user == null || user.equals(null)) {
			log.info("UserController.updateUser() EntityNotFoundException : User object is null ");
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_USER_ENTITY_IS_NULL_CODE,
					ErrorCodeMessages.ERR_USER_ENTITY_IS_NULL_MSG);
		}
		try {
			log.info("UserController.updateUser() is under execution...");
			User savedUser = userService.updateUser(emailId,user);
			log.info("UserController.updateUser() executed successfully.");

			return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
		} catch (Exception e) {
			log.info("UserController.updateUser() exited with exception : Exception occured while updating user.");
			ControllerException umsCE = new ControllerException(ErrorCodeMessages.ERR_USER_UPDATE_UNSUCCESS_CODE,
					ErrorCodeMessages.ERR_USER_UPDATE_UNSUCCESS_MSG);
			throw umsCE;
		}
	}
	
	@DeleteMapping("/delete/{userId}")
	public ResponseEntity<?> deleteUserByUserId(@PathVariable("userId") String emailId){
		boolean isDeleted = false;
		log.info("UserController.deleteUserByUserId() entered with args - emailId");
		if(emailId.equals("") || emailId == null) {
			log.info("UserController.deleteUserByUserId() EmptyInputException : userid/emailid is empty");
			throw new EmptyInputException(ErrorCodeMessages.ERR_USER_EMAIL_ID_NOT_FOUND_CODE,
					ErrorCodeMessages.ERR_USER_EMAIL_ID_NOT_FOUND_MSG);
		}
		try {
			log.info("UserController.deleteUserByUserId() is under execution...");
			userService.deleteUserByUserId(emailId);
			isDeleted = true;
			log.info("UserController.deleteUserByUserId() executed successfully");
			return new ResponseEntity<>(isDeleted, HttpStatus.OK);
		}catch (Exception e) {
			log.info("UserController.deleteUserByUserId() exited with exception : Exception occured while deleting user.");
			ControllerException umsCE = new ControllerException(ErrorCodeMessages.ERR_ROLE_DELETE_UNSUCCESS_CODE,
			ErrorCodeMessages.ERR_ROLE_DELETE_UNSUCCESS_MSG);
			throw umsCE;
		}
	}

	
	@PatchMapping("/updateRole/{userId}")
	public ResponseEntity<?> updateUserRole(@PathVariable("userId") String emailId){
		log.info("AdminController.updateUserRole() entered with args - emailid/userid : "+emailId);
		if(emailId.equals("") || emailId == null) {
			log.info("AdminController.updateUserRole()");
			throw new EmptyInputException(ErrorCodeMessages.ERR_USER_EMAIL_ID_NOT_FOUND_CODE,
					ErrorCodeMessages.ERR_USER_EMAIL_ID_NOT_FOUND_MSG);
		}
		try {
			User updatedUserWithNewRole = userService.updateUserRoleByUserId(emailId);
			return new ResponseEntity<>(updatedUserWithNewRole, HttpStatus.PARTIAL_CONTENT);
		}catch (Exception e) {
			throw new ControllerException(ErrorCodeMessages.ERR_ROLE_UPDATE_UNSUCCESS_CODE,
					ErrorCodeMessages.ERR_ROLE_UPDATE_UNSUCCESS_MSG);
		}
		
	}
	
	@GetMapping("/all")
	public ResponseEntity<?> getAllUser(){
		
		log.info("UserController.getAllUserDetails() is entered");
		try {
			log.info("UserController.getAllUserDetails() is under execution");
			List<User> userList = userService.getAllUsers();
			log.info("UserController.getAllUserDetails() executed successfully");
			return new ResponseEntity<>(userList,HttpStatus.OK);
		}catch (Exception e) {
			// TODO: handle exception
			throw new ControllerException(ErrorCodeMessages.ERR_USER_ENTITY_IS_NULL_CODE,
					ErrorCodeMessages.ERR_USER_ENTITY_IS_NULL_MSG);
		}
		
		
		
	}
	
	@GetMapping("/getUser/{emailId}")
	public ResponseEntity<?> getSingleUserByEmailId(@PathVariable("emailId") String emailId){
		log.info("UserController.getSingleUserByEmailId() entered with args - emailid "+emailId);
		try {
			log.info("UserController.getSingleUserByEmailId() is under execution...");
			User userObject = userService.getUserDetailsByUsername(emailId);
			log.info("UserController.getSingleUserByEmailId() executed successfully");
			return new ResponseEntity<>(userObject,HttpStatus.OK);
			
		}catch (Exception e) {
			// TODO: handle exception
			log.info("UserController.getSingleUserByEmailId() exited with exception : Exception occured while fetching user.");
			
			throw new ControllerException(ErrorCodeMessages.ERR_USER_EMAIL_ID_NOT_FOUND_CODE,
					ErrorCodeMessages.ERR_USER_EMAIL_ID_NOT_FOUND_MSG);
		}
		
	}
	

//	@PatchMapping("/updateRole/{userId}")
//	public ResponseEntity<User> updateUserRole(@PathVariable("userId") String emailId){
//		log.info("UserController.updateUserRole() entered with args - emailid/userid : "+emailId);
//		if(emailId.equals("") || emailId == null) {
//			log.info("UserController.updateUserRole()");
//			throw new EmptyInputException(ErrorCodeMessages.ERR_USER_EMAIL_ID_NOT_FOUND_CODE,
//					ErrorCodeMessages.ERR_USER_EMAIL_ID_NOT_FOUND_MSG);
//		}
//		try {
//			User updatedUserWithNewRole = userService.updateUserRoleByUserId(emailId);
//			return new ResponseEntity<>(updatedUserWithNewRole, HttpStatus.PARTIAL_CONTENT);
//		}catch (Exception e) {
//			throw new ControllerException(ErrorCodeMessages.USER_ROLE_UPDATE_UNSUCCESS_CODE,
//					ErrorCodeMessages.USER_ROLE_UPDATE_UNSUCCESS_MSG);
//		}
//		
//	}

}
