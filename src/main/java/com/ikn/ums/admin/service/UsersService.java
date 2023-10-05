package com.ikn.ums.admin.service;
import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.ikn.ums.admin.VO.EmployeeVO;
import com.ikn.ums.admin.VO.UserVO;
import com.ikn.ums.admin.entity.UserDetailsEntity;

public interface UsersService extends UserDetailsService {
	
	UserDetailsEntity getUserDetailsByUsername(String userName);
	Integer generateOtpForUser(String userName,String pageType);
	Integer validateUserOtp(String email, String otp);
	Integer updatePasswordforUser(String email, CharSequence newRawPassword);
	Integer validateEmailAddress(String email);
	Integer updateUserTwoFactorAuthStatus(String email, boolean isOn);
	UserVO getUserProfile(String username);
	UserDetailsEntity updateProfilePicByEmail(String email);
	UserDetailsEntity updateUserProfilePic(UserDetailsEntity userDetails);
	List<String> getActiveUsersEmailIdList(boolean isActive);
	
	//Admin Operations on user
	UserDetailsEntity createUser(UserDetailsEntity user);
}
