package com.ikn.ums.users.controller;

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

import com.ikn.ums.users.VO.UserVO;
import com.ikn.ums.users.entity.UserDetailsEntity;
import com.ikn.ums.users.exception.ControllerException;
import com.ikn.ums.users.exception.EmptyInputException;
import com.ikn.ums.users.exception.EmptyOTPException;
import com.ikn.ums.users.exception.EntityNotFoundException;
import com.ikn.ums.users.exception.ErrorCodeMessages;
import com.ikn.ums.users.model.UpdatePasswordRequestModel;
import com.ikn.ums.users.model.ValidateOtpRequestModel;
import com.ikn.ums.users.service.UsersService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private BCryptPasswordEncoder encoder;

	@Autowired
	private UsersService userService;

//	@Autowired
//	private UserRepository userRepository;

	@GetMapping // later we will change it to post
	public ResponseEntity<String> authenticate() {
		log.info("UserController.authenticate() ENTERED");
		String encodedPWD = encoder.encode("test");
		return new ResponseEntity<String>(encodedPWD, HttpStatus.OK);
	}

	/*
	 * @PostMapping public ResponseEntity<?> saveUser(@RequestBody UserDetailsEntity
	 * user){ UserDetailsEntity savedUser = userRepository.save(user); return new
	 * ResponseEntity<>(savedUser, HttpStatus.CREATED); }
	 */
	@PostMapping("/generate-otp/{email}")
	public ResponseEntity<?> generateAndSendOtpToUser(@PathVariable String email) {
		log.info("UserController.generateAndSendOtpToUser() ENTERED : email : " + email);
		try {
			if (email.isBlank() || email.length() == 0)
				throw new EmptyInputException(ErrorCodeMessages.ERR_USER_EMAIL_ID_NOT_FOUND_CODE,
						ErrorCodeMessages.ERR_USER_EMAIL_ID_NOT_FOUND_MSG);
			Integer otp = userService.generateOtpForUser(email);
			if (otp <= 0)
				throw new EmptyOTPException(ErrorCodeMessages.ERR_USER_OTP_NOT_GENERATED_CODE,
						ErrorCodeMessages.ERR_USER_OTP_NOT_GENERATED_MSG);
			return new ResponseEntity<>(otp, HttpStatus.OK);
		} catch (Exception e) {
			log.error("UserController.generateAndSendOtpToUser() : Exception Occurred." + e.getMessage());
			ControllerException umsCE = new ControllerException(ErrorCodeMessages.ERR_USER_CONTROLLER_EXCEPTION_CODE,
					ErrorCodeMessages.ERR_USER_CONTROLLER_EXCEPTION_MSG);
			return new ResponseEntity<ControllerException>(umsCE, HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/validate-otp")
	public ResponseEntity<?> validateUserOtp(@RequestBody ValidateOtpRequestModel otpRequestModel) {
		if (otpRequestModel == null) {
			log.info("UserController.validateUserOtp() ValidateOtpRequestModel Object in NULL");
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_USER_ENTITY_IS_NULL_CODE,
					ErrorCodeMessages.ERR_USER_ENTITY_IS_NULL_MSG);
		}
		try {
			int count = userService.validateUserOtp(otpRequestModel.getEmail(), otpRequestModel.getOtpCode());
			return new ResponseEntity<>(count, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getStackTrace(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/reset-password")
	public ResponseEntity<?> updatePassword(@RequestBody UpdatePasswordRequestModel updatePasswordModel) {
		try {
			int updateStatus = userService.updatePasswordforUser(updatePasswordModel.getEmail(),
					updatePasswordModel.getConfirmPassword());
			return new ResponseEntity<>(updateStatus, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/validate-email/{email}")
	public ResponseEntity<?> verifyEmailAddress_ForOtp(@PathVariable String email) {
		try {
			Integer value = userService.validateEmailAddress(email);
			return new ResponseEntity<Integer>(value, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Error while validating email, please try again",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/user-profile/{username}")
	public ResponseEntity<?> fetchUserProfile(@PathVariable String username) {
		try {
			UserVO userprofileDetails = userService.getUserProfile(username);
			System.out.println(userprofileDetails);
			return new ResponseEntity<>(userprofileDetails, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/demo")
	public ResponseEntity<String> demo() {
		return new ResponseEntity<>("OK", HttpStatus.OK);
	}

	@PatchMapping("/update-auth/{username}/{isOn}")
	public ResponseEntity<?> updateUserTwofactorAuthentication(@PathVariable String username,
			@PathVariable boolean isOn) {
		System.out.println("executed");
		try {
			Integer value = userService.updateUserTwoFactorAuthStatus(username, isOn);
			return new ResponseEntity<>(value, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Error while updating two factor authentication status",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/save")
	public ResponseEntity<?> createUser(@RequestBody UserDetailsEntity user){
		try {
			UserDetailsEntity savedUser =  userService.createUser(user);
			return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
		}catch (Exception e) {
			ControllerException umsCE = new ControllerException(ErrorCodeMessages.ERR_USER_CONTROLLER_EXCEPTION_CODE,
					ErrorCodeMessages.ERR_USER_CONTROLLER_EXCEPTION_MSG);
			return new ResponseEntity<>(umsCE, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}

}
