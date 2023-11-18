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
@Table (name = "menu_item_tab")
public class MenuItem {
	
	@Id
	@Column(name = "menuItem_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long menuItemId;
	
	@Column(name = "menuItemName", nullable = false , unique = true)
	private String menuItemName;

	@Column(name = "menuItemPath")
	private String menuItemPath;
	
	@Column(name = "menuItemDescription")
	private String menuItemDescription;
	
	@Column(name = "menuItemStatus", nullable = false)
	private String menuItemStatus;
	
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
