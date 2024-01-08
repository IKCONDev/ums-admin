package com.ikn.ums.admin.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePasswordRequestModel {
	
	private String newPassword;
	private String confirmPassword;
	private String email;

}
