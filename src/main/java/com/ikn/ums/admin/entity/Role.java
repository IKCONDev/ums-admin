package com.ikn.ums.admin.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import lombok.Data;

@Data
@Entity
@Table (name = "role_tab")
public class Role {

	@Id
	@Column(name = "role_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long roleId;
	
	@Column(name = "roleName", nullable = false , unique = true)
	private String roleName;
	
	@LazyCollection(LazyCollectionOption.FALSE)
	@ManyToMany(cascade = {CascadeType.REFRESH, CascadeType.REMOVE})
	@JoinTable(
				name = "role_menuItems_tab",
				joinColumns = @JoinColumn(name = "roleId", unique = false),
				inverseJoinColumns = @JoinColumn(name = "menuItemId",unique = false)
			)
	private List<MenuItem> menuItems;
	
	@OneToOne(cascade = {CascadeType.REFRESH, CascadeType.REMOVE,CascadeType.MERGE}, fetch = FetchType.EAGER)
	@JoinTable(
				name = "role_permission_tab",
				joinColumns = @JoinColumn(name = "roleId"),
				inverseJoinColumns = @JoinColumn(name = "permissionId")
			)
	private Permission permission;
	
	@Column(name = "createdDateTime")
	private LocalDateTime createdDateTime;
	
	@Column(name = "modifiedDateTime", nullable = true)
	private LocalDateTime modifiedDateTime;
	
	@Column(name = "createdBy")
	private String createdBy;
	
	@Column(name = "modifiedBy")
	private String modifiedBy;
	
	@Column(name = "createdByEmailId")
	private String createdByEmailId;
	
	@Column(name = "modifiedByEmailId", nullable = true)
	private String modifiedByEmailId;
	
	@Column(name = "roleStatus", nullable = true)
	private String roleStatus;
}
