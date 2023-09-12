package com.ikn.ums.authentication.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "user_master")
@Data
public class UserDetailsEntity{
	
	@Id
	@Column(name = "email", nullable = false, unique = true)
	private String email;
	
	@Column(name = "encrypted_password", nullable = false, unique = false)
	private String encryptedPassword;
	
	@Column(name = "role", nullable = false, unique = false)
	private String userRole;
	
	@Column(name = "otp_code", nullable = true, unique = false)
	private int otpCode;
	
	@Column(name = "two_factor_authentication", nullable = true, unique = false)
	private boolean twoFactorAuthentication;

	
}
