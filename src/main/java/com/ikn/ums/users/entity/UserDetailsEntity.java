package com.ikn.ums.users.entity;

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

import com.ikn.ums.users.role.entity.Role;

import lombok.Data;

@Entity
@Table(name = "user_master")
@Data
public class UserDetailsEntity{
	
	@Id
	@Column(name = "user_id", nullable = false, unique = true)
	private String email;
	
	@Column(name = "encrypted_password", nullable = false, unique = false)
	private String encryptedPassword;
	
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(
				name = "user_roles",
				joinColumns = @JoinColumn(name = "user_id"),
				inverseJoinColumns = @JoinColumn(name = "role_id")
			)
	private Set<Role> userRoles = new HashSet<>();
	
	@Column(name = "otp_code", nullable = true, unique = false)
	private int otpCode;
	
	@Column(name = "two_factor_authentication", nullable = true, unique = false)
	private boolean twoFactorAuthentication;

	
}
