package com.ikn.ums.authentication.service;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.ikn.ums.authentication.VO.EmployeeVO;
import com.ikn.ums.authentication.VO.UserVO;
import com.ikn.ums.authentication.entity.UserDetailsEntity;

public interface IUsersService extends UserDetailsService {
	
	UserDetailsEntity getUserDetailsByUsername(String userName);
	Integer generateOtpForUser(String userName);
	Integer validateUserOtp(String email, String otp);
	Integer updatePasswordforUser(String email, CharSequence newRawPassword);
	Integer validateEmailAddress(String email);
	Integer updateUserTwoFactorAuthStatus(String email, boolean isOn);
	UserVO getUserProfile(String username);
	
}
