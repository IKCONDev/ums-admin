package com.ikn.ums.admin.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_role_menu_item_permission_map_tab")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleMenuItemPermissionMap {
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "user_id", nullable = false)
	private String email;
	
	@Column(name = "role_id")
	private Long roleId;
	
	@Column(name = "menuItemIdList")
	private String menuItemIdList;
	
	@Column(name = "permissionIdList")
	private String permissionIdList;
	
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

}
