package com.ikn.ums.admin.exception;

import java.util.NoSuchElementException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {


	/**
	 * EntityNotFoundException handles the exception when the Object is NUll
	 * @param emptyInputException
	 * @return
	 */
	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException entityNotFoundException) {
		log.info("GlobalExceptionHandler.handleEntityNotFoundException() ENTERED" );
		log.info("EntityNotFoundException Occurred ::::::::::::: " + entityNotFoundException.getErrorCode());
		log.info("EntityNotFoundException Occurred ::::::::::::: " + entityNotFoundException.getErrorMessage());
		return new ResponseEntity<>("Entity Object is NUll.", HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * The EmptyInputException is a Custom Exception (created by us) written for
	 * handling the Business Scenarios. defined
	 * @param emptyInputException
	 * @return
	 */
	@ExceptionHandler(EmptyInputException.class)
	public ResponseEntity<String> handleEmptyInput(EmptyInputException emptyInputException) {
		log.error("getMenuItemByName() : An error occurred: {}." + emptyInputException.getMessage(), emptyInputException);
		log.info("GlobalExceptionHandler.handleEmptyInput() ENTERED" + emptyInputException.getMessage());
		return new ResponseEntity<>("Input field is empty. Please look into it.", HttpStatus.BAD_REQUEST);
	}

	/**
	 * EntityNotFoundException handles the exception when the Object is NUll
	 * @param emptyInputException
	 * @return
	 */
	@ExceptionHandler(EmptyOTPException.class)
	public ResponseEntity<String> handleEntityNotFoundException(EmptyOTPException emptyOTPException) {
		log.info("GlobalExceptionHandler.handleEntityNotFoundException() ENTERED" + emptyOTPException.getMessage());
		return new ResponseEntity<>("OTP is not generated for the User.", HttpStatus.BAD_REQUEST);
	}

	/**
	 * The NoSuchElementException is a Pre-defined default handler for the
	 * SpringBoot. No class required to be created for pre-defined.
	 * 
	 * @param noSuchElementException
	 * @return
	 */
	@ExceptionHandler(NoSuchElementException.class)
	public ResponseEntity<String> handleNoSuchElementException(NoSuchElementException noSuchElementException) {
		log.info("GlobalExceptionHandler.handleNoSuchElementException() ENTERED" + noSuchElementException.getMessage());
		return new ResponseEntity<>("No Value is Present in DB.", HttpStatus.NOT_FOUND);
	}

	/**
	 * Handling the Business Exceptions global to reduce boiler plate code
	 * @param noSuchElementException
	 * @return
	 */
	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<String> handleBusinessException(BusinessException businessException) {
		log.info("GlobalExceptionHandler.handleBusinessException() ENTERED");
		log.info("Business Exception Occurred." + businessException.getMessage());
		return new ResponseEntity<>("Business Exception.", HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	
	@ExceptionHandler(ImageNotFoundException.class)
	public ResponseEntity<String> handleImageNotFound(ImageNotFoundException imageNotFoundException) {
		log.info("GlobalExceptionHandler.handleImageNotFoundException() ENTERED");
		log.info("Not a Valid Image Exception Occurred." + imageNotFoundException.getMessage());
		return new ResponseEntity<>("Not a Image Exception", HttpStatus.NO_CONTENT);
	}


	/**
	 * Handling the Controller Exceptions global to reduce boiler plate code
	 * @param noSuchElementException
	 * @return
	 */
	@ExceptionHandler(ControllerException.class)
	public ResponseEntity<String> handleControllerException(ControllerException controllerException) {
		log.info("GlobalExceptionHandler.handleControllerException() ENTERED");
		log.info("Controller Exception Occurred ::::::::::::: " + controllerException.getErrorCode());
		log.info("Controller Exception Occurred ::::::::::::: " + controllerException.getErrorMessage());
		return new ResponseEntity<>("Controller Exception.", HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		log.info("GlobalExceptionHandler.handleHttpRequestMethodNotSupported() ENTERED " + ex.getMessage());
		return new ResponseEntity<>("Please change your http method type.", HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(UserInactiveException.class)
	public ResponseEntity<Object> handleUserInactiveException(UserInactiveException uiae) {
		log.info("GlobalExceptionHandler.handleUserInactiveException() ENTERED : " + uiae.getMessage());
		return new ResponseEntity<>("Please change your http method type.", HttpStatus.FORBIDDEN);
	}

	/**
	 * RoleNameExistsException handles the exception when the Role Name is exists
	 * @param roleNameExistsException
	 * @return
	 */
	@ExceptionHandler(RoleNameExistsException.class)
	public ResponseEntity<String> handleRoleNameExistsException(RoleNameExistsException roleNameExistsException) {
		log.info("GlobalExceptionHandler.handleRoleNameExistsException() ENTERED" + roleNameExistsException.getMessage());
		return new ResponseEntity<>("Role Name Already Exists.", HttpStatus.FOUND);
	}
	
	/**
	 * PermissionNameExistsException handles the exception when the Role Name is exists
	 * @param permissionNameExistsException
	 * @return
	 */
	@ExceptionHandler(PermissionNameExistsException.class)
	public ResponseEntity<String> handlePermissionNameExistsException(PermissionNameExistsException permissionNameExistsException) {
		log.info("GlobalExceptionHandler.handlePermissionNameExistsException() ENTERED" + permissionNameExistsException.getMessage());
		return new ResponseEntity<>("Permission Name Already Exists.", HttpStatus.FOUND);
	}

	/**
	 * PermissionInUsageException handles the exception when the Role Name is exists
	 * @param permissionInUsageException
	 * @return
	 */
	@ExceptionHandler(PermissionInUsageException.class)
	public ResponseEntity<String> handlePermissionInUsageException(PermissionInUsageException permissionInUsageException) {
		log.info("GlobalExceptionHandler.handlePermissionInUsageException() ENTERED" + permissionInUsageException.getMessage());
		return new ResponseEntity<>("Permissions Are Assigned to Role and are in Usage.", HttpStatus.IM_USED);
	}
	
	/**
	 * PermissionInUsageException handles the exception when the Role Name is exists
	 * @param permissionInUsageException
	 * @return
	 */
	@ExceptionHandler(RoleInUsageException.class)
	public ResponseEntity<String> handleRoleInUsageException(RoleInUsageException roleInUsageException) {
		log.info("GlobalExceptionHandler.handleRoleNameExistsException() ENTERED" + roleInUsageException.getMessage());
		return new ResponseEntity<>("Permissions Are Assigned to Role and are in Usage.", HttpStatus.IM_USED);
	}
	
	/**
	 * PermissionInUsageException handles the exception when the Role Name is exists
	 * @param permissionInUsageException
	 * @return
	 */
	@ExceptionHandler(MenuItemInUsageException.class)
	public ResponseEntity<String> handleMenuItemInUsageException(MenuItemInUsageException menuItemInUsageException) {
		log.info("GlobalExceptionHandler.handleMenuItemInUsageException() ENTERED" + menuItemInUsageException.getMessage());
		return new ResponseEntity<>("Menu Items Are Assigned to Role and are in Usage.", HttpStatus.IM_USED);
	}
	
	/**
	 * PermissionNameExistsException handles the exception when the Role Name is exists
	 * @param permissionNameExistsException
	 * @return
	 */
	@ExceptionHandler(MenuItemNameExistsException.class)
	public ResponseEntity<String> handleMenuItemNameExistsException(MenuItemNameExistsException menuItemNameExistsException) {
		log.info("GlobalExceptionHandler.handleMenuItemNameExistsException() ENTERED" + menuItemNameExistsException.getMessage());
		return new ResponseEntity<>("Menu item Name Already Exists.", HttpStatus.FOUND);
	}
	
	@ExceptionHandler(LoginAttemptsExceededException.class)
	public ResponseEntity<String> handleLoginAttemptsExceededException(LoginAttemptsExceededException LoginAttemptsExceededException) {
		log.info("GlobalExceptionHandler.handleLoginAttemptsExceededException() ENTERED" + LoginAttemptsExceededException.getMessage());
		return new ResponseEntity<>("Login attempts of the user exceeded more than 3.", HttpStatus.LOCKED);
	}

	
}
