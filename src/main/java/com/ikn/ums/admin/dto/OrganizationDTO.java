package com.ikn.ums.admin.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationDTO {
	
	private Integer orgId;
	private String orgName;
	private String orgContactNumber;
	private String orgEmailId;
	private String orgContactPersonName;
	private String orgContactPersonNumber;
	private String orgContactPersonEmail;
	private String orgFunctionalType;
	private byte[] orgLogo;
	private String orgWebsite;
	private String orgCountry;
	private String orgTimeZone;
	private String orgAddress;
	private String orgSuperAdminEmailId;
	private String defaultReplyEmailId;
	private LocalDateTime createdDateTime;
	private LocalDateTime modifiedDateTime;
	private String createdBy;
	private String modifiedBy;
	private String createdByEmailId;
	private String modifiedByEmailId;
	private byte[] organizationImage;

}