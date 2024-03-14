package com.ikn.ums.admin.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "user_master_tab")
@Data
public class User{
	
	@Id
	@Column(name = "user_id", nullable = false, unique = true)
	private String email;
	
	@Column(name = "encrypted_password", nullable = false, unique = false)
	private String encryptedPassword;
	
	@Column(name="previousPassword1",nullable=true, unique = false)
	private String previousPassword1;
	
	@Column(name="previousPassword2",nullable=true, unique = false)
	private String previousPassword2;

	@Column(name="previousPassword",nullable=true, unique = false)
	private String previousPassword;
	
	@ManyToMany(cascade = {CascadeType.REFRESH, CascadeType.PERSIST}, fetch = FetchType.EAGER)
	@JoinTable(
				name = "user_role_tab",
				joinColumns = @JoinColumn(name = "user_id"),
				inverseJoinColumns = @JoinColumn(name = "role_id")
			)
	private Set<Role> userRoles = new HashSet<>();
	
	@Column(name = "otp_code", nullable = true, unique = false)
	private int otpCode;
	
	@Column(name = "two_factor_authentication", nullable = true, unique = false)
	private boolean twoFactorAuthentication;	
	
	
	@Column(name ="profile_pic",nullable=true)
	private byte[] profilePic;
	
	@Column(name = "isActive")
	private boolean isActive;
	
	@Column(name = "loginAttempts")
	private int loginAttempts;
	
	@Column(name = "loginAttemptedClientIP")
	private String loginAttemptedClientIP;
	
	@Column(name = "loginAttemptedClientDeviceType")
	private String loginAttemptedClientDeviceType;
	
	@Column(name = "loginAttemptedDateTime")
	private LocalDateTime loginAttemptedDateTime;
	
	@Column(name = "createdDateTime")
	private LocalDateTime createdDateTime;
	
	@Column(name = "modifiedDateTime")
	private LocalDateTime modifiedDateTime;
	
	@Column(name = "createdBy")
	private String createdBy;
	
	@Column(name = "modifiedBy")
	private String modifiedBy;
	
	@Column(name = "createdByEmailId")
	private String createdByEmailId;
	
	@Column(name = "modifiedByEmailId")
	private String modifiedByEmailId;
	
	
}





