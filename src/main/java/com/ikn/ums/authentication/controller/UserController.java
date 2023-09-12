package com.ikn.ums.authentication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ikn.ums.authentication.VO.EmployeeVO;
import com.ikn.ums.authentication.VO.UserVO;
import com.ikn.ums.authentication.entity.UserDetailsEntity;
import com.ikn.ums.authentication.model.UpdatePasswordRequestModel;
import com.ikn.ums.authentication.model.ValidateOtpRequestModel;
import com.ikn.ums.authentication.service.IUsersService;

@RestController
@RequestMapping("/users")
public class UserController {
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@Autowired
	private IUsersService userService;
	
//	@Autowired
//	private UserRepository userRepository;
	
	@GetMapping // later we will change it to post
	public ResponseEntity<String> authenticate(){
		String encodedPWD = encoder.encode("test");
		return new ResponseEntity<String>(encodedPWD,HttpStatus.OK);
	}
	
	/*
	@PostMapping
	public ResponseEntity<?> saveUser(@RequestBody UserDetailsEntity user){
		UserDetailsEntity savedUser = userRepository.save(user);	
		return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
	}
	*/
	
	@PostMapping("/generate-otp/{email}")
	public ResponseEntity<?> generateAndSendOtpToUser(@PathVariable String email){
		try {
			Integer otp = userService.generateOtpForUser(email);	
			return new ResponseEntity<>(otp, HttpStatus.OK);
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/validate-otp")
	public ResponseEntity<?> validateUserOtp(@RequestBody ValidateOtpRequestModel otpRequestModel){
		
		try {
			int count = userService.validateUserOtp(otpRequestModel.getEmail(), otpRequestModel.getOtpCode());
			return new ResponseEntity<>(count, HttpStatus.OK);
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getStackTrace(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/reset-password")
	public ResponseEntity<?> updatePassword(@RequestBody UpdatePasswordRequestModel updatePasswordModel){
		try {
			int updateStatus = userService.updatePasswordforUser(updatePasswordModel.getEmail(), 
					                                             updatePasswordModel.getConfirmPassword());
			return new ResponseEntity<>(updateStatus, HttpStatus.OK);
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/validate-email/{email}")
	public ResponseEntity<?> verifyEmailAddress_ForOtp(@PathVariable String email){
		try {
			Integer value = userService.validateEmailAddress(email);
			return new ResponseEntity<Integer>(value,HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<>("Error while validating email, please try again", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	@GetMapping("/user-profile/{username}")
	public ResponseEntity<?> fetchUserProfile(@PathVariable String username){
		try {
			UserVO userprofileDetails = userService.getUserProfile(username);
			System.out.println(userprofileDetails);
			return new ResponseEntity<>(userprofileDetails, HttpStatus.OK);
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/demo")
	public ResponseEntity<String> demo(){
		return new ResponseEntity<>("OK", HttpStatus.OK);
	}
	
	@PatchMapping("/update-auth/{username}/{isOn}")
	public ResponseEntity<?> updateUserTwofactorAuthentication(@PathVariable String username,@PathVariable boolean isOn){
		System.out.println("executed");
		try {
			Integer value = userService.updateUserTwoFactorAuthStatus(username, isOn);
			return new ResponseEntity<>(value, HttpStatus.OK);
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Error while updating two factor authentication status", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
