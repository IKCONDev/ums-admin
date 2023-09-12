package com.ikn.ums.users.service;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.ikn.ums.users.VO.EmployeeVO;
import com.ikn.ums.users.VO.UserVO;
import com.ikn.ums.users.entity.UserDetailsEntity;

public interface UsersService extends UserDetailsService {
	
	UserDetailsEntity getUserDetailsByUsername(String userName);
	Integer generateOtpForUser(String userName);
	Integer validateUserOtp(String email, String otp);
	Integer updatePasswordforUser(String email, CharSequence newRawPassword);
	Integer validateEmailAddress(String email);
	Integer updateUserTwoFactorAuthStatus(String email, boolean isOn);
	UserVO getUserProfile(String username);
	
}
