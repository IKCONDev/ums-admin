package com.ikn.ums.users.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ikn.ums.users.VO.UserVO;
import com.ikn.ums.users.model.UserLoginRequestModel;
import com.ikn.ums.users.service.UsersService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class UserAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private UsersService service;

	private Environment environment;

	public UserAuthenticationFilter(UsersService service, Environment environment, AuthenticationManager authManager) {
		this.service = service;
		this.environment = environment;
		super.setAuthenticationManager(authManager);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {

		try {
			UserLoginRequestModel creds = new ObjectMapper().readValue(request.getInputStream(),
					UserLoginRequestModel.class);

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
		System.out.println("UserAuthenticationFilter.successfulAuthentication()" +userName);
		//get employee and their department details
		UserVO loadedUser = service.getUserProfile(userName);
		System.out.println("UserAuthenticationFilter.successfulAuthentication() "+loadedUser);

		String webToken = Jwts.builder().setSubject(loadedUser.getEmail())
				.setExpiration(new Date(
						System.currentTimeMillis() + Long.parseLong(environment.getProperty("token.expiration_time"))))
				.signWith(SignatureAlgorithm.HS512, environment.getProperty("token.secret"))
				.setIssuer(request.getRequestURL().toString()).claim("role", loadedUser.getUserRole()).compact();

		String refreshToken = Jwts.builder().setSubject(loadedUser.getEmail())
				.setExpiration(new Date(
						System.currentTimeMillis() + Long.parseLong(environment.getProperty("token.expiration_time"))))
				.signWith(SignatureAlgorithm.HS512, environment.getProperty("token.secret"))
				.setIssuer(request.getRequestURL().toString()).claim("role", loadedUser.getUserRole()).compact();
		response.addHeader("token", webToken);
		response.addHeader("refreshToken", refreshToken);
		System.out.println(webToken);
		//response.addHeader("userId", loadedUser.getId().toString());
		response.addHeader("userRole", loadedUser.getUserRole());
		response.addHeader("firstName", loadedUser.getEmployee().getFirstName());
		response.addHeader("lastName", loadedUser.getEmployee().getLastName());
		response.addHeader("email", loadedUser.getEmail());
		response.addHeader("twoFactorAuth", Boolean.toString(loadedUser.isTwoFactorAuthentication()));
		//response.addHeader("department", loadedUser.getDepartment());
		//response.addHeader("Access-Control-Allow-Origin", "*");
		Map<String, String> tokenData = new HashMap<String, String>();
		tokenData.put("token", webToken);
		tokenData.put("refreshToken", refreshToken);
		response.setContentType("application/json");
		new ObjectMapper().writeValue(response.getOutputStream(), tokenData);
	}
}// class
