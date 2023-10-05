package com.ikn.ums.admin.model;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfilePicDetailsRequestModel {
	
	private String email;
	//private MultipartFile profilePic;

}
