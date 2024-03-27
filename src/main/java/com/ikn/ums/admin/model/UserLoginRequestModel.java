package com.ikn.ums.admin.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginRequestModel {

	private String email;
	private String password;
	private String loginCity;
	private String loginState;
	private String loginCountry;

}
