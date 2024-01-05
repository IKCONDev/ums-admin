package com.ikn.ums.admin.service;
import java.io.IOException;
import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

import com.ikn.ums.admin.VO.UserVO;
import com.ikn.ums.admin.dto.UserDTO;
import com.ikn.ums.admin.entity.User;

public interface UserService extends UserDetailsService {
	
	UserDTO getUserDetailsByUsername(String emailId);
	Integer generateOtpForUser(String userName,String pageType);
	Integer validateUserOtp(String email, String otp);
	Integer updatePasswordforUser(String email, CharSequence newRawPassword);
	Integer validateEmailAddress(String email);
	Integer updateUserTwoFactorAuthStatus(String email, boolean isOn);
	UserVO getUser(String username);
	UserVO getUserProfile(String username);
//	User updateProfilePicByEmail(String email);
	User updateUserProfilePic(String emailId, MultipartFile profilePicImage) throws IOException;
	List<String> getActiveUsersEmailIdList(boolean isActive);
	
	//Admin Operations on user
	User saveUser(User user);
	User updateUser(User user);
	void deleteUserByUserId(String emailId);
	List<User> getAllUsers();
	void deleteProfilePicOfUser(String emailId);
	
	//admin operation on user roles
	User updateUserRoleByUserId(String emailId);
}
