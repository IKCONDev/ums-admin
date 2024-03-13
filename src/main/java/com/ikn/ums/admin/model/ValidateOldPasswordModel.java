package com.ikn.ums.admin.model;

import lombok.Data;

@Data
public class ValidateOldPasswordModel {
	private String email;
	private String oldPassword;
}
