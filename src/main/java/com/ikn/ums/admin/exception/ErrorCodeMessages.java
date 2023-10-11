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
	 
	 
	 //ORGANIZATION MODULE ERROR CODES
}
