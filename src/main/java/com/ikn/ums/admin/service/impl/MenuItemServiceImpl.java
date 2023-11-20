package com.ikn.ums.admin.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Strings;
import com.ikn.ums.admin.dto.MenuItemDTO;
import com.ikn.ums.admin.entity.MenuItem;
import com.ikn.ums.admin.exception.EmptyInputException;
import com.ikn.ums.admin.exception.EmptyListException;
import com.ikn.ums.admin.exception.EntityNotFoundException;
import com.ikn.ums.admin.exception.ErrorCodeMessages;
import com.ikn.ums.admin.exception.MenuItemInUsageException;
import com.ikn.ums.admin.exception.MenuItemNameExistsException;
import com.ikn.ums.admin.exception.PermissionNameExistsException;
import com.ikn.ums.admin.repository.MenuItemRepository;
import com.ikn.ums.admin.service.MenuItemService;
import com.ikn.ums.admin.utils.AdminConstants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MenuItemServiceImpl implements MenuItemService{
	
	@Autowired
	private MenuItemRepository menuItemRepository;

	@Autowired
	private ModelMapper mapper;
	
	@Transactional
	@Override
	public MenuItem createMenuItem(MenuItemDTO menuItemDTO) {
	
		log.info("MenuItemServiceImpl.createMenuItem() ENTERED");
	
		if (menuItemDTO == null) {
			log.info("Menu Item Object is null .... ");
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_PERMISSION_ENTITY_IS_NULL_CODE,
					ErrorCodeMessages.ERR_PERMISSION_ENTITY_IS_NULL_MSG);
		}
		if (Strings.isNullOrEmpty(menuItemDTO.getMenuItemName())) {
			log.info("Menu Item Name is null .... ");
			throw new EmptyInputException(ErrorCodeMessages.ERR_MENU_ITEM_NAME_IS_EMPTY_CODE,
					ErrorCodeMessages.ERR_MENU_ITEM_NAME_IS_EMPTY_MSG);
		}
		if ( isMenuItemNameExists(menuItemDTO.getMenuItemName())) {
			log.info("Menu Item Name, " + menuItemDTO.getMenuItemName() + " already exists.");
			throw new PermissionNameExistsException(ErrorCodeMessages.ERR_MENU_ITEM_NAME_EXISTS_CODE,
					ErrorCodeMessages.ERR_MENU_ITEM_NAME_EXISTS_MSG);
		}
	
		log.info("MenuItemServiceImpl.createMenuItem() is under execution...");
	
		MenuItem menuItem = new MenuItem();
		menuItem.setMenuItemId(menuItemDTO.getMenuItemId());
		menuItem.setMenuItemName(menuItemDTO.getMenuItemName());
		menuItem.setMenuItemPath(menuItemDTO.getMenuItemPath());
		menuItem.setMenuItemDescription(menuItemDTO.getMenuItemDescription());
		menuItem.setCreatedDateTime(LocalDateTime.now());
		menuItem.setMenuItemStatus(AdminConstants.STATUS_ACTIVE);

		MenuItem createdMenuItem = menuItemRepository.save(menuItem);
		log.info("MenuItemServiceImpl.createMenuItem() executed successfully");
		return createdMenuItem;
	}

	@Transactional
	@Override
	public MenuItem updateMenuItem(MenuItemDTO menuItemDTO) {
		
		log.info("MenuItemServiceImpl.updateMenuItem() ENTERED");
		if (menuItemDTO == null) {
			log.info("Menu Item data cannot be null !");
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_MENU_ITEM_ENTITY_IS_NULL_CODE,
					ErrorCodeMessages.ERR_MENU_ITEM_ENTITY_IS_NULL_MSG);
		}

		Long menuItemId = menuItemDTO.getMenuItemId();
		Optional<MenuItem> optMenuItem = menuItemRepository.findById(menuItemId);

		if (!optMenuItem.isPresent()) {
			throw new EntityNotFoundException(menuItemId.toString(),
					"The menu item with the id = %s has not been found");
		}

		if (isMenuItemNameExists(menuItemDTO.getMenuItemName())) {
			log.info("Exists already a menu item with the Value %s. Use another Value. "
					+ menuItemDTO.getMenuItemName());
			throw new MenuItemNameExistsException(ErrorCodeMessages.ERR_PERMISSION_VALUE_EXISTS_CODE,
					ErrorCodeMessages.ERR_PERMISSION_VALUE_EXISTS_CODE);
		}
		log.info("MenuItemServiceImpl.updateMenuItem() is under execution...");

		MenuItem menuItem = new MenuItem();
		menuItem.setMenuItemName(menuItemDTO.getMenuItemName());
		menuItem.setMenuItemPath(menuItemDTO.getMenuItemPath());
		menuItem.setMenuItemDescription(menuItemDTO.getMenuItemDescription());
		menuItem.setModifiedDateTime(LocalDateTime.now());

		MenuItem updatedMenuItem = menuItemRepository.save(menuItem);
		
		log.info("MenuItemServiceImpl.updateMenuItem() executed successfully");
		return updatedMenuItem;
	}

	@Transactional
	@Override
	public void deleteMenuItemById(Long menuItemId) {

		log.info("MenuItemServiceImpl.deleteMenuItemById() ENTERED ");
		if (menuItemId <= 0) {
			log.info("Menu Item Id cannot be empty menuItemId : " + menuItemId);
			throw new EmptyInputException(ErrorCodeMessages.ERR_MENU_ITEM_ID_IS_EMPTY_CODE,
					ErrorCodeMessages.ERR_MENU_ITEM_ID_IS_EMPTY_MSG);
		}

		log.info("MenuItemServiceImpl.deleteMenuItemById() is under execution...");
		Optional<MenuItem> optMenuItem = menuItemRepository.findById(menuItemId);

		if (!optMenuItem.isPresent() || optMenuItem == null) {
			log.info("Menu Item is not found");
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_MENU_ITEM_ENTITY_IS_NULL_CODE,
					ErrorCodeMessages.ERR_MENU_ITEM_ENTITY_IS_NULL_MSG);
		}

		MenuItem menuItem = optMenuItem.get();

		//Check Permissions Usage
		Long rowsFound = menuItemRepository.countMenuItemUsage(menuItem.getMenuItemId());
		if (rowsFound > 0) {
			log.info("Menu Items are assigned to Role and cannot be deleted ! : " + rowsFound );
			// Menu Items cannot be deleted as they are already in use
			throw new MenuItemInUsageException(ErrorCodeMessages.ERR_MENU_ITEM_IS_IN_USAGE_CODE,
					ErrorCodeMessages.ERR_MENU_ITEM_IS_IN_USAGE_MSG);
		}

		MenuItemDTO menuItemDTO = new MenuItemDTO();
		menuItemDTO.setMenuItemStatus(AdminConstants.STATUS_IN_ACTIVE);
		
		updateMenuItem(menuItemDTO);//Calling Update method to set Status to In Active
		
		log.info("MenuItemServiceImpl.deleteMenuItemById() executed successfully !");
		
	}

	@Transactional
	@Override
	public void deleteSelectedMenuItemByIds(List<Long> menuItemIds) {
		
		log.info("MenuItemServiceImpl.deleteSelectedMenuItemByIds() ENTERED " );
		
		if ( menuItemIds.size() <= 0 ) {
			log.info(": menuItemIds Size : " + menuItemIds.size() );
			throw new EmptyListException(ErrorCodeMessages.ERR_MENU_ITEM_LIST_IS_EMPTY_CODE,
					ErrorCodeMessages.ERR_MENU_ITEM_LIST_IS_EMPTY_MSG);	
		}
				
		List<MenuItem> menuItemList = menuItemRepository.findAllById(menuItemIds);
		
		//TODO: Need to implement the logic for each Permission Id, we have to check if the menu item is in usage
		
		if (menuItemList.size() > 0) {
			menuItemList.forEach(menuItem -> {
				menuItem.setMenuItemStatus(AdminConstants.STATUS_IN_ACTIVE) ;
			});
		}
	}

	@Transactional
	@Override
	public Optional<MenuItem> getMenuItemById(Long menuItemId) {

		log.info("MenuItemServiceImpl.getMenuItemById() ENTERED ");
		
		if (menuItemId <= 0) {
			log.info("Menu Item Id is null !");
			throw new EmptyInputException(ErrorCodeMessages.ERR_MENU_ITEM_ID_IS_EMPTY_CODE,
					ErrorCodeMessages.ERR_MENU_ITEM_ID_IS_EMPTY_MSG);
		}
		
		Optional<MenuItem> optMenuItem = menuItemRepository.findById(menuItemId);
		if (!optMenuItem.isPresent() || optMenuItem == null) {
			log.info("Menu Item is not found for Menu ItemId id : " + menuItemId);
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_PERMISSION_ENTITY_IS_NULL_CODE,
					ErrorCodeMessages.ERR_PERMISSION_ENTITY_IS_NULL_MSG);
		}
		return optMenuItem;
	}

	@Transactional
	@Override
	public List<MenuItem> getAllMenuItems() {
		log.info(" MenuItemServiceImpl.getAllMenuItems() ENTERED " );
		List<MenuItem> menuItemList = null;
		log.info("getAllMenuItems() is under execution...");
		menuItemList = menuItemRepository.findAllMenuItems(AdminConstants.STATUS_ACTIVE);
		
		if ( menuItemList == null || menuItemList.isEmpty() || menuItemList.size() == 0 )
			throw new EmptyListException(ErrorCodeMessages.ERR_MENU_ITEM_LIST_IS_EMPTY_CODE,
					ErrorCodeMessages.ERR_MENU_ITEM_LIST_IS_EMPTY_MSG);
		log.info("getAllMenuItems() : Total Menu Items Count : " + menuItemList.size());
		log.info("getAllMenuItems() executed successfully");
		return menuItemList;
	}
	
	public boolean isMenuItemNameExists(String menuItemName) {
		boolean isMenuItemNameExists = false;

		if (menuItemName == null || menuItemName.length() <= 0 || Strings.isNullOrEmpty(menuItemName)) {
			log.info("MenuItemServiceImpl.isMenuItemNameExists() Menu Item Name is empty");
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_MENU_ITEM_NAME_IS_NULL_CODE,
					ErrorCodeMessages.ERR_MENU_ITEM_NAME_IS_NULL_MSG);
		} else {
			log.info("MenuItemServiceImpl.isMenuItemNameExists()  : menuItemName : " + menuItemName);
			Optional<MenuItem> optMenuItem = menuItemRepository.findByMenuItemName(menuItemName);

			isMenuItemNameExists = optMenuItem.isPresent();
			log.info("MenuItemServiceImpl  : isMenuItemNameExists : " + isMenuItemNameExists);
		}
		log.info("MenuItemServiceImpl.isMenuItemNameExists() executed successfully");
		return isMenuItemNameExists;
	}



}