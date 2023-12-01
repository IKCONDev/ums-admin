package com.ikn.ums.admin.security;

import javax.servlet.Filter;

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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ikn.ums.admin.service.UserService;

@Configuration
@EnableWebSecurity
public class UsersWebSecurity {
	
	private Environment environment;
	
	private UserService service;
	
	private BCryptPasswordEncoder encoder;
	
	@Autowired
	public UsersWebSecurity(Environment environment, UserService service,BCryptPasswordEncoder encoder){	
		this.environment = environment;
		this.service= service;
		this.encoder = encoder;
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
		//disable csrf 
				http.cors().disable();
				http.csrf().disable();
				//provide authorization
				http.authorizeRequests()
				.antMatchers("/users/**")
				.permitAll()
				//.antMatchers("/users/login").permitAll()
				.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
				//register your custom authentication filter with spring security
				.and()
				.addFilter(getAuthenticationFilter(http));
				
				//working with IP, instead of permit all
				//.hasIpAddress(env.getProperty("spring.cloud.gateway.ip"));
				
				//if you are using h2 as db, to view console
				//http.headers().frameOptions().disable();
				return http.build();
	}
	

	/*
	 * old code used while extending WebSecurityConfigurerAdapter
	 
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//disable csrf 
		http.cors().disable();
		http.csrf().disable();
		//provide authorization
		http.authorizeRequests()
		.antMatchers("/users/**")
		.permitAll()
		//.antMatchers("/users/login").permitAll()
		.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
		//register your custom authentication filter with spring security
		.and()
		.addFilter(getAuthenticationFilter());
		
		//working with IP, instead of permit all
		//.hasIpAddress(env.getProperty("spring.cloud.gateway.ip"));
		
		//if you are using h2 as db, to view console
		//http.headers().frameOptions().disable();
	}
	*/
	
	
	public UserAuthenticationFilter getAuthenticationFilter(HttpSecurity http) throws Exception{
		//set authentication manager on your auth filter
		UserAuthenticationFilter authenticationFilter = new UserAuthenticationFilter(service,environment,authenticationManager(http.getSharedObject(AuthenticationConfiguration.class)));
		//set authentication manager on your auth filter
		//authenticationFilter.setAuthenticationManager(authenticationManager());
		authenticationFilter.setFilterProcessesUrl(environment.getProperty("login.url.path"));
		return authenticationFilter;
	}
	
	/*
	public UserAuthenticationFilter getAuthenticationFilter() throws Exception{
		//set authentication manager on your auth filter
		UserAuthenticationFilter authenticationFilter = new UserAuthenticationFilter(service,environment,authenticationManager());
		//set authentication manager on your auth filter
		//authenticationFilter.setAuthenticationManager(authenticationManager());
		authenticationFilter.setFilterProcessesUrl(environment.getProperty("login.url.path"));
		return authenticationFilter;
	}
	*/
	
	/*
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(service).passwordEncoder(encoder);
	}
	*/
	
	
	/**
	 * 
	 * @param authenticationConfiguration
	 * @return the authentication manager required for while authetication
	 * @throws Exception
	 */
	@Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
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
		return auth;
	}
	
	//TODO: Check to remove Override and this function need to be tested
	//@Override
	protected void configure(HttpSecurity http) throws Exception {
	    http.authorizeRequests()
	        .antMatchers("/public/**").permitAll()
	        .antMatchers("/admin/**").hasAnyRole("ADMIN", "SUPER_ADMIN")
	        .antMatchers("/user/**").hasAnyRole("USER", "MANAGER")
	        .anyRequest().authenticated()
	        .and()
	        .formLogin().loginPage("/login").permitAll()
	        .and()
	        .logout().permitAll();

	    // Dynamic menu items and permissions based on the user's roles
	    http.addFilterBefore(menuItemsAndPermissionsFilter(), UsernamePasswordAuthenticationFilter.class);
	}

	@Bean
    public Filter menuItemsAndPermissionsFilter() {
        return new MenuItemsAndPermissionsFilter();
    }
//	
//	private String[] getDynamicAntMatchers() {
//		// Fetch dynamic menu items based on the user's roles
//		//RoleController roleController = new RoleController();
//		
//		RoleService roleService = new RoleServiceImpl();
//		
//		List<Role> roleList = roleService.getAllRoles();
//	
//		return roleList.stream().map(menuItem -> menuItem.getMenuItems()).toArray(String[]::new);
//		
//		//List<Role> dynamicMenuItems = roleController.getAllRoles();
//
//		// Convert dynamic menu items to antMatchers strings
//		//return dynamicMenuItems.stream().map(menuItem -> "/v1/" + menuItem.getPath()) // Adjust the path as needed
//				//.toArray(String[]::new);
//	}
	
}
