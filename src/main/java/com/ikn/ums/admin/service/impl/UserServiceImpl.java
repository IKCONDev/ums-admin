package com.ikn.ums.admin.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
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
import com.ikn.ums.admin.VO.EmployeeVO;
import com.ikn.ums.admin.VO.UserVO;
import com.ikn.ums.admin.dto.MenuItemDTO;
import com.ikn.ums.admin.dto.PermissionDTO;
import com.ikn.ums.admin.dto.RoleDTO;
import com.ikn.ums.admin.dto.UserDTO;
import com.ikn.ums.admin.dto.UserRoleMenuItemPermissionMapDTO;
import com.ikn.ums.admin.entity.Role;
import com.ikn.ums.admin.entity.User;
import com.ikn.ums.admin.exception.EmptyInputException;
import com.ikn.ums.admin.exception.EntityNotFoundException;
import com.ikn.ums.admin.exception.ErrorCodeMessages;
import com.ikn.ums.admin.exception.ImageNotFoundException;
import com.ikn.ums.admin.repository.UserRepository;
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
	private UserRoleMenuItemPermissionMapService userRoleMenuItemPermissionMapService;

	int otp = 0;
	
	int otpExecutionCount = 0;
	
	TimerTask timerTask;
	
	@Autowired
	private ModelMapper mapper;
	
	@Override
	public UserDTO getUserDetailsByUsername(String email) {
		log.info("UsersServiceImpl.getUserDetailsByUsername() entered");
		log.info("UsersServiceImpl.getUserDetailsByUsername() is under execution");
		email=email.toLowerCase();
		User loadedUser = userRepository.findByEmail(email);
		if (loadedUser == null) {
			log.info("UsersServiceImpl.getUserDetailsByUsername() UsernameNotFoundException User with email" + email + " does not exist");
			throw new UsernameNotFoundException("User with " + email + " is not exist");
		}
		UserDTO userDTO = new UserDTO();
		mapper.map(loadedUser, userDTO);
		List<UserRoleMenuItemPermissionMapDTO> userRPMDTO = userRoleMenuItemPermissionMapService.getUserRoleMenuItemPermissionMapsByUserId(email);
		userDTO.setUserRoleMenuItemPermissionMap(userRPMDTO);
		log.info("UsersServiceImpl.getUserDetailsByUsername() executed successfully");
		return userDTO;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
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
		if(Strings.isNullOrEmpty(userName)) {
			log.info("UsersServiceImpl.generateOtpForUser() EmptyInputException : userId / emailId is empaty.");
			throw new EmptyInputException(ErrorCodeMessages.ERR_USER_EMAIL_ID_IS_EMPTY_MSG, 
					ErrorCodeMessages.ERR_USER_EMAIL_ID_IS_EMPTY_MSG);
		}
		log.info("UsersServiceImpl.generateOtpForUser() is entered with args:");
		otpExecutionCount = 1;
		log.info("UsersServiceImpl.generateOtpForUser() : userName :" + userName);
		Random r = new Random();
		String mailHeading = null;
		if (pageType.equals("TwoFactorAuth")) {
			mailHeading = "One Time Password for Secure Login ";
		} else if (pageType.equals("ForgotPassword")) {
			mailHeading = "One Time Password for Secure Login";
		}
		try {
			log.info("UsersServiceImpl.generateOtpForUser() is under execution");
			int random = r.nextInt(899999)+100000;
			for (int i = 0; i < random; i++) {
				System.out.println("executed " + i);
				otp = random;
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
		if(Strings.isNullOrEmpty(email)) {
			log.info("UsersServiceImpl.validateUserOtp() EmptyInputException : userId / emailId is empaty.");
			throw new EmptyInputException(ErrorCodeMessages.ERR_USER_EMAIL_ID_IS_EMPTY_MSG, 
					ErrorCodeMessages.ERR_USER_EMAIL_ID_IS_EMPTY_MSG);
		}
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
		if(Strings.isNullOrEmpty(email)) {
			log.info("UsersServiceImpl.updatePasswordforUser() EmptyInputException : userId / emailId is empaty.");
			throw new EmptyInputException(ErrorCodeMessages.ERR_USER_EMAIL_ID_IS_EMPTY_MSG, 
					ErrorCodeMessages.ERR_USER_EMAIL_ID_IS_EMPTY_MSG);
		}
		if(Strings.isNullOrEmpty(newRawPassword.toString())) {
			log.info("UsersServiceImpl.updatePasswordforUser() EmptyInputException : userId / emailId is empaty.");
			throw new EmptyInputException(ErrorCodeMessages.ERR_USER_NEW_PASSWORD_IS_EMPTY_CODE, 
					ErrorCodeMessages.ERR_USER_NEW_PASSWORD_IS_EMPTY_MSG);
		}
		User user = userRepository.findByEmail(email);
		if (user == null) {
			return 0; // User is not found with provided email ID
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
		if(Strings.isNullOrEmpty(email)) {
			log.info("UsersServiceImpl.validateEmailAddress() EmptyInputException : userId / emailId is empaty.");
			throw new EmptyInputException(ErrorCodeMessages.ERR_USER_EMAIL_ID_IS_EMPTY_MSG, 
					ErrorCodeMessages.ERR_USER_EMAIL_ID_IS_EMPTY_MSG);
		}
		log.info("UsersServiceImpl.validateEmailAddress() entered");
		log.info("UsersServiceImpl.validateEmailAddress() is under execution");
		int value = userRepository.validateEmail(email);
		log.info("UsersServiceImpl.validateEmailAddress() executed successfully");
		return value;
	}

	@Transactional
	@Override
	public Integer updateUserTwoFactorAuthStatus(String email, boolean isOn) {
		if(Strings.isNullOrEmpty(email)) {
			log.info("UsersServiceImpl.updateUserTwoFactorAuthStatus() EmptyInputException : userId / emailId is empty.");
			throw new EmptyInputException(ErrorCodeMessages.ERR_USER_EMAIL_ID_IS_EMPTY_MSG, 
					ErrorCodeMessages.ERR_USER_EMAIL_ID_IS_EMPTY_MSG);
		}
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
		emailId=emailId.toLowerCase();
		User dbLoggedInUser = userRepository.findByEmail(emailId);
		UserVO user = new UserVO();
		user.setEmail(dbLoggedInUser.getEmail());
		user.setUserRoles(dbLoggedInUser.getUserRoles());
		user.setEncryptedPassword(dbLoggedInUser.getEncryptedPassword());
		user.setTwoFactorAuthentication(dbLoggedInUser.isTwoFactorAuthentication());
		user.setUserRoles(dbLoggedInUser.getUserRoles());
		user.setProfilePic(dbLoggedInUser.getProfilePic());
		user.setActive(dbLoggedInUser.isActive());
		log.info("UsersServiceImpl.getUserProfile() executed successfully");
		return user;
	}

	@Override
	public User updateUserProfilePic(String emailId, MultipartFile profilePicImage) throws IOException {
		if (emailId.equals("") || emailId == null || emailId.equals(null)) {
			log.info("UsersServiceImpl.updateUserProfilePic() EmptyInputException : empty/null userid/emailid");
			throw new EmptyInputException(ErrorCodeMessages.ERR_USER_EMAIL_ID_NOT_FOUND_CODE,
					ErrorCodeMessages.ERR_USER_EMAIL_ID_NOT_FOUND_MSG);
		}
		String contentType = profilePicImage.getContentType();
		if(contentType==null) {
			throw new ImageNotFoundException(ErrorCodeMessages.ERR_USER_IMAGE_NULL_CODE,
					ErrorCodeMessages.ERR_USER_IMAGE_NULL_MSG);
		}
		if (!contentType.startsWith("image/")) {
			log.info("updateUserProfilePic()  ImageNotFoundException : Invalid image format or image not valid. ");
			throw new ImageNotFoundException(ErrorCodeMessages.ERR_USER_IMAGE_NOT_VALID_CODE,
					ErrorCodeMessages.ERR_USER_IMAGE_NOT_VALID_MSG);
		}
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
	public UserDTO saveUser(UserDTO user) {
		log.info("UsersServiceImpl.createUser() entered with args - user");
		if (user == null || user.equals(null)) {
			log.info("UsersServiceImpl.createUser() EntityNotFoundException : user object is null");
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_USER_ENTITY_IS_NULL_CODE,
					ErrorCodeMessages.ERR_USER_ENTITY_IS_NULL_MSG);
		}
		log.info("UsersServiceImpl.createUser() is under execution...");
		String defaultEncryptedpassword = passwordEncoder.encode("Test@123");
		user.setEncryptedPassword(defaultEncryptedpassword);
		user.setActive(true);
		user.setOtpCode(0);
		user.setProfilePic(user.getProfilePic());
		User entity = new User();
		mapper.map(user, entity);
		User savedUser = userRepository.save(entity);
		UserDTO userDTO =  null;
		if(savedUser != null) {
			userDTO = new UserDTO();
			mapper.map(savedUser, userDTO);
				List<UserRoleMenuItemPermissionMapDTO> userRoleMenuItemPermissionMapDTO = assignRoleMenuItemPermissionsToUser(userDTO);
				userRoleMenuItemPermissionMapService.saveAllUserRoleMenuItemPermissionMaps(userRoleMenuItemPermissionMapDTO);
		}
		restTemplate.exchange("http://UMS-EMPLOYEE-SERVICE/employees/employeestatus-update/"+user.getEmail(),HttpMethod.PUT, null, boolean.class);
		log.info("createUser() call to employee microservice successfull.");
		log.info("createUser() executed successfully.");
		return userDTO;
	}
	

    RoleDTO roleDTO = null;
	private List<UserRoleMenuItemPermissionMapDTO> assignRoleMenuItemPermissionsToUser(UserDTO userDTO){
		log.info("assignRoleMenuItemPermissionsToUser() entered with args - User object");
		log.info("assignRoleMenuItemPermissionsToUser() is under execution...");
		Iterator<RoleDTO> roleIterator = userDTO.getUserRoles().iterator();
		while(roleIterator.hasNext()) {
			roleDTO = roleIterator.next();
		}
		List<MenuItemDTO> menuItemList = roleDTO.getMenuItems();
		PermissionDTO permissionDTO = roleDTO.getPermission();
		log.info(permissionDTO+" Permission list");
		log.info(menuItemList+" Menu Item list");
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
		log.info("assignRoleMenuItemPermissionsToUser() "+userRoleMenuItemPermissionMapDTOList+" UserRoleMenuItemPermissions assigned to user.");
		log.info("assignRoleMenuItemPermissionsToUser() executed successfully.");
		return userRoleMenuItemPermissionMapDTOList;
	}

	@Transactional
	@Override
	public UserDTO updateUser(UserDTO user) {
		log.info("UsersServiceImpl.updateUser() entered with args - user");
		if (user == null || user.equals(null)) {
			log.info("UsersServiceImpl.updateUser() EntityNotFoundException : user object is null");
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_USER_ENTITY_IS_NULL_CODE,
					ErrorCodeMessages.ERR_USER_ENTITY_IS_NULL_MSG);
		}
		Set<Role> roleSet = new HashSet<>();
		user.getUserRoles().forEach(dto -> {
			Role entity = new Role();
			mapper.map(dto, entity);
			roleSet.add(entity);
		});
		User dbUser = userRepository.findByEmail(user.getEmail());
		Long uiRoleId = user.getUserRoles().iterator().next().getRoleId();
		Long dbRoleId = dbUser.getUserRoles().iterator().next().getRoleId();
		if (dbUser != null) {
			dbUser.setActive(user.isActive());
			dbUser.setTwoFactorAuthentication(user.isTwoFactorAuthentication());
			dbUser.setUserRoles(roleSet);
			dbUser.setLoginAttempts(user.getLoginAttempts());
		}
		log.info("UsersServiceImpl.updateUser() is under execution...");
		User updatedUser = userRepository.save(dbUser);
		UserDTO userDTO = null;
		if(updatedUser != null) {
			userDTO = new UserDTO();
			mapper.map(updatedUser, userDTO);
			if(dbRoleId != uiRoleId) {
				userRoleMenuItemPermissionMapService.deleteAllUserRoleMenuItemPermissionMapByUserId(dbUser.getEmail());
				List<UserRoleMenuItemPermissionMapDTO> userRoleMenuItemPermissionMapDTOList = assignRoleMenuItemPermissionsToUser(userDTO);
				userRoleMenuItemPermissionMapService.saveAllUserRoleMenuItemPermissionMaps(userRoleMenuItemPermissionMapDTOList);
			}
		}
		log.info("UsersServiceImpl.updateUser() executed successfully.");
		return userDTO;
	}

	@Transactional(value = TxType.REQUIRED)
	@Override
	public void deleteUserByUserId(String emailId) {
		log.info("UsersServiceImpl.deleteUser() entered with args - id");
		if (Strings.isNullOrEmpty(emailId) || emailId.isEmpty()) {
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
	public List<UserDTO> getAllUsers() {
		log.info("UserServiceImpl.getAllUsers() is entered");
		log.info("UserServiceImpl.getAllUsers() is under execution");
		List<UserDTO> userDTOList = new ArrayList<>();
		List<User> userList = userRepository.findAll();
		userList.forEach(entity -> {
			UserDTO dto = new UserDTO();
			mapper.map(entity, dto);
			userDTOList.add(dto);
		});
		log.info("UserServiceImpl.getAllUsers() executed successfully");
		return userDTOList;
	}

	@Override
	public UserVO getUserProfile(String emailId) {
		log.info("UsersServiceImpl.getUserProfile() entered with args - emailId : " + emailId);
		if (Strings.isNullOrEmpty(emailId)) {
			log.info("UsersServiceImpl.getUserProfile() EmptyInputException : empty/null userid/emailid");
			throw new EmptyInputException(ErrorCodeMessages.ERR_USER_EMAIL_ID_NOT_FOUND_CODE,
					ErrorCodeMessages.ERR_USER_EMAIL_ID_NOT_FOUND_MSG);
		}
		log.info("UsersServiceImpl.getUserProfile() is under execution...");
		emailId=emailId.toLowerCase();
		User dbLoggedInUser = userRepository.findByEmail(emailId);
		// communicate with Employee microservice and get the employee object
		ResponseEntity<EmployeeVO> response = restTemplate
				.getForEntity("http://UMS-EMPLOYEE-SERVICE/employees/" + emailId, EmployeeVO.class);
		log.info("UsersServiceImpl.getUserProfile() : call to employee microservice successful");
		EmployeeVO employeeDetails = response.getBody();
		if (employeeDetails == null) {
			log.info("getUserProfile() UsernameNotFoundException: user with provided emailId / userId doesn't exists.");
			throw new UsernameNotFoundException("Employee with " + emailId + " does not exist");
		}
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
		UserDTO userdto = new UserDTO();
		mapper.map(dbUser, userdto);
		log.info("UsersServiceImpl.deleteProfilePicOfUser() before execution !");
		updateUser(userdto);
		log.info("UsersServiceImpl.deleteProfilePicOfUser() execution sucessfull !");
	}

}
