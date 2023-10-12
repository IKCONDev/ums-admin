package com.ikn.ums.admin.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "organization_tab")
public class Organization {
	
	@Id
	@SequenceGenerator(name = "org_id_gen", initialValue = 1, allocationSize = 1)
	@GeneratedValue(generator = "org_id_gen")
	@Column(name = "orgId")
	private Integer orgId;
	
	@Column(name = "orgName", nullable = false, unique = true)
	private String orgName;
	
	@Column(name = "orgEmailId", nullable = false, unique = true)
	private String orgEmailId;
	
	@Column(name = "orgContact", nullable = false, unique = true)
	private String orgContact;
	
	@Column(name = "orgContactPerson", nullable = false, unique = true)
	private String orgContactPerson;
	
	@Column(name = "orgContactPersonEmail", nullable = false, unique = true)
	private String orgContactPersonEmail;
	
	@Column(name = "orgFunctionalType", nullable = false)
	private String orgFunctionalType;
	
	@Column(name = "orgLogo", nullable = false)
	private byte[] orgLogo;
	
	@Column(name = "orgCountry", nullable = false)
	private String orgCountry;
	
	@Column(name = "orgTimeZone", nullable = false)
	private String orgTimeZone;
	
	@Column(name = "orgAddress", nullable = false)
	private String orgAddress;
	
	@Column(name = "orgSuperAdminEmailId", nullable = false, unique = true)
	private String orgSuperAdminEmailId;
	
	@Column(name = "defaultReplyEmailId", nullable = false, unique = true)
	private String defaultReplyEmailId;
	
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