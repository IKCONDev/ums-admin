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
	
	//org details
	@Column(name = "orgName", nullable = true, unique = true)
	private String orgName;
	
	@Column(name = "orgContactNumber", nullable = true, unique = true)
	private String orgContactNumber;
	
	@Column(name = "orgEmailId", nullable = true, unique = true)
	private String orgEmailId;
	
	//org contact person details
	@Column(name = "orgContactPersonName", nullable = true)
	private String orgContactPersonName;
	
	@Column(name = "orgContactPersonNumber", nullable = true, unique = true)
	private String orgContactPersonNumber;
	
	@Column(name = "orgContactPersonEmail", nullable = true, unique = true)
	private String orgContactPersonEmail;
	
	//
	@Column(name = "orgFunctionalType", nullable = true)
	private String orgFunctionalType;
	
	@Column(name = "orgLogo", nullable = true)
	private byte[] orgLogo;
	
	@Column(name = "orgWebsite", nullable = true)
	private String orgWebsite;
	
	@Column(name = "orgCountry", nullable = true)
	private String orgCountry;
	
	@Column(name = "orgTimeZone", nullable = true)
	private String orgTimeZone;
	
	@Column(name = "orgAddress", nullable = true)
	private String orgAddress;
	
	@Column(name = "orgSuperAdminEmailId", nullable = true, unique = true)
	private String orgSuperAdminEmailId;
	
	@Column(name = "defaultReplyEmailId", nullable = true, unique = true)
	private String defaultReplyEmailId;
	
	
	//other properties
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