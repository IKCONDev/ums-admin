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
import com.ikn.ums.admin.exception.MenuItemInUsageException;
import com.ikn.ums.admin.exception.MenuItemNameExistsException;
import com.ikn.ums.admin.service.MenuItemService;
import com.netflix.servo.util.Strings;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/menuitem")
public class MenuItemController {

	@Autowired
	private MenuItemService menuItemService;

	@PostMapping("/create")
	public ResponseEntity<MenuItemDTO> createMenuItem(@RequestBody MenuItemDTO menuItemDTO) {
		
		log.info("createMenuItem() entered ");
		if (menuItemDTO == null) {
			log.info("Permission Entity Not Found Exception has encountered while creating MenuItem.");
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_MENU_ITEM_ENTITY_IS_NULL_CODE,
					ErrorCodeMessages.ERR_MENU_ITEM_ENTITY_IS_NULL_MSG);
		}
		try {
			log.info("createMenuItem() is under execution.");
			log.info(":MenuItem Object : " + menuItemDTO );
			var createdMenuItem = menuItemService.createMenuItem(menuItemDTO);
			log.info("createMenuItem() executed successfully.");
			return new ResponseEntity<>(createdMenuItem, HttpStatus.CREATED);
		} catch (EntityNotFoundException | MenuItemNameExistsException menuBusinessException) {
			log.error("createMenuItem() : An error occurred: {}." + menuBusinessException.getMessage(), menuBusinessException);

			throw menuBusinessException;
		} catch (Exception e) {
			log.error("General Exception has encountered while creating MenuItem. " + e.getMessage());
			throw new ControllerException(ErrorCodeMessages.ERR_MENU_ITEM_CREATE_UNSUCCESS_CODE,
					ErrorCodeMessages.ERR_MENU_ITEM_CREATE_UNSUCCESS_MSG);
		}
	}

	@PutMapping("/update")
	public ResponseEntity<MenuItemDTO> updateMenuItem(@RequestBody MenuItemDTO menuItemDTO) {
		log.info("updateMenuItem() entered with args");
		if (menuItemDTO == null) {
			log.info("MenuItem Entity Not Found Exception has encountered while updating Role.");
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_PERMISSION_ENTITY_IS_NULL_CODE,
					ErrorCodeMessages.ERR_PERMISSION_ENTITY_IS_NULL_MSG);
		}
		try {
			log.info("updateMenuItem() is under execution.");
			var updatedMenuItemDTO = menuItemService.updateMenuItem(menuItemDTO);
			log.info("updateMenuItem() is executed sucessfully.");
			return new ResponseEntity<>(updatedMenuItemDTO, HttpStatus.PARTIAL_CONTENT);
		}catch (EntityNotFoundException | MenuItemNameExistsException| EmptyInputException  menuItemBusinessException) {
			log.error("MenuItem Business Exception has encountered while updating MenuItem. " + menuItemBusinessException.getMessage());
			throw menuItemBusinessException;
		}catch (Exception e) {
			log.error("General Exception has encountered while updating MenuItem. " + e.getMessage(), e);
			throw new ControllerException(ErrorCodeMessages.ERR_MENU_ITEM_UPDATE_UNSUCCESS_CODE, 
					ErrorCodeMessages.ERR_MENU_ITEM_UPDATE_UNSUCCESS_MSG);
		}
	}

	@DeleteMapping("/delete/{ids}")
	public ResponseEntity<Boolean> deleteSelectedMenuItems(@PathVariable("ids") List<Long> menuItemIds) {
		log.info("deleteSelectedMenuItems() entered ");
		if (menuItemIds == null || menuItemIds.isEmpty() ) {
			log.info("deleteSelectedMenuItems() EmptyInputException : menuItem Id/Ids are empty");
			throw new EmptyListException(ErrorCodeMessages.ERR_MENU_ITEM_ID_IS_EMPTY_CODE,
					ErrorCodeMessages.ERR_MENU_ITEM_ID_IS_EMPTY_MSG);
		}
		try {
			log.info("deleteSelectedMenuItems() is under execution...");
			
			menuItemService.deleteSelectedMenuItemByIds(menuItemIds);
			log.info("deleteSelectedMenuItems() executed successfully");
			return new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);
		}catch (EmptyListException | MenuItemInUsageException businessException ) {
			throw businessException;
		} catch (Exception e) {
			throw new ControllerException(ErrorCodeMessages.ERR_MENU_ITEM_DELETE_UNSUCCESS_CODE,
					ErrorCodeMessages.ERR_MENU_ITEM_DELETE_UNSUCCESS_MSG);
		}
	}
	
	@GetMapping("/all")
	public ResponseEntity<List<MenuItemDTO>> getAllMenuItems() {
		log.info("getAllPermissions() ENTERED.");
		try {
			log.info("getAllMenuItems() is under execution...");
			var menuItemList = menuItemService.getAllMenuItems();
			log.info("getAllMenuItems() executed successfully");
			return new ResponseEntity<>(menuItemList, HttpStatus.OK);
		}catch (EmptyListException businessException) {
			throw businessException;
		} 
		catch (Exception e) {
			log.error("getAllMenuItems() exited with exception : Exception occured fetching menuitems list."
					+ e.getMessage(),e);
			throw new ControllerException(ErrorCodeMessages.ERR_MENU_ITEM_GET_UNSUCCESS_CODE,
					ErrorCodeMessages.ERR_MENU_ITEM_GET_UNSUCCESS_MSG);
		}

	}

	@GetMapping("/{menuItemId}")
	public ResponseEntity<MenuItemDTO> getMenuItemById(@PathVariable Long menuItemId) {
		log.info("getMenuItemById() entered with args - menuItemId");
		if (menuItemId <= 0||menuItemId==null) {
			log.info("getMenuItemById() permissionId < 0 EmptyInputException ");
			throw new EmptyInputException(ErrorCodeMessages.ERR_MENU_ITEM_ID_IS_EMPTY_CODE,
					ErrorCodeMessages.ERR_MENU_ITEM_ID_IS_EMPTY_MSG);
		}
		try {
			log.info("getMenuItemById() is under execution...");
			log.info("getMenuItemById() ENTERED : menuItemId : " + menuItemId);
			var menuItem = menuItemService.getMenuItemById(menuItemId);
			log.info("getMenuItemById() executed successfully");
			return new ResponseEntity<>(menuItem, HttpStatus.OK);
		}catch (EmptyInputException businessException) {
			log.error("getMenuItemById() : An error occurred: {}." + businessException.getMessage(), businessException);
			throw businessException;
		}catch (Exception e) {
			throw new ControllerException(ErrorCodeMessages.ERR_MENU_ITEM_GET_UNSUCCESS_CODE,
					ErrorCodeMessages.ERR_MENU_ITEM_GET_UNSUCCESS_MSG);
		}
	}
	
	@GetMapping("/get/{menuItemName}")
	public ResponseEntity<MenuItemDTO> getMenuItemByName(@PathVariable String menuItemName) {
		log.info("getMenuItemById() entered with args menuItemName");
		if (Strings.isNullOrEmpty(menuItemName) || menuItemName.isEmpty()) {
			log.info("getMenuItemById() permissionId <0 exception ");
			throw new EmptyInputException(ErrorCodeMessages.ERR_MENU_ITEM_ID_IS_EMPTY_CODE,
					ErrorCodeMessages.ERR_MENU_ITEM_ID_IS_EMPTY_MSG);
		}
		try {
			log.info("getMenuItemById() is under execution...");
			log.info("getMenuItemById() ENTERED : menuItemId : " + menuItemName);
			var menuItem = menuItemService.getMenuItemByName(menuItemName);
			log.info("getMenuItemById() executed successfully");
			return new ResponseEntity<>(menuItem, HttpStatus.OK);
		}catch (EmptyInputException businessException) {
			log.error("getMenuItemByName() : An error occurred: {}." + businessException.getMessage(), businessException);
			throw businessException;
		}catch (Exception e) {
			throw new ControllerException(ErrorCodeMessages.ERR_MENU_ITEM_GET_UNSUCCESS_CODE,
					ErrorCodeMessages.ERR_MENU_ITEM_GET_UNSUCCESS_MSG);
		}
	}
}
