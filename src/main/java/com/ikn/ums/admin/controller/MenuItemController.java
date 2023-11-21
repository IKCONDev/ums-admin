package com.ikn.ums.admin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ikn.ums.admin.dto.MenuItemDTO;
import com.ikn.ums.admin.exception.ControllerException;
import com.ikn.ums.admin.exception.EmptyInputException;
import com.ikn.ums.admin.exception.EmptyListException;
import com.ikn.ums.admin.exception.EntityNotFoundException;
import com.ikn.ums.admin.exception.ErrorCodeMessages;
import com.ikn.ums.admin.exception.MenuItemNameExistsException;
import com.ikn.ums.admin.service.MenuItemService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/menuitem")
public class MenuItemController {

	@Autowired
	private MenuItemService menuItemService;

	@PostMapping("/create")
	public ResponseEntity<MenuItemDTO> createMenuItem(@RequestBody MenuItemDTO menuItemDTO) {
		
		log.info("MenuItemController.createMenuItem() entered ");
		if (menuItemDTO == null || menuItemDTO.equals(null)) {
			log.info("Permission Entity Not Found Exception has encountered while creating MenuItem.");
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_MENU_ITEM_ENTITY_IS_NULL_CODE,
					ErrorCodeMessages.ERR_MENU_ITEM_ENTITY_IS_NULL_MSG);
		}
		try {
			log.info("MenuItemController.createMenuItem() is under execution.");
			log.info(":MenuItem Object : " + menuItemDTO );
			MenuItemDTO createdMenuItem = menuItemService.createMenuItem(menuItemDTO);
			log.info("MenuItemController.createMenuItem() executed successfully.");
			return new ResponseEntity<>(createdMenuItem, HttpStatus.CREATED);
		} catch (EntityNotFoundException | MenuItemNameExistsException menuBusinessException) {
			log.info("MenuItem Business Exception has encountered while creating MenuItem. " + menuBusinessException.getMessage());
			throw menuBusinessException;
		} catch (Exception e) {
			log.info("General Exception has encountered while creating MenuItem. " + e.getMessage());
			ControllerException umsCE = new ControllerException(ErrorCodeMessages.ERR_MENU_ITEM_CREATE_UNSUCCESS_CODE,
					ErrorCodeMessages.ERR_MENU_ITEM_CREATE_UNSUCCESS_MSG);
			throw umsCE;
		}
	}

	@PutMapping("/update")
	public ResponseEntity<MenuItemDTO> updateMenuItem(@RequestBody MenuItemDTO menuItemDTO) {
		log.info("MenuItemController.updateMenuItem() entered with args");
		if (menuItemDTO == null || menuItemDTO.equals(null)) {
			log.info("MenuItem Entity Not Found Exception has encountered while updating Role.");
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_PERMISSION_ENTITY_IS_NULL_CODE,
					ErrorCodeMessages.ERR_PERMISSION_ENTITY_IS_NULL_MSG);
		}
		try {
			log.info("MenuItemController.updateMenuItem() is under execution.");
			MenuItemDTO updatedMenuItemDTO = menuItemService.updateMenuItem(menuItemDTO);
			log.info("MenuItemController.updateMenuItem() is executed sucessfully.");
			return new ResponseEntity<>(updatedMenuItemDTO, HttpStatus.PARTIAL_CONTENT);
		}catch (EntityNotFoundException | MenuItemNameExistsException| EmptyInputException  menuItemBusinessException) {
			log.info("MenuItem Business Exception has encountered while updating MenuItem. " + menuItemBusinessException.getMessage());
			throw menuItemBusinessException;
		}catch (Exception e) {
			log.info("General Exception has encountered while updating MenuItem. " + e.getMessage());
			ControllerException umsCE = new ControllerException(ErrorCodeMessages.ERR_MENU_ITEM_UPDATE_UNSUCCESS_CODE, 
					ErrorCodeMessages.ERR_MENU_ITEM_CREATE_UNSUCCESS_MSG);
			throw umsCE;
		}
	}

	@DeleteMapping("/delete/{ids}")
	public ResponseEntity<?> deleteSelectedMenuItems(@PathVariable("ids") List<Long> menuItemIds) {
		boolean isMenuItemDeleted = false;
		log.info("MenuItemController.deleteSelectedMenuItems() entered ");
		if (menuItemIds == null || menuItemIds.size() <= 0 ) {
			log.info("MenuItemController.deleteSelectedMenuItems() EmptyInputException : menuItem Id/Ids are empty");
			throw new EmptyInputException(ErrorCodeMessages.ERR_PERMISSION_ID_IS_EMPTY_CODE,
					ErrorCodeMessages.ERR_PERMISSION_ID_IS_EMPTY_MSG);
		}
		try {
			log.info("MenuItemController.deleteSelectedMenuItems() is under execution...");
			
			menuItemService.deleteSelectedMenuItemByIds(menuItemIds);
			isMenuItemDeleted = true;
			log.info("MenuItemController.deleteSelectedMenuItems() executed successfully");
			return new ResponseEntity<>(isMenuItemDeleted, HttpStatus.OK);
		}catch (EmptyListException businessException) {
			throw businessException;
		} catch (Exception e) {
			ControllerException umsCE = new ControllerException(ErrorCodeMessages.ERR_PERMISSION_DELETE_UNSUCCESS_CODE,
					ErrorCodeMessages.ERR_PERMISSION_DELETE_UNSUCCESS_MSG);
			throw umsCE;
		}
	}
	
	@GetMapping("/all")
	public ResponseEntity<?> getAllMenuItems() {
		log.info("MenuItemController.getAllPermissions() ENTERED.");
		try {
			log.info("MenuItemController.getAllMenuItems() is under execution...");
			List<MenuItemDTO> menuItemList = menuItemService.getAllMenuItems();
			log.info("MenuItemController.getAllMenuItems() executed successfully");
			return new ResponseEntity<>(menuItemList, HttpStatus.OK);
		}catch (EmptyListException businessException) {
			throw businessException;
		} 
		catch (Exception e) {
			log.info("MenuItemController.getAllMenuItems() exited with exception : Exception occured fetching menuitems list."
					+ e.getMessage());
			throw new ControllerException(ErrorCodeMessages.ERR_MENU_ITEM_GET_UNSUCCESS_CODE,
					ErrorCodeMessages.ERR_MENU_ITEM_GET_UNSUCCESS_MSG);
		}

	}

	@GetMapping("/{menuItemId}")
	public ResponseEntity<?> getPermissionById(@PathVariable Long menuItemId) {
	
		if (menuItemId <= 0) {
			log.info("PermissionController.getPermissionById() permissionId <0 exception ");
			throw new EmptyInputException(ErrorCodeMessages.ERR_ROLE_ID_IS_EMPTY_CODE,
					ErrorCodeMessages.ERR_ROLE_ID_IS_EMPTY_MSG);
		}
		try {
			log.info("PermissionController.getPermissionById() is under execution...");
			log.info("PermissionController.getPermissionById() ENTERED : permissionId : " + menuItemId);
			MenuItemDTO menuItem = menuItemService.getMenuItemById(menuItemId);
			log.info("PermissionController.getPermissionById() executed successfully");
			return new ResponseEntity<>(menuItem, HttpStatus.OK);
		}catch (EmptyInputException businessException) {
			throw businessException;
		}catch (Exception e) {
			ControllerException umsCE = new ControllerException(ErrorCodeMessages.ERR_MENU_ITEM_GET_UNSUCCESS_CODE,
					ErrorCodeMessages.ERR_MENU_ITEM_GET_UNSUCCESS_MSG);
			throw umsCE;
		}
	}
}
