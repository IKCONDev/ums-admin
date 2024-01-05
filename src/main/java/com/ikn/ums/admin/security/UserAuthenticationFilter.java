package com.ikn.ums.admin.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ikn.ums.admin.VO.UserVO;
import com.ikn.ums.admin.dto.UserDTO;
import com.ikn.ums.admin.entity.Role;
import com.ikn.ums.admin.exception.ErrorCodeMessages;
import com.ikn.ums.admin.exception.LoginAttemptsExceededException;
import com.ikn.ums.admin.exception.UserInactiveException;
import com.ikn.ums.admin.model.UserLoginRequestModel;
import com.ikn.ums.admin.service.UserService;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private UserService service;

	private Environment environment;

	private ModelMapper mapper = new ModelMapper();

	public UserAuthenticationFilter(UserService service, Environment environment, AuthenticationManager authManager) {
		log.info("UserAuthenticationFilter() constructor entered with args - UserService,Environment and AuthenticationManager Objects.");
		this.service = service;
		this.environment = environment;
		super.setAuthenticationManager(authManager);
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		log.info("attemptAuthentication() entered with args - HttpRequest and HttpResponse Objects.");
		int i = 1;
		try {
			UserLoginRequestModel creds = new ObjectMapper().readValue(request.getInputStream(),
					UserLoginRequestModel.class);
			UserVO loadedUser = service.getUser(creds.getEmail());
			if (loadedUser != null) {
				boolean isActive = loadedUser.isActive();
				UserDTO loginAttemptedUser = service.getUserDetailsByUsername(creds.getEmail());
				// get user datails and update login attempts
				loginAttemptedUser.setLoginAttempts(loginAttemptedUser.getLoginAttempts() + i);
				com.ikn.ums.admin.entity.User user = new com.ikn.ums.admin.entity.User();
				mapper.map(loginAttemptedUser, user);
				com.ikn.ums.admin.entity.User updatedUserWithLogginAttempts = service.updateUser(user);
				if (!isActive) {
					log.error(" attemptAuthentication() Error occured while attempting to Login , UserInactiveException : User is inactive - Cannot login");
					throw new UserInactiveException(ErrorCodeMessages.ERR_USER_INACTIVE_CODE,
							ErrorCodeMessages.ERR_USER_INACTIVE_MSG);
				} else if (isActive && updatedUserWithLogginAttempts.getLoginAttempts() > 3) {
					updatedUserWithLogginAttempts.setActive(false);
					service.updateUser(updatedUserWithLogginAttempts);
					log.error(" attemptAuthentication() Error occured while attempting to Login , LoginAttemptsExceededException : User login attempts exceeded more then 3.");
					throw new LoginAttemptsExceededException(ErrorCodeMessages.ERR_USER_LOGIN_ATTEMPTS_EXCEEDED_CODE,
							ErrorCodeMessages.ERR_USER_LOGIN_ATTEMPTS_EXCEEDED_MSG);
				}
			}
			log.info("attemptAuthentication() excuted succesfully.");
			return getAuthenticationManager().authenticate(
					new UsernamePasswordAuthenticationToken(creds.getEmail(), creds.getPassword(), new ArrayList<>()));
		} 
		catch (IOException ioe) {
			log.error("attemptAuthentication() Exception occured while Login ."+ioe.getMessage(), ioe);
			throw new RuntimeException(ioe);
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		log.info("UserAuthenticationFilter.successfulAuthentication() entered with args - HttpRequest , HttpResponse , FilterChain & Authentication objects.");
		String userName = ((User) authResult.getPrincipal()).getUsername();
		UserVO loadedUser = service.getUser(userName);
		// on sucessful auth set login attempts to 0
		UserDTO loggedInUser = service.getUserDetailsByUsername(userName);
		loggedInUser.setLoginAttempts(0);
		com.ikn.ums.admin.entity.User user = new com.ikn.ums.admin.entity.User();
		mapper.map(loggedInUser, user);
		service.updateUser(user);
		log.info("successfulAuthentication() : Login attempts reset to 0.");
		
		Map <String, String> userRoleMenuItemsPermissionMap = getUserRoleMenuItemPermissions(loggedInUser);
		
		/*
		 * if(loadedUser == null) { throw new
		 * UserNotFoundException(ErrorCodeMessages.ERR_USER_NOT_FOUND_CODE,
		 * ErrorCodeMessages.ERR_USER_NOT_FOUND_MSG); }
		 */
		String webToken = Jwts.builder().setSubject(loadedUser.getEmail())
				.setExpiration(new Date(
						System.currentTimeMillis() + Long.parseLong(environment.getProperty("token.expiration_time"))))
				.signWith(SignatureAlgorithm.HS512, environment.getProperty("token.secret"))
				.setIssuer(request.getRequestURL().toString()).claim("role", loadedUser.getUserRoles().iterator().next().getRoleName())
				.compact();

		String refreshToken = Jwts.builder().setSubject(loadedUser.getEmail())
				.setExpiration(new Date(
						System.currentTimeMillis() + Long.parseLong(environment.getProperty("token.expiration_time"))))
				.signWith(SignatureAlgorithm.HS512, environment.getProperty("token.secret"))
				.setIssuer(request.getRequestURL().toString()).claim("role", loadedUser.getUserRoles().iterator().next().getRoleName())
				.compact();
		log.info("successfulAuthentication() : Jwt Token created for the user "+userName);
		//set a default buffer size
		//response.setBufferSize(10000);
		response.addHeader("token", webToken);
		response.addHeader("refreshToken", refreshToken);
		Iterator<Role> itr = loadedUser.getUserRoles().iterator();
		Role role = null;
		while (itr.hasNext()) {
			role = itr.next();
		}
		response.addHeader("userRole", role.getRoleName());
		response.addHeader("email", loadedUser.getEmail());
		response.addHeader("twoFactorAuth", Boolean.toString(loadedUser.isTwoFactorAuthentication()));
		response.addHeader("jwtExpiry",
				new Date(System.currentTimeMillis() + Long.parseLong(environment.getProperty("token.expiration_time")))
						.toString());
		//optional:
		String userRoleMenuItemMapJsonString = new ObjectMapper().writeValueAsString(userRoleMenuItemsPermissionMap);
		response.addHeader("userRoleMenuItemsPermissionMap", userRoleMenuItemMapJsonString);
		log.info("successfulAuthentication() : User Role Menu Item Permission Map retrived for the user "+userName+" and returned in response object to client application.");
		Map<String, String> tokenData = new HashMap<String, String>();
		tokenData.put("token", webToken);
		tokenData.put("refreshToken", refreshToken);
		response.setContentType("application/json");
		new ObjectMapper().writeValue(response.getOutputStream(), tokenData);
		log.info("successfulAuthentication() executed successfully.");
		log.info("successfulAuthentication() User Authentication sucessfull.");
	}

	public Map<String, String> getUserRoleMenuItemPermissions(UserDTO loggedInUser) {
		log.info("getUserRoleMenuItemPermissions() entered with args - user object.");
		Map<String, String> userRoleMenuItemPermissionMap = new HashMap<String, String>();			
			loggedInUser.getUserRoleMenuItemPermissionMap().forEach(urmitDTO -> {
				userRoleMenuItemPermissionMap.put(urmitDTO.getMenuItemIdList(), urmitDTO.getPermissionIdList());
			});
		log.info("getUserRoleMenuItemPermissions() - UserRoleMenuItemPermission Map object created.");
		log.info("getUserRoleMenuItemPermissions() executed successfully.");
		return userRoleMenuItemPermissionMap;
	}

}// class
