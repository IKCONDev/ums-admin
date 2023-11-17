package com.ikn.ums.admin.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table (name = "permission_tab")
public class Permission {

	@Id
	@Column(name = "permission_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long permissionId;
	
	@Column(name = "permissionValue", nullable = false , unique = true)
	private String permissionValue;
	
	@Column(name = "permissionDescription")
	private String permissionDescription;
	
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
	
	@Column(name = "permissionStatus", nullable = false)
	private String permissionStatus;
}
