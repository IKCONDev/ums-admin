package com.ikn.ums.admin.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ikn.ums.admin.VO.EmployeeVO;
import com.ikn.ums.admin.VO.UserVO;
import com.ikn.ums.admin.dto.MenuItemDTO;
import com.ikn.ums.admin.dto.PermissionDTO;
import com.ikn.ums.admin.dto.RoleDTO;
import com.ikn.ums.admin.dto.UserDTO;
import com.ikn.ums.admin.dto.UserRoleMenuItemPermissionMapDTO;
import com.ikn.ums.admin.entity.Role;
import com.ikn.ums.admin.entity.User;
import com.ikn.ums.admin.entity.UserRoleMenuItemPermissionMap;
import com.ikn.ums.admin.exception.EmptyInputException;
import com.ikn.ums.admin.exception.EntityNotFoundException;
import com.ikn.ums.admin.exception.ErrorCodeMessages;
import com.ikn.ums.admin.exception.ImageNotFoundException;
import com.ikn.ums.admin.repository.UserRepository;
import com.ikn.ums.admin.service.RoleService;
import com.ikn.ums.admin.service.UserRoleMenuItemPermissionMapService;
import com.ikn.ums.admin.service.UserService;
import com.ikn.ums.admin.utils.AdminConstants;
import com.ikn.ums.admin.utils.EmailService;
import com.netflix.servo.util.Strings;

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
	
	@Autowired
	private UserRoleMenuItemPermissionMapService userRoleMenuItemPermissionMapService;

	int otp = 0;
	
	int otpExecutionCount = 0;
	
	TimerTask timerTask;
	
	@Autowired
	private ModelMapper mapper;
	
	@Override
	public UserDTO getUserDetailsByUsername(String email) {
		// old implementation UserDetailsEntity loadedUser =
		log.info("UsersServiceImpl.getUserDetailsByUsername() entered");
		log.info("UsersServiceImpl.getUserDetailsByUsername() is under execution");
		email=email.toLowerCase();
		User loadedUser = userRepository.findByEmail(email);
		if (loadedUser == null)
			throw new UsernameNotFoundException("User with " + email + " does not exist");

		System.out.println("UsersServiceImpl.getUserDetailsByUsername() " + email + " " + loadedUser);
		log.info("UsersServiceImpl.getUserDetailsByUsername() executed successfully");
		UserDTO userDTO = new UserDTO();
		mapper.map(loadedUser, userDTO);
		List<UserRoleMenuItemPermissionMapDTO> userRPMDTO = userRoleMenuItemPermissionMapService.getUserRoleMenuItemPermissionMapsByUserId(email);
		userDTO.setUserRoleMenuItemPermissionMap(userRPMDTO);
		return userDTO;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		/*
		 * ResponseEntity<EmployeeVO> response = restTemplate
		 * .getForEntity("http://UMS-EMPLOYEE-SERVICE/employees/" + username,
		 * EmployeeVO.class); EmployeeVO employeeDetails = response.getBody();
		 */
		log.info("UsersServiceImpl.loadUserByUsername() entered");
		log.info("UsersServiceImpl.loadUserByUsername() is under execution");
		username=username.toLowerCase();
		User userDetails = userRepository.findByEmail(username);
		if (userDetails == null)
			throw new UsernameNotFoundException("User does not exists");
		//userDetails.isActive();
		log.info("UsersServiceImpl.loadUserByUsername() executed successfully");
		return new org.springframework.security.core.userdetails.User(userDetails.getEmail(),
				userDetails.getEncryptedPassword(), true, true, true, true, new ArrayList<>());
	}

	@Transactional
	@Override
	public Integer generateOtpForUser(String userName, String pageType) {
		if(userName == null || userName == "") {
			log.info("UsersServiceImpl.generateOtpForUser() EmptyInputException : userId / emailId is empaty.");
			throw new EmptyInputException(ErrorCodeMessages.ERR_USER_EMAIL_ID_IS_EMPTY_MSG, 
					ErrorCodeMessages.ERR_USER_EMAIL_ID_IS_EMPTY_MSG);
		}
		log.info("UsersServiceImpl.generateOtpForUser() is entered with args:");
		otpExecutionCount = 1;
		log.info("UsersServiceImpl.generateOtpForUser() : userName :" + userName);
		Random r = new Random();
		//Integer otp = 0;
		String text = null;
		String mailHeading = null;
		if (pageType.equals("TwoFactorAuth")) {
			text = "One Time Password for Secure Login";
			mailHeading = "One Time Password for Secure Login ";
		} else if (pageType.equals("ForgotPassword")) {
			text = "your secret password reset code ";
			mailHeading = "One Time Password for Secure Login";
		}
		try {
			log.info("UsersServiceImpl.generateOtpForUser() is under execution");
			for (int i = 0; i < r.nextInt(999999); i++) { //TODO Check This
				System.out.println("executed " + i);
				otp = r.nextInt(999999);
				if (otp > 100000 && otp < 999999) {
					userRepository.saveOtp(userName, otp);
					String textBody = "Dear User,"+"\r\n"+"\r\n"+"Please use the one-time password below for secure login in your company account IKCON DIGITAL IT SERVICES PRIVATE LIMITED with email ID "+userName+"." +"\r\n"+"\r\n" + otp +"." +"\r\n" + "\r\n" + "" + "In case you have any queries / clarifications, please call us at our Customer Service number "+"\r\n"+"\r\n"+"9999999999."+"\r\n"+"\r\n"+"Sincerely,"+"\r\n"+"UMS Support Team."
							+ "\r\n" + "\r\n" + "\r\n" + "\r\n" + ""
							+ "Please dont reply to this message as this is an automated email generated by system.";
					emailService.sendMail(userName, mailHeading, textBody);
					break;
				}
			}//for
			
			 if( timerTask !=null ) {
				 timerTask.cancel();
			 }
			 // Set up timer to clear OTP after 60 seconds
	        Timer timer = new Timer();
	        
	        timerTask =new TimerTask() {
				@Override
				@Transactional
				public void run() {
					otp = 0;
					userRepository.saveOtp(userName, otp);
				}
			};
	        timer.schedule(timerTask,AdminConstants.OTP_ACTIVE_SECONDS);
	        log.info("UsersServiceImpl.generateOtpForUser() executed successfully");
			return otp;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public Integer validateUserOtp(String email, String otp) {
		log.info("UsersServiceImpl.validateUserOtp() entered");
		int otpCode = Integer.parseInt(otp);
		log.info("UsersServiceImpl.validateUserOtp() is under execution...");
		int count = userRepository.validateUserOtp(email, otpCode);
		log.info("UsersServiceImpl.validateUserOtp() executed successfully");
		return count;
	}

	@Override
	@Transactional
	public Integer updatePasswordforUser(String email, CharSequence newRawPassword) {
		log.info("UsersServiceImpl.updatePasswordforUser() entered with args :");
		User user = userRepository.findByEmail(email);

		if (user == null) {
			return 0; // User is not found
		}
		if (passwordEncoder.matches(newRawPassword, user.getEncryptedPassword())) {
			return 0; // New password matching the current password
		}
		// Checking against the previous two passwords
		if (user.getPreviousPassword1() != null
				&& passwordEncoder.matches(newRawPassword, user.getPreviousPassword1())) {
			return 0; // New password matching the previous password
		}
		if (user.getPreviousPassword2() != null
				&& passwordEncoder.matches(newRawPassword, user.getPreviousPassword2())) {
			return 0; // New password matching the second previous password
		}
		log.info("UsersServiceImpl.updatePasswordforUser() is under execution");
		// Updating the previous password fields
		user.setPreviousPassword2(user.getPreviousPassword1());
		user.setPreviousPassword1(user.getEncryptedPassword());
		// Encode and set the new password
		String newEncodedPassword = passwordEncoder.encode(newRawPassword);
		user.setEncryptedPassword(newEncodedPassword);

		int updateStatus = userRepository.updatePassword(email, newEncodedPassword);
		log.info("UsersServiceImpl.updatePasswordforUser() executed successfully");
		return updateStatus;
	}

	@Override
	public Integer validateEmailAddress(String email) {
		log.info("UsersServiceImpl.validateEmailAddress() entered");
		log.info("UsersServiceImpl.validateEmailAddress() is under execution");
		int value = userRepository.validateEmail(email);
		log.info("UsersServiceImpl.validateEmailAddress() executed successfully");
		return value;
	}

	@Transactional
	@Override
	public Integer updateUserTwoFactorAuthStatus(String email, boolean isOn) {
		log.info("UsersServiceImpl.updateUserTwoFactorAuthStatus() entered");
		log.info("UsersServiceImpl.updateUserTwoFactorAuthStatus() executed successfully");
		return userRepository.updateTwofactorAuthenticationStatus(email, isOn);
	}

	@Override
	public UserVO getUser(String emailId) {
		log.info("UsersServiceImpl.getUserProfile() entered with args - emailId : " + emailId);
		if (emailId.equals("") || emailId == null || emailId.equals(null)) {
			log.info("UsersServiceImpl.getUserProfile() EmptyInputException : empty/null userid/emailid");
			throw new EmptyInputException(ErrorCodeMessages.ERR_USER_EMAIL_ID_NOT_FOUND_CODE,
					ErrorCodeMessages.ERR_USER_EMAIL_ID_NOT_FOUND_MSG);
		}
		log.info("UsersServiceImpl.getUserProfile() is under execution...");
		// get user details
		emailId=emailId.toLowerCase();
		User dbLoggedInUser = userRepository.findByEmail(emailId);
		/*
		 * // communicate with Employee microservice and get the employee object
		 * ResponseEntity<EmployeeVO> response = restTemplate
		 * .getForEntity("http://UMS-EMPLOYEE-SERVICE/employees/" + emailId,
		 * EmployeeVO.class); log.
		 * info("UsersServiceImpl.getUserProfile() : call to employee microservice successful"
		 * ); EmployeeVO employeeDetails = response.getBody(); if (employeeDetails ==
		 * null) throw new UsernameNotFoundException("User with " + emailId +
		 * " does not exist");
		 */
		UserVO user = new UserVO();
		user.setEmail(dbLoggedInUser.getEmail());
		user.setUserRoles(dbLoggedInUser.getUserRoles());
		user.setEncryptedPassword(dbLoggedInUser.getEncryptedPassword());
		user.setTwoFactorAuthentication(dbLoggedInUser.isTwoFactorAuthentication());
		user.setUserRoles(dbLoggedInUser.getUserRoles());
		// user.setEmployee(employeeDetails);
		user.setProfilePic(dbLoggedInUser.getProfilePic());
		user.setActive(dbLoggedInUser.isActive());
		//UserRoleMenuItemPermissionMapDTO userRPMDTO = userRoleMenuItemPermissionMapService.getUserRoleMenuItemPermissionMapByUserId(dbLoggedInUser.getEmail());
		//user.setUserRoleMenuItemPermissionMap(userRPMDTO);
		log.info("UsersServiceImpl.getUserProfile() executed successfully");
		return user;
	}

//	@Override
//	@Transactional
//	public User updateProfilePicByEmail(String email) {
//		log.info("UsersServiceImpl.updateProfilePicByEmail() is entered with args - "+ email);
//		if (email != null) {
//			log.info("UsersServiceImpl.updateProfilePicByEmail() is under execution");
//			User updateUser = userRepository.save(getUserDetailsByUsername(email));
//			log.info("UsersServiceImpl.updateProfilePicByEmail() executed successfully");
//			return updateUser;
//		}
//		return null;
//	}

	@Override
	public User updateUserProfilePic(String emailId, MultipartFile profilePicImage) throws IOException {
		log.info("UsersServiceImpl.updateUserProfilePic() entered");
		log.info("UsersServiceImpl.updateUserProfilePic() is under execution");
		User dbUser = userRepository.findByEmail(emailId);
		dbUser.setProfilePic(profilePicImage.getBytes());
		User updatedUser = userRepository.save(dbUser);
		log.info("UsersServiceImpl.updateUserProfilePic() executed successfully");
		return updatedUser;
	}

	@Override
	public List<String> getActiveUsersEmailIdList(boolean isActive) {
		log.info("UsersServiceImpl.getActiveUsersEmailIdList() entered");
		log.info("UsersServiceImpl.getActiveUsersEmailIdList() is under execution");
		List<String> userEmailIdList = userRepository.findAllActiveUsersEmailIdList(isActive);
		log.info("UsersServiceImpl.getActiveUsersEmailIdList() executed successfully");
		return userEmailIdList;
	}

	@Transactional
	@Override
	public User saveUser(User user) {
		log.info("UsersServiceImpl.createUser() entered with args - user");
		if (user == null || user.equals(null)) {
			log.info("UsersServiceImpl.createUser() EntityNotFoundException : user object is null");
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_USER_ENTITY_IS_NULL_CODE,
					ErrorCodeMessages.ERR_USER_ENTITY_IS_NULL_CODE);
		}
		log.info("UsersServiceImpl.createUser() is under execution...");
		String defaultEncryptedpassword = passwordEncoder.encode("Test@123");
		user.setEncryptedPassword(defaultEncryptedpassword);
		user.setActive(true);
		user.setOtpCode(0);
		user.setProfilePic(user.getProfilePic());
		String email= user.getEmail();
		User savedUser = userRepository.save(user);
		UserDTO userDTO =  null;
		if(savedUser != null) {
			userDTO = new UserDTO();
			mapper.map(savedUser, userDTO);
			System.out.println(savedUser);
			System.out.println(userDTO);
			try {
				List<UserRoleMenuItemPermissionMapDTO> userRoleMenuItemPermissionMapDTO = assignRoleMenuItemPermissionsToUser(userDTO);
				userRoleMenuItemPermissionMapService.saveAllUserRoleMenuItemPermissionMaps(userRoleMenuItemPermissionMapDTO);
			}catch (JsonProcessingException e) {
				// TODO: handle exception
			}
		}
		restTemplate.exchange("http://UMS-EMPLOYEE-SERVICE/employees/employeestatus-update/"+email,HttpMethod.PUT, null, boolean.class);
		log.info("UsersServiceImpl.createUser() executed successfully.");
		return savedUser;
	}
	

    RoleDTO roleDTO = null;
	private List<UserRoleMenuItemPermissionMapDTO> assignRoleMenuItemPermissionsToUser(UserDTO userDTO) throws JsonProcessingException {
		//if()
		Iterator<RoleDTO> roleIterator = userDTO.getUserRoles().iterator();
		while(roleIterator.hasNext()) {
			roleDTO = roleIterator.next();
		}
		List<MenuItemDTO> menuItemList = roleDTO.getMenuItems();
		//String menuItemListString = mapper.writeValueAsString(menuItemList);
//		StringBuilder menuItemBuilder = new StringBuilder();
//
//		for(int i= 0; i<menuItemList.size(); i++) {
//			menuItemBuilder.append(menuItemList.get(i).getMenuItemId());
//			if(i < menuItemList.size()-1) {
//				menuItemBuilder.append(",");
//			}
//		}
		
		PermissionDTO permissionDTO = roleDTO.getPermission();
		System.out.println(permissionDTO+" Permission DTO");
		System.out.println(menuItemList.size()+" Menu Item list size");
		//System.out.println(": Converted Menu Item String : "+menuItemBuilder.toString());
		List<UserRoleMenuItemPermissionMapDTO> userRoleMenuItemPermissionMapDTOList = new ArrayList<>();
		if(menuItemList.size() > 0) {
			menuItemList.forEach(menuItem -> {
				UserRoleMenuItemPermissionMapDTO userRoleMenuItemPermissionMapDTO = new UserRoleMenuItemPermissionMapDTO();
				userRoleMenuItemPermissionMapDTO.setEmail(userDTO.getEmail());
				userRoleMenuItemPermissionMapDTO.setRoleId(roleDTO.getRoleId());
				userRoleMenuItemPermissionMapDTO.setMenuItemIdList(menuItem.getMenuItemId().toString());
				userRoleMenuItemPermissionMapDTO.setPermissionIdList(permissionDTO.getPermissionValue());
				userRoleMenuItemPermissionMapDTOList.add(userRoleMenuItemPermissionMapDTO);
			});
		}else {
			UserRoleMenuItemPermissionMapDTO userRoleMenuItemPermissionMapDTO = new UserRoleMenuItemPermissionMapDTO();
			userRoleMenuItemPermissionMapDTO.setEmail(userDTO.getEmail());
			userRoleMenuItemPermissionMapDTO.setRoleId(roleDTO.getRoleId());
			userRoleMenuItemPermissionMapDTO.setMenuItemIdList("0");
			userRoleMenuItemPermissionMapDTO.setPermissionIdList(permissionDTO.getPermissionValue());
			userRoleMenuItemPermissionMapDTOList.add(userRoleMenuItemPermissionMapDTO);
		}
		
		return userRoleMenuItemPermissionMapDTOList;
	}

	@Transactional
	@Override
	public User updateUser(User user) {
		log.info("UsersServiceImpl.updateUser() entered with args - user");
		if (user == null || user.equals(null)) {
			log.info("UsersServiceImpl.updateUser() EntityNotFoundException : user object is null");
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_USER_ENTITY_IS_NULL_CODE,
					ErrorCodeMessages.ERR_USER_ENTITY_IS_NULL_MSG);
		}
		User dbUser = userRepository.findByEmail(user.getEmail());
		Long uiRoleId = user.getUserRoles().iterator().next().getRoleId();
		Long dbRoleId = dbUser.getUserRoles().iterator().next().getRoleId();
		if (dbUser != null) {
			dbUser.setActive(user.isActive());
			dbUser.setTwoFactorAuthentication(user.isTwoFactorAuthentication());
			dbUser.setUserRoles(user.getUserRoles());
			dbUser.setLoginAttempts(user.getLoginAttempts());
		}
		log.info("UsersServiceImpl.updateUser() is under execution...");
		User updatedUser = userRepository.save(dbUser);
		UserDTO userDTO = new UserDTO();
		if(updatedUser != null) {
			userDTO = new UserDTO();
			mapper.map(updatedUser, userDTO);
			if(dbRoleId != uiRoleId) {
				userRoleMenuItemPermissionMapService.deleteAllUserRoleMenuItemPermissionMapByUserId(dbUser.getEmail());
				try {
					List<UserRoleMenuItemPermissionMapDTO> userRoleMenuItemPermissionMapDTOList = assignRoleMenuItemPermissionsToUser(userDTO);
					userRoleMenuItemPermissionMapService.saveAllUserRoleMenuItemPermissionMaps(userRoleMenuItemPermissionMapDTOList);
				}catch (Exception e) {
					e.printStackTrace();
				}
	
			}
			
		}
		log.info("UsersServiceImpl.updateUser() executed successfully.");
		return updatedUser;
	}

	@Transactional(value = TxType.REQUIRED)
	@Override
	public void deleteUserByUserId(String emailId) {
		log.info("UsersServiceImpl.deleteUser() entered with args - id");
		if (emailId.equals(null) || emailId == null || emailId.equals("")) {
			log.info("UsersServiceImpl.deleteUser() EmptyInputException : emailid/userid is null");
			throw new EmptyInputException(ErrorCodeMessages.ERR_USER_EMAIL_ID_NOT_FOUND_CODE,
					ErrorCodeMessages.ERR_USER_EMAIL_ID_NOT_FOUND_MSG);
		}
		log.info("UsersServiceImpl.deleteUser() is under execution...");
		userRepository.deleteUserByUserId(emailId);
		userRoleMenuItemPermissionMapService.deleteAllUserRoleMenuItemPermissionMapByUserId(emailId);
		log.info("UsersServiceImpl.deleteUser() executed successfully");
		restTemplate.exchange("http://UMS-EMPLOYEE-SERVICE/employees/status-update/"+emailId,HttpMethod.PUT, null, boolean.class);
		log.info("UsersServiceImpl.createUser() executed successfully.");
	}

	@Override
	public User updateUserRoleByUserId(String emailId) {
		log.info("UsersServiceImpl.updateUserRoleByUserId() entered c with args - emailId");
		Set<Role> userRoleList = null;
		if (emailId.equals(null) || emailId == null) {
			throw new EmptyInputException(ErrorCodeMessages.ERR_USER_EMAIL_ID_NOT_FOUND_CODE,
					ErrorCodeMessages.ERR_USER_EMAIL_ID_NOT_FOUND_MSG);
		}
		log.info("UsersServiceImpl.updateUserRoleByUserId() is under execution...");
		User user = userRepository.findByEmail(emailId);
		// update user with new role, for now its harcoded, later will come from UI,
		// according to admin selection
		Optional<Role> optRole = roleService.getRoleById(2L);
		Role role = null;
		// if roles are present create a HashSet and push the roles into set
		if (optRole.isPresent()) {
			role = optRole.get();
			userRoleList = new HashSet<>();
			userRoleList.add(role);
		}
		// set the new role to user
		user.setUserRoles(userRoleList);
		log.info("UsersServiceImpl.updateUserRoleByUserId() executed successfully");
		return user;
	}

	@Override
	public List<User> getAllUsers() {
		// TODO Auto-generated method stub
		log.info("UserServiceImpl.getAllUsers() is entered");
		log.info("UserServiceImpl.getAllUsers() is under execution");
		List<User> userList = userRepository.findAll();
		log.info("UserServiceImpl.getAllUsers() executed successfully");
		return userList;
	}

	@Override
	public UserVO getUserProfile(String username) {
		log.info("UsersServiceImpl.getUserProfile() entered with args - emailId : " + username);
		if (username.equals("") || username == null || username.equals(null)) {
			log.info("UsersServiceImpl.getUserProfile() EmptyInputException : empty/null userid/emailid");
			throw new EmptyInputException(ErrorCodeMessages.ERR_USER_EMAIL_ID_NOT_FOUND_CODE,
					ErrorCodeMessages.ERR_USER_EMAIL_ID_NOT_FOUND_MSG);
		}
		log.info("UsersServiceImpl.getUserProfile() is under execution...");
		// get user details
		username=username.toLowerCase();
		User dbLoggedInUser = userRepository.findByEmail(username);
		// communicate with Employee microservice and get the employee object
		ResponseEntity<EmployeeVO> response = restTemplate
				.getForEntity("http://UMS-EMPLOYEE-SERVICE/employees/" + username, EmployeeVO.class);
		log.info("UsersServiceImpl.getUserProfile() : call to employee microservice successful");
		EmployeeVO employeeDetails = response.getBody();
		if (employeeDetails == null)
			throw new UsernameNotFoundException("Employee with " + username + " does not exist");
		UserVO user = new UserVO();
		user.setEmail(dbLoggedInUser.getEmail());
		user.setUserRoles(dbLoggedInUser.getUserRoles());
		user.setEncryptedPassword(dbLoggedInUser.getEncryptedPassword());
		user.setTwoFactorAuthentication(dbLoggedInUser.isTwoFactorAuthentication());
		user.setUserRoles(dbLoggedInUser.getUserRoles());
		user.setEmployee(employeeDetails);
		user.setProfilePic(dbLoggedInUser.getProfilePic());
		user.setActive(dbLoggedInUser.isActive());
		//UserRoleMenuItemPermissionMapDTO userRPMDTO = userRoleMenuItemPermissionMapService.getUserRoleMenuItemPermissionMapByUserId(username);
		//user.setUserRoleMenuItemPermissionMap(userRPMDTO);
		log.info("UsersServiceImpl.getUserProfile() executed successfully");
		return user;
	}

	@Transactional
	@Override
	public void deleteProfilePicOfUser(String emailId) {
		
		log.info("UsersServiceImpl.deleteProfilePicOfUser() Entered !");
		
		if (Strings.isNullOrEmpty(emailId)) {
			log.info("UsersServiceImpl.deleteProfilePicOfUser() email id is null !");
			throw new EmptyInputException(ErrorCodeMessages.ERR_USER_EMAIL_ID_IS_EMPTY_CODE,
					ErrorCodeMessages.ERR_USER_EMAIL_ID_IS_EMPTY_MSG);
		}
		User dbUser = userRepository.findByEmail(emailId);
		
		if (dbUser == null) {
			log.info("UsersServiceImpl.deleteProfilePicOfUser() the user is not found in the database !");
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_USER_DB_ENTITY_IS_NULL_CODE,
					ErrorCodeMessages.ERR_USER_DB_ENTITY_IS_NULL_MSG);
		}
		
		dbUser.setProfilePic(null);
		log.info("UsersServiceImpl.deleteProfilePicOfUser() before execution !");
		updateUser(dbUser);
		log.info("UsersServiceImpl.deleteProfilePicOfUser() execution sucessfull !");
	}

}
