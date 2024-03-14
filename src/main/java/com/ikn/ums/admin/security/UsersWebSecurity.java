package com.ikn.ums.admin.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.ikn.ums.admin.service.UserService;
import com.ikn.ums.admin.utils.EmailService;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableWebSecurity
@Slf4j
public class UsersWebSecurity {
	
	private Environment environment;
	
	private UserService service;
	
	private BCryptPasswordEncoder encoder;
	
	private EmailService emailService;
	
	@Autowired
	public UsersWebSecurity(Environment environment, UserService service,BCryptPasswordEncoder encoder,
			EmailService emailService){	
		this.environment = environment;
		this.service= service;
		this.encoder = encoder;
		this.emailService = emailService;
	}
	
	/**
	 * the web security configurer adapter is deprecated , 
	 * going forward for registering a security filter chain would be as shown below
	 * @param http
	 * @return
	 * @throws Exception
	 */
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		// disable csrf
		http.cors().disable();
		http.csrf().disable();
		// provide authorization
		http.authorizeRequests()
				.antMatchers("/users/**").permitAll()
				.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
				// register your custom authentication filter with spring security
				.and().addFilter(getAuthenticationFilter(http));
		log.info("filterChain() : SecurityFilterChain bean object created.");
		return http.build();
			
	}
	
	public UserAuthenticationFilter getAuthenticationFilter(HttpSecurity http) throws Exception{
		log.info("getAuthenticationFilter() entered with args HttpSecurity object");
		//set authentication manager on your auth filter
		UserAuthenticationFilter authenticationFilter = new UserAuthenticationFilter(service,environment,authenticationManager(http.getSharedObject(AuthenticationConfiguration.class)),emailService,encoder);
		//set authentication manager on your auth filter
		authenticationFilter.setFilterProcessesUrl(environment.getProperty("login.url.path"));
		log.info("getAuthenticationFilter() executed successfully.");
		return authenticationFilter;
	}
	
	
	/**
	 * 
	 * @param authenticationConfiguration
	 * @return the authentication manager required for while authetication
	 * @throws Exception
	 */
	@Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
		log.info("getAuthenticationFilter() : AuthenticationManager bean object created.");
        return authenticationConfiguration.getAuthenticationManager();
    }
    
	
	/**
	 * 
	 * @return the authentication Manager Builder with service and password encoder
	 */
	@Bean 
	public DaoAuthenticationProvider autheticationProvider() {
		DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
		auth.setUserDetailsService(service);
		auth.setPasswordEncoder(encoder);
		log.info("autheticationProvider() : DaoAuthenticationProvider bean object created.");
		return auth;
	}
	
}
