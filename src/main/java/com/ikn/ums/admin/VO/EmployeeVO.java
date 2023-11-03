package com.ikn.ums.admin.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeVO {

	private Integer id;
	private String employeeOrgId;
	//required for batch processing of teams meetings
	private String teamsUserId;
	private String firstName;
	private String lastName;
	private String email;
	private String reportingManager;
//	private String encryptedPassword;
//	private String userRole;
	//private String department;
	private String designation;
	private DesignationVO empDesignation;
//	private int otpCode;
//	private boolean twoFactorAuthentication;
	private Long departmentId;
	private String gender;
	private DepartmentVO department;
}
