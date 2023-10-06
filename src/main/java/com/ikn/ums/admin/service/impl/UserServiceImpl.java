package com.ikn.ums.admin.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ikn.ums.admin.VO.EmployeeVO;
import com.ikn.ums.admin.VO.UserVO;
import com.ikn.ums.admin.entity.Role;
import com.ikn.ums.admin.entity.User;
import com.ikn.ums.admin.exception.EmptyInputException;
import com.ikn.ums.admin.exception.EntityNotFoundException;
import com.ikn.ums.admin.exception.ErrorCodeMessages;
import com.ikn.ums.admin.repository.UserRepository;
import com.ikn.ums.admin.service.RoleService;
import com.ikn.ums.admin.service.UserService;
import com.ikn.ums.admin.utils.EmailService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private EmailService emailService;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private RoleService roleService;

	@Override
	public User getUserDetailsByUsername(String email) {
		// old implementation UserDetailsEntity loadedUser =
		User loadedUser = userRepository.findByEmail(email);
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
		User userDetails = userRepository.findByEmail(username);
		if (userDetails == null)
			throw new UsernameNotFoundException("User does not exists");
		System.out.println("UsersServiceImpl.loadUserByUsername() "+userDetails);
		return new org.springframework.security.core.userdetails.User(userDetails.getEmail(), userDetails.getEncryptedPassword(), true, true, true, true,
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
					userRepository.saveOtp(userName, otp);
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
		int count = userRepository.validateUserOtp(email, otpCode);
		return count;
	}

	@Override
	@Transactional
	public Integer updatePasswordforUser(String email, CharSequence newRawPassword) {
		String encodedPassword = passwordEncoder.encode(newRawPassword);
		int updateStatus = userRepository.updatePassword(email, encodedPassword);
		return updateStatus;
	}

	@Override
	public Integer validateEmailAddress(String email) {
		int value = userRepository.validateEmail(email);
		return value;
	}

	@Transactional
	@Override
	public Integer updateUserTwoFactorAuthStatus(String email, boolean isOn) {
		return userRepository.updateTwofactorAuthenticationStatus(email, isOn);
	}

	@Override
	public UserVO getUserProfile(String emailId) {
		 log.info("UsersServiceImpl.getUserProfile() entered with args - emailId : "+emailId);
		if(emailId.equals("") || emailId == null || emailId.equals(null)) {
			log.info("UsersServiceImpl.getUserProfile() EmptyInputException : empty/null userid/emailid");
			throw new EmptyInputException(ErrorCodeMessages.ERR_USER_EMAIL_ID_NOT_FOUND_CODE,
					ErrorCodeMessages.ERR_USER_EMAIL_ID_NOT_FOUND_MSG);
		}
		log.info("UsersServiceImpl.getUserProfile() is under execution...");
		//get user details
		User dbLoggedInUser = getUserDetailsByUsername(emailId);
		// communicate with Employee microservice and get the employee object
		ResponseEntity<EmployeeVO> response = restTemplate
				.getForEntity("http://UMS-EMPLOYEE-SERVICE/employees/" + emailId, EmployeeVO.class);
		log.info("UsersServiceImpl.getUserProfile() : call to employee microservice successful");
		EmployeeVO employeeDetails = response.getBody();
		if (employeeDetails == null)
			throw new UsernameNotFoundException("User with " + emailId + " does not exist");
		UserVO user = new UserVO();
		user.setEmail(dbLoggedInUser.getEmail());
		user.setUserRoles(dbLoggedInUser.getUserRoles());
		user.setEncryptedPassword(dbLoggedInUser.getEncryptedPassword());
		user.setTwoFactorAuthentication(dbLoggedInUser.isTwoFactorAuthentication());
		user.setUserRoles(dbLoggedInUser.getUserRoles());
		user.setEmployee(employeeDetails);
		user.setProfilePic(dbLoggedInUser.getProfilePic());
		user.setActive(dbLoggedInUser.isActive());
		log.info("UsersServiceImpl.getUserProfile() executed successfully");
		return user;
	}

	@Override
	@Transactional
	public User updateProfilePicByEmail(String email) {
		if(email!=null) {
			User updateUser =userRepository.save(getUserDetailsByUsername(email));
			return updateUser;
		}
		return null;
	}

	@Override
	public User updateUserProfilePic(User userDetails) {
		return userRepository.save(userDetails);
	}

	@Override
	public List<String> getActiveUsersEmailIdList(boolean isActive) {
		List<String> userEmailIdList =  userRepository.findAllActiveUsersEmailIdList(isActive);
		return userEmailIdList;
	}
	
	@Transactional
	@Override
	public User saveUser(User user) {
		log.info("UsersServiceImpl.createUser() entered with args - user");
		if(user == null || user.equals(null)) {
			log.info("UsersServiceImpl.createUser() EntityNotFoundException : user object is null");
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_USER_ENTITY_IS_NULL_CODE, 
					ErrorCodeMessages.ERR_USER_ENTITY_IS_NULL_MSG);
		}
		log.info("UsersServiceImpl.createUser() is under execution...");
		User savedUser =  userRepository.save(user);
		log.info("UsersServiceImpl.createUser() executed successfully.");
		return savedUser;
	}

	@Transactional
	@Override
	public User updateUser(User user) {
		log.info("UsersServiceImpl.updateUser() entered with args - user");
		if(user == null || user.equals(null)) {
			log.info("UsersServiceImpl.updateUser() EntityNotFoundException : user object is null");
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_USER_ENTITY_IS_NULL_CODE, 
					ErrorCodeMessages.ERR_USER_ENTITY_IS_NULL_MSG);
		}
		log.info("UsersServiceImpl.updateUser() is under execution...");
		User updatedUser =  userRepository.save(user);
		log.info("UsersServiceImpl.updateUser() executed successfully.");
		return updatedUser;
	}

	@Transactional
	@Override
	public void deleteUserByUserId(String emailId) {
		log.info("UsersServiceImpl.deleteUser() entered with args - id");
		if(emailId.equals(null) || emailId == null || emailId.equals("")) {
			log.info("UsersServiceImpl.deleteUser() EmptyInputException : emailid/userid is null");
			throw new EmptyInputException(ErrorCodeMessages.ERR_USER_EMAIL_ID_NOT_FOUND_CODE, 
					ErrorCodeMessages.ERR_USER_EMAIL_ID_NOT_FOUND_MSG);
		}
		log.info("UsersServiceImpl.deleteUser() is under execution...");
		userRepository.deleteUserByUserId(emailId);
		log.info("UsersServiceImpl.deleteUser() executed successfully");
	}

	@Override
	public User updateUserRoleByUserId(String emailId) {
		Set<Role> userRoleList = null;
		if(emailId.equals(null) || emailId == null) {
			throw new EmptyInputException(ErrorCodeMessages.ERR_USER_EMAIL_ID_NOT_FOUND_CODE, 
					ErrorCodeMessages.ERR_USER_EMAIL_ID_NOT_FOUND_MSG);
		}
		User user = userRepository.findByEmail(emailId);
		//update user with new role, for now its harcoded, later will come from UI, according to admin selection
		Optional<Role> optRole = roleService.getRoleById(2L);
		Role role = null;
		//if roles are present create a HashSet and push the roles into set
		if(optRole.isPresent()) {
		   role = optRole.get();
		   userRoleList = new HashSet<>(); 
		   userRoleList.add(role);
		}
		//set the new role to user
		user.setUserRoles(userRoleList);
		return user;
	}

}
