package com.ikn.ums.admin.service;
import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.ikn.ums.admin.VO.EmployeeVO;
import com.ikn.ums.admin.VO.UserVO;
import com.ikn.ums.admin.entity.User;

public interface UserService extends UserDetailsService {
	
	User getUserDetailsByUsername(String emailId);
	Integer generateOtpForUser(String userName,String pageType);
	Integer validateUserOtp(String email, String otp);
	Integer updatePasswordforUser(String email, CharSequence newRawPassword);
	Integer validateEmailAddress(String email);
	Integer updateUserTwoFactorAuthStatus(String email, boolean isOn);
	UserVO getUserProfile(String username);
	User updateProfilePicByEmail(String email);
	User updateUserProfilePic(User userDetails);
	List<String> getActiveUsersEmailIdList(boolean isActive);
	
	//Admin Operations on user
	User saveUser(User user);
	User updateUser(String emailId,User user);
	void deleteUserByUserId(String emailId);
	List<User> getAllUsers();
	
	//admin operation on user roles
	User updateUserRoleByUserId(String emailId);
}
