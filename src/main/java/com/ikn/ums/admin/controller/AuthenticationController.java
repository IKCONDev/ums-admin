package com.ikn.ums.admin.controller;

import java.io.IOException;
import java.util.List;

import org.apache.http.protocol.HTTP;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ikn.ums.admin.VO.UserVO;
import com.ikn.ums.admin.entity.User;
import com.ikn.ums.admin.exception.ControllerException;
import com.ikn.ums.admin.exception.EmptyInputException;
import com.ikn.ums.admin.exception.EmptyOTPException;
import com.ikn.ums.admin.exception.EntityNotFoundException;
import com.ikn.ums.admin.exception.ErrorCodeMessages;
import com.ikn.ums.admin.exception.ImageNotFoundException;
import com.ikn.ums.admin.model.UpdatePasswordRequestModel;
import com.ikn.ums.admin.model.UserProfilePicDetailsRequestModel;
import com.ikn.ums.admin.model.ValidateOtpRequestModel;
import com.ikn.ums.admin.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/users")
public class AuthenticationController {

	@Autowired
	private BCryptPasswordEncoder encoder;

	@Autowired
	private UserService userService;

//	@Autowired
//	private UserRepository userRepository;

	@GetMapping // later we will change it to post
	public ResponseEntity<String> authenticate() {
		log.info("UserController.authenticate() ENTERED");
		String encodedPWD = encoder.encode("test");
		return new ResponseEntity<String>(encodedPWD, HttpStatus.OK);
	}

	@PostMapping("/generate-otp/{email}/{pageType}")
	public ResponseEntity<?> generateAndSendOtpToUser(@PathVariable String email,@PathVariable String pageType) {
		log.info("UserController.generateAndSendOtpToUser() ENTERED : email : " + email);
		try {
			if (email.isBlank() || email.length() == 0)
				throw new EmptyInputException(ErrorCodeMessages.ERR_USER_EMAIL_ID_NOT_FOUND_CODE,
						ErrorCodeMessages.ERR_USER_EMAIL_ID_NOT_FOUND_MSG);
			Integer otp = userService.generateOtpForUser(email,pageType);
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
			@PathVariable("isOn") boolean isTwoFactorSwitched) {
		try {
			Integer value = userService.updateUserTwoFactorAuthStatus(username, isTwoFactorSwitched);
			return new ResponseEntity<>(value, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Error while updating two factor authentication status",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/profile-pic")
		public ResponseEntity<?> updateUserProfilePicture(@RequestParam("email") String userEmailId,
				@RequestParam("profilePic") MultipartFile profilePicImage) throws ImageNotFoundException {
				String contentType=profilePicImage.getContentType();
				if(!contentType.startsWith("image/")) {
					throw new ImageNotFoundException(ErrorCodeMessages.ERR_USER_IMAGE_NOT_VALID_CODE,
							ErrorCodeMessages.ERR_USER_IMAGE_NOT_VALID_MSG);
				 }
			try {	
				User updatedUser = null;
				if(contentType.startsWith("image/")) {
				User dbUser = userService.getUserDetailsByUsername(userEmailId);
				dbUser.setProfilePic(profilePicImage.getBytes());
				updatedUser = userService.updateUserProfilePic(dbUser);	
				}
				return new ResponseEntity<>(updatedUser, HttpStatus.OK);
			}catch (Exception e) {
					//return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
				throw new ControllerException(ErrorCodeMessages.ERR_USER_IMAGE_NOT_VALID_CODE,
						ErrorCodeMessages.ERR_USER_IMAGE_NOT_VALID_MSG);
			}	
		}
	
	@GetMapping("/getEmail-list")
	public ResponseEntity<?> getActiveUsersEmailIdList(){
		boolean isActive = true;
		List<String> activeUserEmailIdList = userService.getActiveUsersEmailIdList(isActive);
		return new ResponseEntity<>(activeUserEmailIdList, HttpStatus.OK);
	}

}
