package com.ikn.ums.admin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import com.ikn.ums.admin.model.ValidateOtpRequestModel;
import com.ikn.ums.admin.service.UserService;
import com.netflix.servo.util.Strings;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/users")
public class AuthenticationController {

	@Autowired
	private BCryptPasswordEncoder encoder;

	@Autowired
	private UserService userService;

	@GetMapping // later we will change it to post
	public ResponseEntity<String> authenticate() {
		log.info("UserController.authenticate() ENTERED");
		String encodedPWD = encoder.encode("test");
		return new ResponseEntity<String>(encodedPWD, HttpStatus.OK);
	}

	@PostMapping("/generate-otp/{email}/{pageType}")
	public ResponseEntity<Integer> generateAndSendOtpToUser(@PathVariable String email, @PathVariable String pageType) {
		log.info("UserController.generateAndSendOtpToUser() ENTERED : email : " + email + " : and pageType :" + pageType);
		try {
			if (email.isBlank() || email.length() == 0 || Strings.isNullOrEmpty(email) )
				throw new EmptyInputException(ErrorCodeMessages.ERR_USER_EMAIL_ID_NOT_FOUND_CODE,
						ErrorCodeMessages.ERR_USER_EMAIL_ID_NOT_FOUND_MSG);
			log.info("UserController.generateAndSendOtpToUser() is under execution...");
			Integer otp = userService.generateOtpForUser(email, pageType);
			if (otp <= 0) {
				log.info("UserController.generateAndSendOtpToUser() otp is not generated ....");
				throw new EmptyOTPException(ErrorCodeMessages.ERR_USER_OTP_NOT_GENERATED_CODE,
						ErrorCodeMessages.ERR_USER_OTP_NOT_GENERATED_MSG);
			}
			log.info("UserController.generateAndSendOtpToUser() otp :" + otp);
			log.info("UserController.generateAndSendOtpToUser() executed successfully");
			return new ResponseEntity<>(otp, HttpStatus.OK);
		} catch (Exception e) {
			log.error("generateAndSendOtpToUser() : An error occurred: {}." + e.getMessage(), e);
			ControllerException umsCE = new ControllerException(ErrorCodeMessages.ERR_USER_OTP_NOT_GENERATED_CODE, 
					ErrorCodeMessages.ERR_USER_OTP_NOT_GENERATED_MSG);
			throw umsCE;
		}
	}

	@PostMapping("/validate-otp")
	public ResponseEntity<Integer> validateUserOtp(@RequestBody ValidateOtpRequestModel otpRequestModel) {
		log.info("UserController.validateUserOtp() ENTERED with args : otpRequestModel ");
		if (otpRequestModel == null) {
			log.info("UserController.validateUserOtp() ValidateOtpRequestModel Object in NULL");
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_USER_ENTITY_IS_NULL_CODE,
					ErrorCodeMessages.ERR_USER_ENTITY_IS_NULL_MSG);
		}
		try {
			log.info("UserController.validateUserOtp() is under execution...");
			int count = userService.validateUserOtp(otpRequestModel.getEmail(), otpRequestModel.getOtpCode());
			log.info("UserController.validateUserOtp() executed successfully");
			return new ResponseEntity<>(count, HttpStatus.OK);
		} catch (Exception e) {
			log.error("validateUserOtp() : An error/exception occurred: {}." + e.getMessage(), e);
			ControllerException umsCE = new ControllerException(ErrorCodeMessages.ERR_USER_OTP_NOT_GENERATED_CODE, 
					ErrorCodeMessages.ERR_USER_OTP_NOT_GENERATED_MSG);
			throw umsCE;
		}
	}

	@PostMapping("/reset-password")
	public ResponseEntity<Integer> updatePassword(@RequestBody UpdatePasswordRequestModel updatePasswordModel) {
		log.info("UserController.updatePassword() ENTERED with args : updatePasswordModel");
		try {
			log.info("UserController.updatePassword() is under execution...");
			int updateStatus = userService.updatePasswordforUser(updatePasswordModel.getEmail(),
					updatePasswordModel.getConfirmPassword());
			log.info("UserController.updatePassword() executed successfully");
			return new ResponseEntity<>(updateStatus, HttpStatus.OK);
		} catch (Exception e) {
			log.error("updatePassword() : An error/exception occurred: {}." + e.getMessage(), e);
			ControllerException umsCE = new ControllerException(ErrorCodeMessages.ERR_USER_OTP_NOT_GENERATED_CODE, 
					ErrorCodeMessages.ERR_USER_OTP_NOT_GENERATED_MSG);
			throw umsCE;
		}
	}

	@GetMapping("/validate-email/{email}")
	public ResponseEntity<?> verifyEmailAddress_ForOtp(@PathVariable String email) {
		log.info("UserController.verifyEmailAddress_ForOtp() ENTERED with args :" + email);
		try {
			log.info("UserController.verifyEmailAddress_ForOtp() is under execution...");
			Integer value = userService.validateEmailAddress(email);
			log.info("UserController.verifyEmailAddress_ForOtp() executed successfully");
			return new ResponseEntity<Integer>(value, HttpStatus.OK);
		} catch (Exception e) {
			log.error("verifyEmailAddress_ForOtp() : An error/exception occurred: {}." + e.getMessage(), e);
			return new ResponseEntity<>("Error while validating email, please try again",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/user-profile/{username}")
	public ResponseEntity<?> fetchUserProfile(@PathVariable String username) {
		log.info("UserController.fetchUserProfile() ENTERED with args :" + username);
		try {
			log.info("UserController.fetchUserProfile() is under execution...");
			UserVO userprofileDetails = userService.getUserProfile(username);
			System.out.println(userprofileDetails);
			log.info("UserController.fetchUserProfile() executed successfully");
			return new ResponseEntity<>(userprofileDetails, HttpStatus.OK);
		} catch (Exception e) {
			log.error("fetchUserProfile() : An error/exception occurred: {}." + e.getMessage(), e);
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
		log.info("UserController.updateUserTwofactorAuthentication() ENTERED with args :" + username);
		try {
			log.info("UserController.updateUserTwofactorAuthentication() is under execution...");
			Integer value = userService.updateUserTwoFactorAuthStatus(username, isTwoFactorSwitched);
			log.info("UserController.updateUserTwofactorAuthentication() executed successfully");
			return new ResponseEntity<>(value, HttpStatus.OK);
		} catch (Exception e) {
			log.error("updateUserTwofactorAuthentication() : An error/exception occurred: {}." + e.getMessage(), e);
			return new ResponseEntity<>("Error while updating two factor authentication status",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/profile-pic")
	public ResponseEntity<User> updateUserProfilePicture(@RequestParam("email") String userEmailId,
			@RequestParam("profilePic") MultipartFile profilePicImage) throws ImageNotFoundException {
		log.info("UserController.updateUserProfilePicture() ENTERED with args :");
		String contentType = profilePicImage.getContentType();
		if (contentType != null && !contentType.startsWith("image/")) {
			throw new ImageNotFoundException(ErrorCodeMessages.ERR_USER_IMAGE_NOT_VALID_CODE,
					ErrorCodeMessages.ERR_USER_IMAGE_NOT_VALID_MSG);
		}
		try {
			log.info("UserController.updateUserProfilePicture() is under execution...");
			User updatedUser = null;
			if (contentType != null && contentType.startsWith("image/")) {
				updatedUser = userService.updateUserProfilePic(userEmailId, profilePicImage);
			}
			log.info("UserController.updateUserProfilePicture() executed successfully");
			return new ResponseEntity<>(updatedUser, HttpStatus.OK);
		} catch (Exception e) {
			// return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			log.error("updateUserProfilePicture() : An error/exception occurred: {}." + e.getMessage(), e);
			throw new ControllerException(ErrorCodeMessages.ERR_USER_IMAGE_NOT_VALID_CODE,
					ErrorCodeMessages.ERR_USER_IMAGE_NOT_VALID_MSG);
		}
	}

	@GetMapping("/getEmail-list")
	public ResponseEntity<List<String>> getActiveUsersEmailIdList() {
		log.info("UserController.getActiveUsersEmailIdList() ENTERED with args :");
		boolean isActive = true;
		try {
			log.info("UserController.getActiveUsersEmailIdList() is under execution...");
			List<String> activeUserEmailIdList = userService.getActiveUsersEmailIdList(isActive);
			log.info("UserController.getActiveUsersEmailIdList() executed successfully");
			return new ResponseEntity<>(activeUserEmailIdList, HttpStatus.OK);
		} catch (Exception e) {
			log.error("getActiveUsersEmailIdList() : An error/exception occurred: {}." + e.getMessage(), e);
			throw new ControllerException(ErrorCodeMessages.ERR_USER_LIST_IS_EMPTY_CODE,
					ErrorCodeMessages.ERR_USER_LIST_IS_EMPTY_MSG);
		}

	}

	@DeleteMapping("/deleteProfilePic")
	public ResponseEntity<Void> deleteProfilePic(@RequestParam String email) {
		log.info("UserController.deleteProfilePic() is under execution...");
		if (email == null || email.isBlank() || email.isEmpty()) {
			throw new EmptyInputException(ErrorCodeMessages.ERR_USER_EMAIL_ID_NOT_FOUND_CODE,
					ErrorCodeMessages.ERR_USER_EMAIL_ID_NOT_FOUND_MSG);
		}
		try {
			log.info("UserController.deleteProfilePic() is under execution...");
			userService.deleteProfilePicOfUser(email);
			log.info("UserController.deleteProfilePic() executed succesfully");
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			log.error("deleteProfilePic() : An error/exception occurred: {}." + e.getMessage(), e);
			throw new ControllerException(ErrorCodeMessages.ERR_USER_DELETE_PROFILEPIC_UNSUCCESS_CODE,
					ErrorCodeMessages.ERR_USER_DELETE_PROFILEPIC_UNSUCCESS_MSG);
		}

	}
}
