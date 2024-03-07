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

import com.ikn.ums.admin.dto.UserDTO;
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
	public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO user) {
		log.info("createUser() entered with args - user"+user);
		if(user == null || user.equals(null)) {
			log.info("updateUserRole() EntityNotFoundException : user object null");
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_USER_ENTITY_IS_NULL_CODE,
					ErrorCodeMessages.ERR_USER_ENTITY_IS_NULL_MSG);
		}
		try {
			log.info("createUser() is under execution...");
			var savedUser = userService.saveUser(user);
			log.info("createUser() executed successfully.");
			return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
		} catch (EntityNotFoundException businesException) {
			log.error("createUser() exited with exception :Business Exception occured while saving user. "+businesException.getMessage(), businesException);
			throw businesException;
		}
		catch (Exception e) {
			e.printStackTrace();			
			log.error("createUser() exited with exception : Exception occured while saving user. "+e.getMessage(), e);
			throw new ControllerException(ErrorCodeMessages.ERR_USER_CREATE_UNSUCCESS_CODE,
					ErrorCodeMessages.ERR_USER_CREATE_UNSUCCESS_MSG);
		}
	}
	
	@PutMapping("/update")
	public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO user) {
		log.info("AdminController.updateUser() entered with args - user");
		if(user == null || user.equals(null)) {
			log.info("updateUser() EntityNotFoundException : User object is null ");
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_USER_ENTITY_IS_NULL_CODE,
					ErrorCodeMessages.ERR_USER_ENTITY_IS_NULL_MSG);
		}
		try {
			log.info("updateUser() is under execution...");
			var updatedUser = userService.updateUser(user);
			log.info("updateUser() executed successfully.");
			return new ResponseEntity<>(updatedUser, HttpStatus.CREATED);
		} 
		catch (EntityNotFoundException businesException) {
			log.error("updateUser() exited with exception :Business Exception occured while updating user. "+businesException.getMessage(), businesException);
			throw businesException;
		}catch (Exception e) {
			log.error("updateUser() exited with exception : Exception occured while updating user."+ e.getMessage(), e);
			throw new ControllerException(ErrorCodeMessages.ERR_USER_UPDATE_UNSUCCESS_CODE,
					ErrorCodeMessages.ERR_USER_UPDATE_UNSUCCESS_MSG);
		}
	}
	
	@DeleteMapping("/delete/{userId}")
	public ResponseEntity<Boolean> deleteUserByUserId(@PathVariable("userId") String emailId){
		log.info("deleteUserByUserId() entered with args - emailId");
		if(emailId == null || emailId.isEmpty()) {
			log.info("deleteUserByUserId() EmptyInputException : userid/emailid is empty");
			throw new EmptyInputException(ErrorCodeMessages.ERR_USER_EMAIL_ID_NOT_FOUND_CODE,
					ErrorCodeMessages.ERR_USER_EMAIL_ID_NOT_FOUND_MSG);
		}
		try {
			log.info("deleteUserByUserId() is under execution...");
			userService.deleteUserByUserId(emailId);
			log.info("deleteUserByUserId() executed successfully");
			return new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);
		}catch (EmptyInputException businesException) {
			log.error("deleteUserByUserId() exited with exception :Business Exception occured while deleting user. "+businesException.getMessage(), businesException);
			throw businesException;
		}catch (Exception e) {
			log.error("deleteUserByUserId() exited with exception : Exception occured while deleting user."+ e.getMessage(), e);
			throw new ControllerException(ErrorCodeMessages.ERR_USER_DELETE_UNSUCCESS_CODE,
			ErrorCodeMessages.ERR_USER_DELETE_UNSUCCESS_MSG);
		}
	}

	
	@GetMapping("/all")
	public ResponseEntity<List<UserDTO>> getAllUser(){
		
		log.info("getAllUserDetails() is entered");
		try {
			log.info("getAllUserDetails() is under execution");
			var userList = userService.getAllUsers();
			log.info("getAllUserDetails() executed successfully");
			return new ResponseEntity<>(userList,HttpStatus.OK);
		}catch (Exception e) {
			log.error("getAllUserDetails() exited with exception :Exception occured while updating user. "+e.getMessage(), e);
			throw new ControllerException(ErrorCodeMessages.ERR_USER_GET_UNSUCCESS_CODE,
					ErrorCodeMessages.ERR_USER_GET_UNSUCCESS_MSG);
		}
	}
	
	@GetMapping("/getUser/{emailId}")
	public ResponseEntity<UserDTO> getSingleUserByEmailId(@PathVariable("emailId") String emailId){
		log.info("getSingleUserByEmailId() entered with args - emailid "+emailId);
		if(emailId == null || emailId.isEmpty()) {
			log.info("getSingleUserByEmailId() EmptyInputException : userId / emailId is empty or null");
			throw new EmptyInputException(ErrorCodeMessages.ERR_USER_EMAIL_ID_NOT_FOUND_CODE,
					ErrorCodeMessages.ERR_USER_EMAIL_ID_NOT_FOUND_MSG);
		}
		try {
			log.info("getSingleUserByEmailId() is under execution...");
			var userObject = userService.getUserDetailsByUsername(emailId);
			log.info("getSingleUserByEmailId() executed successfully");
			return new ResponseEntity<>(userObject,HttpStatus.OK);
		}catch (EmptyInputException businesException) {
			log.error("getSingleUserByEmailId() exited with exception :Business Exception occured while updating user. "+businesException.getMessage(), businesException);
			throw businesException;
		}catch (Exception e) {
			log.error("getSingleUserByEmailId() exited with exception : Exception occured while fetching user."+ e.getMessage(), e);
			throw new ControllerException(ErrorCodeMessages.ERR_USER_GET_UNSUCCESS_CODE,
					ErrorCodeMessages.ERR_USER_GET_UNSUCCESS_MSG);
		}
	}
	
	@PutMapping("/set-status/{emailId}")
	public ResponseEntity<Boolean> setUserStatus(@PathVariable("emailId") String emailId){
		log.info("setUserStatus() entered with args - emailid "+emailId);
		if(emailId == null || emailId.isEmpty()) {
			log.info("setUserStatus() EmptyInputException : userId / emailId is empty or null");
			throw new EmptyInputException(ErrorCodeMessages.ERR_USER_EMAIL_ID_NOT_FOUND_CODE,
					ErrorCodeMessages.ERR_USER_EMAIL_ID_NOT_FOUND_MSG);
		}
		try {
			log.info("setUserStatus() is under execution...");
			var result = userService.setUserStatustoInactive(emailId);
			log.info("setUserStatus() executed successfully");
			return new ResponseEntity<>(result,HttpStatus.OK);
		}catch (EmptyInputException businesException) {
			log.error("setUserStatus() exited with exception :Business Exception occured while updating user. "+businesException.getMessage(), businesException);
			throw businesException;
		}catch (Exception e) {
			log.error("setUserStatus() exited with exception : Exception occured while updating user."+ e.getMessage(), e);
			throw new ControllerException(ErrorCodeMessages.ERR_USER_UPDATE_UNSUCCESS_CODE,
					ErrorCodeMessages.ERR_USER_UPDATE_UNSUCCESS_CODE);
		}
	}
	
	
}
