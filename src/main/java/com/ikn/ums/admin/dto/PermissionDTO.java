package com.ikn.ums.admin.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

import com.ikn.ums.admin.entity.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PermissionDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long permissionId;
	private String permissionValue;
	private String permissionDescription;
	private LocalDateTime createdDateTime;
	private LocalDateTime modifiedDateTime;
	private String createdBy;
	private String modifiedBy;
	private String createdByEmailId;
	private String modifiedByEmailId;
	private String permissionStatus;

}
