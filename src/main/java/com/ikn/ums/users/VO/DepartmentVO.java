package com.ikn.ums.users.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentVO {

	private Long departmentId;
	private String departmentName;
	private String departmentAddress;
	private String departmentCode;
}
