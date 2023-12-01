package com.ikn.ums.admin.exception;

public class ErrorCodeMessages {
	
	//USER MODULE ERROR CODES

	 public static final String ERR_ADMIN_SERVICE_NOT_FOUND_CODE = "ADMIN-CORE-SERVICE-1001";
	 public static final String ERR_ADMIN_SERVICE_NOT_FOUND_MSG = "Admin Service not present.";
	 
	 public static final String ERR_USER_ENTITY_IS_NULL_CODE = "ADMIN-ENTITY-IS-NULL-1002";
	 public static final String ERR_USER_ENTITY_IS_NULL_MSG = "Admin Entity is Null."; 

	 public static final String ERR_USER_ID_NOT_FOUND_CODE = "USER-ID-NOT-FOUND-1003";
	 public static final String ERR_USER_ID_NOT_FOUND_MSG = "Requested User Id is not present."; 

	 public static final String ERR_USER_ID_ALREADY_EXISTS_CODE = "USER-ID-ALREADY-EXISTS-1006";
	 public static final String ERR_USER_ID_ALREADY_EXISTS_MSG = "User Id Already Exists."; 

	 public static final String ERR_USER_FIRST_NAME_IS_NULL_CODE = "USER_FIRST_NAME_IS_NULL-1007";
	 public static final String ERR_USER_FIRST_NAME_IS_NULL_MSG = "User First Name Is Null."; 

	 public static final String ERR_USER_LAST_NAME_IS_NULL_CODE = "USER_LAST_NAME_IS_NULL-1008";
	 public static final String ERR_USER_LAST_NAME_IS_NULL_MSG = "User Last Name Is Null."; 
	 
	 public static final String USER_CREATE_SUCCESS_CODE = "USER-CREATE_SUCCESS-1009";
	 public static final String USER_CREATE_SUCCESS_MSG = "User Creation Sucessful!"; 	 
	 
	 public static final String ERR_USER_LIST_IS_USERTY_CODE = "USER_LIST_IS_USERTY-1010";
	 public static final String ERR_USER_LIST_IS_USERTY_MSG = "user List Is Empty."; 	 

	 public static final String ERR_USER_SERVICE_EXCEPTION_CODE = "USER_SERVICE_EXCEPTION_CODE-1011";
	 public static final String ERR_USER_SERVICE_EXCEPTION_MSG = "User Occured in the Employee Service Layer."; 	

	 public static final String ERR_USER_EMAIL_ID_NOT_FOUND_CODE = "USER_EMAIL_ID_NOT_FOUND_CODE-1012";
	 public static final String ERR_USER_EMAIL_ID_NOT_FOUND_MSG = "User Email Id is not found."; 	

	 public static final String ERR_USER_LIST_IS_EMPTY_CODE = "USER_LIST_IS_EMPTY-1013";
	 public static final String ERR_USER_LIST_IS_EMPTY_MSG = "User List Is Empty."; 	 

	 public static final String ERR_USER_OTP_NOT_GENERATED_CODE = "USER_OTP_NOT_GENERATED-1014";
	 public static final String ERR_USER_OTP_NOT_GENERATED_MSG = "OTP is Not Generated For User."; 
	 
	 public static final String ERR_USER_CONTROLLER_EXCEPTION_CODE = "USER_CONTROLLER_EXCEPTION-1015";
	 public static final String ERR_USER_CONTROLLER_EXCEPTION_MSG = "User Controller Exception Occurred."; 
	 
	 public static final String ERR_USER_CREATE_UNSUCCESS_CODE = "USER_CREATE_UNSUCCESS_CODE-1016";
	 public static final String ERR_USER_CREATE_UNSUCCESS_MSG = "Exception occured while creating user";
	 
	 public static final String USER_DELETE_SUCCESS_CODE = "USER_DELETE_SUCCESS_CODE-1017";
	 public static final String USER_DELETE_SUCCESS_MSG = "User Deletion sucessfull!";
	 
	 public static final String USER_UPDATE_SUCCESS_CODE = "USER_UPDATE_SUCCESS_CODE-1018";
	 public static final String USER_UPDATE_SUCCESS_MSG = "User Updation sucessfull!";

	 public static final String ERR_USER_DELETE_UNSUCCESS_CODE = "USER_DELETE_UNSUCCESS_CODE-1019";
	 public static final String ERR_USER_DELETE_UNSUCCESS_MSG = "Exception occured while deleting user";
	 
	 public static final String ERR_USER_UPDATE_UNSUCCESS_CODE = "USER_UPDATE_UNSUCCESS_CODE-1020";
	 public static final String ERR_USER_UPDATE_UNSUCCESS_MSG = "Exception occured while updating user";
	 
	 public static final String ERR_USER_DELETE_PROFILEPIC_UNSUCCESS_CODE = "USER_DELETE_UNSUCCESS_CODE-1021";
	 public static final String ERR_USER_DELETE_PROFILEPIC_UNSUCCESS_MSG = "Exception occured while deleting user profile pic";
 
//ROLE MODULE ERROR CODES

	 public static final String ERR_ROLE_ID_ALREADY_EXISTS_CODE = "ROLE-ID-ALREADY-EXISTS-1001";
	 public static final String ERR_ROLE_ID_ALREADY_EXISTS_MSG = "Role Id Already Exists."; 

	 public static final String ERR_ROLE_NAME_IS_NULL_CODE = "ROLE NAME_IS_NULL-1002";
	 public static final String ERR_ROLE_NAME_IS_NULL_MSG = "Role Name is Null."; 
	 
	 public static final String ROLE_SAVE_SUCCESS_CODE = "ROLE-SAVE-SUCCESS-1003";
	 public static final String ROLE_SAVE_SUCCESS_MSG = "Role Save Sucessfull."; 	 
	 
	 public static final String ERR_ROLE_LIST_IS_EMPTY_CODE = "ROLE_LIST_IS_EMPTY-1004";
	 public static final String ERR_ROLE_LIST_IS_EMPTY_MSG = "Role List Is Empty."; 	 

	 public static final String ERR_ROLE_ID_IS_EMPTY_CODE = "ROLE_ID_IS_EMPTY-1005";
	 public static final String ERR_ROLE_ID_IS_EMPTY_MSG = "Role Id Is Empty."; 	 

	 public static final String ERR_ROLE_NAME_IS_EMPTY_CODE = "ROLE_NAME_IS_EMPTY-1006";
	 public static final String ERR_ROLE_NAME_IS_EMPTY_MSG = "Role Name Is Empty."; 	
	 
	 public static final String ERR_ROLE_ENTITY_IS_NULL_CODE = "ROLE-ENTITY-IS-NULL-1007";
	 public static final String ERR_ROLE_ENTITY_IS_NULL_MSG = "Role Entity is Null."; 
	 
	 public static final String ERR_ROLE_SERVICE_EXCEPTION_CODE = "ROLE_SERVICE_EXCEPTION_CODE-1007";
	 public static final String ERR_ROLE_SERVICE_EXCEPTION_MSG = "Exception Occured in the Role Service Layer."; 
	 
	 public static final String ERR_ROLE_CREATE_UNSUCCESS_CODE = "ROLE_CREATE_UNSUCCESS_CODE-1009";
	 public static final String ERR_ROLE_CREATE_UNSUCCESS_MSG = "Exception occured while creating role";
	 
	 public static final String ERR_ROLE_DELETE_UNSUCCESS_CODE = "ROLE_DELETE_UNSUCCESS_CODE-1010";
	 public static final String ERR_ROLE_DELETE_UNSUCCESS_MSG = "Exception occured while deleting role";
	 
	 public static final String ERR_ROLE_UPDATE_UNSUCCESS_CODE = "ROLE_UPDATE_UNSUCCESS_CODE-1011";
	 public static final String ERR_ROLE_UPDATE_UNSUCCESS_MSG = "Exception occured while updating role";

	 public static final String ERR_ROLE_NAME_EXISTS_CODE = "ROLE_NAME_EXISTS_CODE-1011";
	 public static final String ERR_ROLE_NAME_EXISTS_MSG = "Role Name Already Exists";
	 
	 public static final String ERR_ROLE_GET_UNSUCCESS_CODE = "ERR_ROLE_GET_UNSUCCESS_CODE-1012";
	 public static final String ERR_ROLE_GET_UNSUCCESS_MSG = "Error while getting user role(s)";
	 
	 //ORGANIZATION MODULE ERROR CODES
	 
	 public static final String ERR_ORG_ENTITY_IS_NULL_CODE = "ORG-ENTITY-IS-NULL-1027";
	 public static final String ERR_ORG_ENTITY_IS_NULL_MSG = "Organization Entity is Null."; 

	 public static final String ERR_ORG_ID_NOT_FOUND_CODE = "ORG-ID-NOT-FOUND-1028";
	 public static final String ERR_ORG_ID_NOT_FOUND_MSG = "Requested Organization Id is not present."; 

	 public static final String ERR_ORG_ID_ALREADY_EXISTS_CODE = "ORG-ID-ALREADY-EXISTS-1029";
	 public static final String ERR_ORG_ID_ALREADY_EXISTS_MSG = "Organization Id Already Exists."; 

	 public static final String ERR_ORG_FIRST_NAME_IS_NULL_CODE = "ORG_FIRST_NAME_IS_NULL-1030";
	 public static final String ERR_ORG_FIRST_NAME_IS_NULL_MSG = "Organization First Name Is Null."; 

	 public static final String ERR_ORG_LAST_NAME_IS_NULL_CODE = "ORG_LAST_NAME_IS_NULL-1031";
	 public static final String ERR_ORG_LAST_NAME_IS_NULL_MSG = "Organization Last Name Is Null."; 
	 
	 public static final String ORG_SAVE_UNSUCCESS_CODE = "Organization-SAVE-UNSUCCESS-1032";
	 public static final String ORG_SAVE_UNSUCCESS_MSG = "Exception occured while saving Organization."; 	 
	 
	 public static final String ERR_ORG_LIST_IS_ENTITY_CODE = "ORG_LIST_IS_ENTITY-1033";
	 public static final String ERR_ORG_LIST_IS_ENTITY_MSG = "Organization List Is Empty."; 	 

	 public static final String ERR_ORG_SERVICE_EXCEPTION_CODE = "ORG_SERVICE_EXCEPTION_CODE-1034";
	 public static final String ERR_ORG_SERVICE_EXCEPTION_MSG = "Organization Occured in the Employee Service Layer."; 	

	 public static final String ERR_ORG_EMAIL_ID_NOT_FOUND_CODE = "ORG_EMAIL_ID_NOT_FOUND_CODE-1035";
	 public static final String ERR_ORG_EMAIL_ID_NOT_FOUND_MSG = "Organization Email Id is not found."; 	

	 public static final String ERR_ORG_LIST_IS_EMPTY_CODE = "ORG_LIST_IS_EMPTY-1036";
	 public static final String ERR_ORG_LIST_IS_EMPTY_MSG = "Organization List Is Empty."; 	 

	 public static final String ERR_ORG_ID_IS_EMPTY_CODE = "ORG-ID-IS-EMPTY-1037";
	 public static final String ERR_ORG_ID_IS_EMPTY_MSG = "Requested Organization Id is not present."; 

	 public static final String ERR_ORG_CONTROLLER_EXCEPTION_CODE = "ORG_CONTROLLER_EXCEPTION-1038";
	 public static final String ERR_ORG_CONTROLLER_EXCEPTION_MSG = "Organization Controller Exception Occurred."; 
	 
	 public static final String ERR_ORG_UPDATE_UNSUCCESS_CODE = "ORG_UPDATE_UNSUCCESS_CODE-1039";
	 public static final String ERR_ORG_UPDATE_UNSUCCESS_MSG = "Exception Occured while Updating Organization."; 
	 
	 public static final String ERR_ORG_DELETE_UNSUCCESS_CODE = "ORG_DELETE_UNSUCCESS_CODE-1040";
	 public static final String ERR_ORG_DELETE_UNSUCCESS_MSG = "Exception Occured while deleting Organization."; 
	 
	 public static final String ERR_ORG_GET_UNSUCCESS_CODE = "ERR_ORG_GET_UNSUCCESS_CODE-1041";
	 public static final String ERR_ORG_GET_UNSUCCESS_MSG = "Exception Occured while getting Organization details."; 
	 
	 public static final String ERR_USER_IMAGE_NOT_VALID_CODE = "IMAGE-IS-NOT-VALID-1042";
	 public static final String ERR_USER_IMAGE_NOT_VALID_MSG = "THIS IS NOT A VALID IMAGE FILE";
	 
	 public static final String ERR_USER_NOT_FOUND_CODE = "ERR_USER_NOT_FOUND_CODE-1043";
	 public static final String ERR_USER_NOT_FOUND_MSG = "User does not exists to Login";
	 
	 public static final String ERR_USER_INACTIVE_CODE = "ERR_USER_INACTIVE_CODE-1044";
	 public static final String ERR_USER_INACTIVE_MSG = "User is inactive - Cannot Login";
	 
	 public static final String ERR_USER_LOGIN_ATTEMPTS_EXCEEDED_CODE = "ERR_USER_LOGIN_ATTEMPTS_CODE-1045";
	 public static final String ERR_USER_LOGIN_ATTEMPTS_EXCEEDED_MSG = "Login attempts exceeded moew than 3";
	 
	 //ORGANIZATION MODULE ERROR CODES
	 
	//PERMISSION MODULE ERROR CODES
		 public static final String ERR_PERMISSION_ID_ALREADY_EXISTS_CODE = "PERMISSION_ID_ALREADY_EXISTS-1046";
		 public static final String ERR_PERMISSION_ID_ALREADY_EXISTS_MSG = "Permission Id Already Exists."; 

		 public static final String ERR_PERMISSION_VALUE_IS_NULL_CODE = "PERMISSION_VALUE_IS_NULL-1047";
		 public static final String ERR_PERMISSION_VALUE_IS_NULL_MSG = "Permission Value is Null."; 
		 
		 public static final String PERMISSION_SAVE_SUCCESS_CODE = "PERMISSION_CREATE_SUCCESS_CODE-1048";
		 public static final String PERMISSION_SAVE_SUCCESS_MSG = "Permission created Sucessfully."; 	 
		 
		 public static final String ERR_PERMISSION_LIST_IS_EMPTY_CODE = "PERMISSION_LIST_IS_EMPTY-1049";
		 public static final String ERR_PERMISSION_LIST_IS_EMPTY_MSG = "Permission List Is Empty."; 	 

		 public static final String ERR_PERMISSION_VALUE_IS_EMPTY_CODE = "PERMISSION_VALUE_IS_EMPTY-1050";
		 public static final String ERR_PERMISSION_VALUE_IS_EMPTY_MSG = "Permission Value Is Empty."; 	 

		 public static final String ERR_PERMISSION_ENTITY_IS_NULL_CODE = "PERMISSION_ENTITY_IS_NULL-1051";
		 public static final String ERR_PERMISSION_ENTITY_IS_NULL_MSG = "Permission Entity is Null."; 
		 
		 public static final String ERR_PERMISSION_CREATE_UNSUCCESS_CODE = "PERMISSION_CREATE_UNSUCCESS_CODE-1052";
		 public static final String ERR_PERMISSION_CREATE_UNSUCCESS_MSG = "Exception occured while creating Permission";
		 
		 public static final String ERR_PERMISSION_DELETE_UNSUCCESS_CODE = "PERMISSION_DELETE_UNSUCCESS_CODE-1053";
		 public static final String ERR_PERMISSION_DELETE_UNSUCCESS_MSG = "Exception occured while deleting Permission";
		 
		 public static final String ERR_PERMISSION_UPDATE_UNSUCCESS_CODE = "PERMISSION_UPDATE_UNSUCCESS_CODE-1054";
		 public static final String ERR_PERMISSION_UPDATE_UNSUCCESS_MSG = "Exception occured while updating Permission";

		 public static final String ERR_PERMISSION_VALUE_EXISTS_CODE = "PERMISSION_VALUE_EXISTS_CODE-1055";
		 public static final String ERR_PERMISSION_VALUE_EXISTS_MSG = "Permission Value Already Exists";
		 
		 public static final String ERR_PERMISSION_GET_UNSUCCESS_CODE = "ERR_PERMISSION_GET_UNSUCCESS_CODE-1056";
		 public static final String ERR_PERMISSION_GET_UNSUCCESS_MSG = "Error while getting user Permission(s)";

		 public static final String ERR_PERMISSION_ID_IS_EMPTY_CODE = "ERR_PERMISSION_ID_IS_EMPTY_CODE-1057";
		 public static final String ERR_PERMISSION_ID_IS_EMPTY_MSG = "Permission Id is Empty !";

		 public static final String ERR_PERMISSION_IS_IN_USAGE_CODE = "ERR_PERMISSION_IS_IN_USAGE_CODE-1058";
		 public static final String ERR_PERMISSION_IS_IN_USAGE_MSG = "Permission is in Usage !";
		 
		 //PERMISSION MODULE ERROR CODES 
		 
		//MENU ITEM MODULE ERROR CODES
		 
		 public static final String ERR_MENU_ITEM_ID_ALREADY_EXISTS_CODE = "MENU_ITEM_ID_ALREADY_EXISTS-1059";
		 public static final String ERR_MENU_ITEM_ID_ALREADY_EXISTS_MSG = "Menu Item Id Already Exists."; 

		 public static final String ERR_MENU_ITEM_NAME_IS_NULL_CODE = "MENU_ITEM_VALUE_IS_NULL-1060";
		 public static final String ERR_MENU_ITEM_NAME_IS_NULL_MSG = "Menu Item Value is Null."; 
		 
		 public static final String MENU_ITEM_CREATE_SUCCESS_CODE = "MENU_ITEM_CREATE_SUCCESS_CODE-1061";
		 public static final String MENU_ITEM_CREATE_SUCCESS_MSG = "Menu Item created Sucessfully."; 	 
		 
		 public static final String ERR_MENU_ITEM_LIST_IS_EMPTY_CODE = "MENU_ITEM_LIST_IS_EMPTY-1062";
		 public static final String ERR_MENU_ITEM_LIST_IS_EMPTY_MSG = "Menu Item List Is Empty."; 	 

		 public static final String ERR_MENU_ITEM_NAME_IS_EMPTY_CODE = "MENU_ITEM_NAME_IS_EMPTY-1063";
		 public static final String ERR_MENU_ITEM_NAME_IS_EMPTY_MSG = "Menu Item Name Is Empty."; 	 

		 public static final String ERR_MENU_ITEM_ENTITY_IS_NULL_CODE = "MENU_ITEM_ENTITY_IS_NULL-1064";
		 public static final String ERR_MENU_ITEM_ENTITY_IS_NULL_MSG = "Menu Item Entity is Null."; 
		 
		 public static final String ERR_MENU_ITEM_CREATE_UNSUCCESS_CODE = "MENU_ITEM_CREATE_UNSUCCESS_CODE-1065";
		 public static final String ERR_MENU_ITEM_CREATE_UNSUCCESS_MSG = "Exception occured while creating Menu Item";
		 
		 public static final String ERR_MENU_ITEM_DELETE_UNSUCCESS_CODE = "MENU_ITEM_DELETE_UNSUCCESS_CODE-1066";
		 public static final String ERR_MENU_ITEM_DELETE_UNSUCCESS_MSG = "Exception occured while deleting Menu Item";
		 
		 public static final String ERR_MENU_ITEM_UPDATE_UNSUCCESS_CODE = "MENU_ITEM_UPDATE_UNSUCCESS_CODE-1067";
		 public static final String ERR_MENU_ITEM_UPDATE_UNSUCCESS_MSG = "Exception occured while updating Menu Item";

		 public static final String ERR_MENU_ITEM_NAME_EXISTS_CODE = "MENU_ITEM_Name_EXISTS_CODE-1068";
		 public static final String ERR_MENU_ITEM_NAME_EXISTS_MSG = "Menu Item Name Already Exists";
		 
		 public static final String ERR_MENU_ITEM_GET_UNSUCCESS_CODE = "ERR_MENU_ITEM_GET_UNSUCCESS_CODE-1069";
		 public static final String ERR_MENU_ITEM_GET_UNSUCCESS_MSG = "Error while getting user Menu Items(s)";

		 public static final String ERR_MENU_ITEM_ID_IS_EMPTY_CODE = "ERR_MENU_ITEM_ID_IS_EMPTY_CODE-1070";
		 public static final String ERR_MENU_ITEM_ID_IS_EMPTY_MSG = "Menu Item Id is Empty !";

		 public static final String ERR_MENU_ITEM_IS_IN_USAGE_CODE = "ERR_MENU_ITEM_IS_IN_USAGE_CODE-1071";
		 public static final String ERR_MENU_ITEM_IS_IN_USAGE_MSG = "Menu Item is in Usage !";
		 
		 public static final String ERR_USER_IS_EMPTY_CODE = "ERR_USER_IS_NOT_PRESENT_CODE-1072";
		 public static final String ERR_USER_IS_EMPTY_MSG = "User is not found to delete Profile Pic !";
		 
		 //Error codes for UserRoleMenuItemPermissionMap
		 public static final String ERR_USER_RPM_IS_NULL_CODE = "ERR_USER_RPM_DTO_IS_NULL_CODE-1001";
		 public static final String ERR_USER_RPM_IS_NULL_MSG = "User role menu item object is null / empty !";
		 
}
