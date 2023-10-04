package com.ikn.ums.users.service.impl;

import java.util.ArrayList;
import java.util.Random;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ikn.ums.users.VO.EmployeeVO;
import com.ikn.ums.users.VO.UserVO;
import com.ikn.ums.users.entity.UserDetailsEntity;
import com.ikn.ums.users.exception.EmptyInputException;
import com.ikn.ums.users.exception.ErrorCodeMessages;
import com.ikn.ums.users.repository.UserRepository;
import com.ikn.ums.users.service.UsersService;
import com.ikn.ums.users.utils.EmailService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UsersServiceImpl implements UsersService {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private EmailService emailService;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private RestTemplate restTemplate;

	@Override
	public UserDetailsEntity getUserDetailsByUsername(String email) {
		// old implementation UserDetailsEntity loadedUser =
		UserDetailsEntity loadedUser = userRepo.findByEmail(email);
		if (loadedUser == null)
			throw new UsernameNotFoundException("User with " + email + " does not exist");
		
		System.out.println("UsersServiceImpl.getUserDetailsByUsername() "+email+" "+loadedUser);
		return loadedUser;
		// ModelMapper mapper = new ModelMapper();
		// mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		// return mapper.map(loadedUser, UserDetailsDto.class);

		// communicate with Employee microservice and get the employee object

		/*
		 * ResponseEntity<EmployeeVO> response = restTemplate
		 * .getForEntity("http://UMS-EMPLOYEE-SERVICE/employees/" + email,
		 * EmployeeVO.class); EmployeeVO employeeDetails = response.getBody(); if
		 * (employeeDetails == null) throw new UsernameNotFoundException("User with " +
		 * email + " does not exist"); return employeeDetails;
		 */
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		/*
		 * ResponseEntity<EmployeeVO> response = restTemplate
		 * .getForEntity("http://UMS-EMPLOYEE-SERVICE/employees/" + username,
		 * EmployeeVO.class); EmployeeVO employeeDetails = response.getBody();
		 */
		UserDetailsEntity userDetails = userRepo.findByEmail(username);
		if (userDetails == null)
			throw new UsernameNotFoundException("User does not exists");
		System.out.println("UsersServiceImpl.loadUserByUsername() "+userDetails);
		return new User(userDetails.getEmail(), userDetails.getEncryptedPassword(), true, true, true, true,
				new ArrayList<>());
	}

	@Transactional
	@Override
	public Integer generateOtpForUser(String userName,String pageType) {
		log.info("UsersServiceImpl.generateOtpForUser() : userName :" + userName );
		Random r = new Random();
		Integer otp = 0;
		String text = null;
		String mailHeading=null;
		if(pageType.equals("TwoFactorAuth")){
			text ="your two factor otp is ";
			mailHeading="Two Factor Otp ";
		}
		else if(pageType.equals("ForgotPassword")){
			text="your secret password reset code ";
			mailHeading="Reset Password Otp";
		}
		try {
			for (int i = 0; i < r.nextInt(999999); i++) {
				System.out.println("executed " + i);
				otp = r.nextInt(999999);
				if (otp > 000000 && otp < 999999) {
					userRepo.saveOtp(userName, otp);
					String textBody = "Hi, "+ text + otp + "\r\n" + "\r\n" + ""
							+ "Please dont share it with anyone" + "\r\n" + "\r\n" + "\r\n" + "\r\n" + ""
							+ "Please dont reply to this message as this is an automated email generated by system";
					emailService.sendMail(userName, mailHeading , textBody);
					break;
				}
			}
			return otp;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public Integer validateUserOtp(String email, String otp) {
		int otpCode = Integer.parseInt(otp);
		int count = userRepo.validateUserOtp(email, otpCode);
		return count;
	}

	@Override
	@Transactional
	public Integer updatePasswordforUser(String email, CharSequence newRawPassword) {
		String encodedPassword = passwordEncoder.encode(newRawPassword);
		int updateStatus = userRepo.updatePassword(email, encodedPassword);
		return updateStatus;
	}

	@Override
	public Integer validateEmailAddress(String email) {
		int value = userRepo.validateEmail(email);
		return value;
	}

	@Transactional
	@Override
	public Integer updateUserTwoFactorAuthStatus(String email, boolean isOn) {
		return userRepo.updateTwofactorAuthenticationStatus(email, isOn);
	}

	@Override
	public UserVO getUserProfile(String username) {
		//get user details
		UserDetailsEntity dbLoggedInUser = getUserDetailsByUsername(username);
		// communicate with Employee microservice and get the employee object
        System.out.println("UsersServiceImpl.getUserProfile() "+username);
		ResponseEntity<EmployeeVO> response = restTemplate
				.getForEntity("http://UMS-EMPLOYEE-SERVICE/employees/" + username, EmployeeVO.class);
		EmployeeVO employeeDetails = response.getBody();
		if (employeeDetails == null)
			throw new UsernameNotFoundException("User with " + username + " does not exist");
		UserVO user = new UserVO();
		user.setEmail(dbLoggedInUser.getEmail());
		user.setUserRoles(dbLoggedInUser.getUserRoles());
		user.setEncryptedPassword(dbLoggedInUser.getEncryptedPassword());
		user.setTwoFactorAuthentication(dbLoggedInUser.isTwoFactorAuthentication());
		user.setUserRoles(dbLoggedInUser.getUserRoles());
		user.setEmployee(employeeDetails);
		user.setProfilePic(dbLoggedInUser.getProfilePic());
		return user;
	}

	@Override
	public UserDetailsEntity createUser(UserDetailsEntity user) {
		if(user == null) {
			throw new EmptyInputException(ErrorCodeMessages.ERR_USER_ENTITY_IS_NULL_CODE, 
					ErrorCodeMessages.ERR_USER_ENTITY_IS_NULL_MSG);
		}
		UserDetailsEntity savedUser =  userRepo.save(user);
		return savedUser;
	}

	@Override
	@Transactional
	public UserDetailsEntity updateProfilePicByEmail(String email) {
		if(email!=null) {
			UserDetailsEntity updateUser =userRepo.save(getUserDetailsByUsername(email));
			return updateUser;
		}
		return null;
	}

	@Override
	public UserDetailsEntity updateUserProfilePic(UserDetailsEntity userDetails) {
		return userRepo.save(userDetails);
	}

}
