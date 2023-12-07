package com.ikn.ums.admin.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.ikn.ums.admin.dto.UserRoleMenuItemPermissionMapDTO;
import com.ikn.ums.admin.entity.Role;
import com.ikn.ums.admin.exception.ErrorCodeMessages;
import com.ikn.ums.admin.exception.LoginAttemptsExceededException;
import com.ikn.ums.admin.exception.UserInactiveException;
import com.ikn.ums.admin.model.UserLoginRequestModel;
import com.ikn.ums.admin.repository.UserRepository;
import com.ikn.ums.admin.service.UserRoleMenuItemPermissionMapService;
import com.ikn.ums.admin.service.UserService;
import com.ikn.ums.admin.service.impl.UserRoleMenuItemPermissionMapServiceImpl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private UserService service;

	private Environment environment;

	private ModelMapper mapper = new ModelMapper();

	public UserAuthenticationFilter(UserService service, Environment environment, AuthenticationManager authManager) {
		this.service = service;
		this.environment = environment;
		super.setAuthenticationManager(authManager);
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
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
					throw new UserInactiveException(ErrorCodeMessages.ERR_USER_INACTIVE_CODE,
							ErrorCodeMessages.ERR_USER_INACTIVE_MSG);
				} else if (isActive && updatedUserWithLogginAttempts.getLoginAttempts() > 3) {
					updatedUserWithLogginAttempts.setActive(false);
					service.updateUser(updatedUserWithLogginAttempts);
					throw new LoginAttemptsExceededException(ErrorCodeMessages.ERR_USER_LOGIN_ATTEMPTS_EXCEEDED_CODE,
							ErrorCodeMessages.ERR_USER_LOGIN_ATTEMPTS_EXCEEDED_MSG);
				}
			}
			return getAuthenticationManager().authenticate(
					new UsernamePasswordAuthenticationToken(creds.getEmail(), creds.getPassword(), new ArrayList<>()));
		} // try
		catch (IOException ioe) {
			throw new RuntimeException(ioe);
		} // catch

	}// attemptAuth(req, res)

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		String userName = ((User) authResult.getPrincipal()).getUsername();
		log.info("UserAuthenticationFilter.successfulAuthentication()" + userName);
		// get employee and their department details
		UserVO loadedUser = service.getUser(userName);
		log.info("UserAuthenticationFilter.successfulAuthentication() " + loadedUser);

		// on sucessful auth set login attempts to 0
		UserDTO loggedInUser = service.getUserDetailsByUsername(userName);
		loggedInUser.setLoginAttempts(0);
		com.ikn.ums.admin.entity.User user = new com.ikn.ums.admin.entity.User();
		mapper.map(loggedInUser, user);
		service.updateUser(user);
		
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
		System.out.println(userRoleMenuItemsPermissionMap.toString());
		//optional:
		String userRoleMenuItemMapJsonString = new ObjectMapper().writeValueAsString(userRoleMenuItemsPermissionMap);
		response.addHeader("userRoleMenuItemsPermissionMap", userRoleMenuItemMapJsonString); //TODO: Check This one.
		
		Map<String, String> tokenData = new HashMap<String, String>();
		tokenData.put("token", webToken);
		tokenData.put("refreshToken", refreshToken);
		response.setContentType("application/json");
		new ObjectMapper().writeValue(response.getOutputStream(), tokenData);
	}

	public Map<String, String> getUserRoleMenuItemPermissions(UserDTO loggedInUser) {
		Map<String, String> userRoleMenuItemPermissionMap = new HashMap<String, String>();			
			loggedInUser.getUserRoleMenuItemPermissionMap().forEach(urmitDTO -> {
				userRoleMenuItemPermissionMap.put(urmitDTO.getMenuItemIdList(), urmitDTO.getPermissionIdList());
			});
		return userRoleMenuItemPermissionMap;
	}

}// class
