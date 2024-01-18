package com.ikn.ums.admin.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
import com.ikn.ums.admin.repository.MenuItemRepository;
import com.ikn.ums.admin.service.MenuItemService;
import com.ikn.ums.admin.utils.AdminConstants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MenuItemServiceImpl implements MenuItemService {

	@Autowired
	private MenuItemRepository menuItemRepository;

	@Autowired
	private ModelMapper mapper;

	@Transactional
	@Override
	public MenuItemDTO createMenuItem(MenuItemDTO menuItemDTO) {

		log.info("MenuItemServiceImpl.createMenuItem() ENTERED");

		if (menuItemDTO == null) {
			log.info("Menu Item Object is null .... ");
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_MENU_ITEM_ENTITY_IS_NULL_CODE,
					ErrorCodeMessages.ERR_MENU_ITEM_ENTITY_IS_NULL_MSG);
		}
		if (Strings.isNullOrEmpty(menuItemDTO.getMenuItemName())) {
			log.info("Menu Item Name is null .... ");
			throw new EmptyInputException(ErrorCodeMessages.ERR_MENU_ITEM_NAME_IS_EMPTY_CODE,
					ErrorCodeMessages.ERR_MENU_ITEM_NAME_IS_EMPTY_MSG);
		}
		if (isMenuItemNameExists(menuItemDTO.getMenuItemName())) {
			log.info("Menu Item Name, " + menuItemDTO.getMenuItemName() + " already exists.");
			throw new MenuItemNameExistsException(ErrorCodeMessages.ERR_MENU_ITEM_NAME_EXISTS_CODE,
					ErrorCodeMessages.ERR_MENU_ITEM_NAME_EXISTS_MSG);
		}

		log.info("MenuItemServiceImpl.createMenuItem() is under execution...");

		var menuItem = new MenuItem();
		menuItem.setMenuItemId(menuItemDTO.getMenuItemId());
		menuItem.setMenuItemName(menuItemDTO.getMenuItemName());
		menuItem.setMenuItemPath(menuItemDTO.getMenuItemPath());
		menuItem.setMenuItemDescription(menuItemDTO.getMenuItemDescription());
		menuItem.setCreatedBy(menuItemDTO.getCreatedBy());
		menuItem.setCreatedDateTime(LocalDateTime.now());
		menuItem.setMenuItemStatus(AdminConstants.STATUS_ACTIVE);

		var createdMenuItem = menuItemRepository.save(menuItem);
		var createdMenuItemDTO = new MenuItemDTO();
		mapper.map(createdMenuItem, createdMenuItemDTO);
		log.info("MenuItemServiceImpl.createMenuItem() executed successfully");
		return createdMenuItemDTO;
	}

	@Transactional
	@Override
	public MenuItemDTO updateMenuItem(MenuItemDTO menuItemDTO) {

		log.info("MenuItemServiceImpl.updateMenuItem() ENTERED");
		if (menuItemDTO == null) {
			log.info("Menu Item data cannot be null !");
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_MENU_ITEM_ENTITY_IS_NULL_CODE,
					ErrorCodeMessages.ERR_MENU_ITEM_ENTITY_IS_NULL_MSG);
		}

		var menuItemId = menuItemDTO.getMenuItemId();
		Optional<MenuItem> optMenuItem = menuItemRepository.findById(menuItemId);

		if (!optMenuItem.isPresent()) {
			throw new EntityNotFoundException(menuItemId.toString(),
					"The menu item with the id = %s has not been found");
		}
		log.info("MenuItemServiceImpl.updateMenuItem() is under execution...");

		MenuItem dbMenuItem = null;
		if (optMenuItem.isEmpty()) {
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_MENU_ITEM_DB_ENTITY_NOTFOUND_CODE, 
					ErrorCodeMessages.ERR_MENU_ITEM_DB_ENTITY_NOTFOUND_MSG);
		}
		dbMenuItem = optMenuItem.get();
		dbMenuItem.setMenuItemName(menuItemDTO.getMenuItemName());
		dbMenuItem.setMenuItemPath(menuItemDTO.getMenuItemPath());
		dbMenuItem.setMenuItemDescription(menuItemDTO.getMenuItemDescription());
		// dbMenuItem.setModifiedDateTime(LocalDateTime.now());
		dbMenuItem.setModifiedBy(menuItemDTO.getModifiedBy());
		dbMenuItem.setModifiedDateTime(LocalDateTime.now());
		var updatedMenuItem = menuItemRepository.save(dbMenuItem);
		var updatedMenuItemDTO = new MenuItemDTO();
		mapper.map(updatedMenuItem, updatedMenuItemDTO);
		log.info("MenuItemServiceImpl.updateMenuItem() executed successfully");
		return updatedMenuItemDTO;
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

		if (!optMenuItem.isPresent()) {
			log.info("Menu Item is not found");
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_MENU_ITEM_ENTITY_IS_NULL_CODE,
					ErrorCodeMessages.ERR_MENU_ITEM_ENTITY_IS_NULL_MSG);
		}
		var menuItem = optMenuItem.get();
		// Check Permissions Usage
		var rowsFound = menuItemRepository.countMenuItemUsage(menuItem.getMenuItemId());
		if (rowsFound > 0) {
			log.info("Menu Items are assigned to Role and cannot be deleted ! : " + rowsFound);
			// Menu Items cannot be deleted as they are already in use
			throw new MenuItemInUsageException(ErrorCodeMessages.ERR_MENU_ITEM_IS_IN_USAGE_CODE,
					ErrorCodeMessages.ERR_MENU_ITEM_IS_IN_USAGE_MSG);
		}
		var menuItemDTO = new MenuItemDTO();
		menuItemDTO.setMenuItemStatus(AdminConstants.STATUS_IN_ACTIVE);
		updateMenuItem(menuItemDTO);
		log.info("MenuItemServiceImpl.deleteMenuItemById() executed successfully !");

	}

	@Transactional
	@Override
	public void deleteSelectedMenuItemByIds(List<Long> menuItemIds) {
		log.info("MenuItemServiceImpl.deleteSelectedMenuItemByIds() ENTERED ");
		if (menuItemIds.isEmpty()) {
			log.info("deleteSelectedMenuItemByIds() EmptyListException : menuItemids list is empty.");
			throw new EmptyListException(ErrorCodeMessages.ERR_MENU_ITEM_LIST_IS_EMPTY_CODE,
					ErrorCodeMessages.ERR_MENU_ITEM_LIST_IS_EMPTY_MSG);
		}
		log.info("deleteSelectedMenuItemByIds() is under execution...");
		menuItemIds.forEach(id -> {
			var rowsFound = menuItemRepository.countMenuItemUsage(id);
			if(rowsFound > 0) {
				log.info("deleteSelectedMenuItemByIds() MenuItemInUsageException :Menu item trying to delete is already in usage.");
				throw new MenuItemInUsageException(ErrorCodeMessages.ERR_MENU_ITEM_IS_IN_USAGE_CODE, 
						ErrorCodeMessages.ERR_MENU_ITEM_IS_IN_USAGE_MSG);
			}
		});
		var menuItemList = menuItemRepository.findAllById(menuItemIds);		
		if (menuItemList.size() > 0) {
			menuItemList.forEach(menuItem -> {
				menuItem.setMenuItemStatus(AdminConstants.STATUS_IN_ACTIVE);
			});
		}
		log.info("deleteSelectedMenuItemByIds() executed successfully.");
	}

	@Transactional
	@Override
	public MenuItemDTO getMenuItemById(Long menuItemId) {

		log.info("MenuItemServiceImpl.getMenuItemById() ENTERED ");

		if (menuItemId <= 0) {
			log.info("getMenuItemById() EmptyInputException : Menu Item Id is null !");
			throw new EmptyInputException(ErrorCodeMessages.ERR_MENU_ITEM_ID_IS_EMPTY_CODE,
					ErrorCodeMessages.ERR_MENU_ITEM_ID_IS_EMPTY_MSG);
		}
		Optional<MenuItem> optMenuItem = menuItemRepository.findById(menuItemId);
		if (!optMenuItem.isPresent()) {
			log.info("getMenuItemById() EntityNotFoundException : Menu Item is not found for Menu ItemId id : " + menuItemId);
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_MENU_ITEM_ENTITY_IS_NULL_MSG,
					ErrorCodeMessages.ERR_MENU_ITEM_ENTITY_IS_NULL_CODE);
		}
		var menuItemDTO = new MenuItemDTO();
		mapper.map(optMenuItem.get(), menuItemDTO);
		return menuItemDTO;
	}

	@Transactional
	@Override
	public List<MenuItemDTO> getAllMenuItems() {
		log.info(" MenuItemServiceImpl.getAllMenuItems() ENTERED ");
		List<MenuItem> menuItemList = null;
		log.info("getAllMenuItems() is under execution...");
		menuItemList = menuItemRepository.findAllMenuItems(AdminConstants.STATUS_ACTIVE);
		List<MenuItemDTO> menuItemDTOList = new ArrayList<>();
		menuItemList.stream().forEach(menuItem -> {
			MenuItemDTO menuItemDTO = new MenuItemDTO();
			mapper.map(menuItem, menuItemDTO);
			menuItemDTOList.add(menuItemDTO);
		});
		log.info("getAllMenuItems() : Total Menu Items Count : " + menuItemDTOList.size());
		log.info("getAllMenuItems() executed successfully");
		return menuItemDTOList;
	}

	public boolean isMenuItemNameExists(String menuItemName) {
		log.info("MenuItemServiceImpl.isMenuItemNameExists() entered with args - menuItemName");
		boolean isMenuItemNameExists = false;
		if (Strings.isNullOrEmpty(menuItemName) || menuItemName.isEmpty()) {
			log.info("MenuItemServiceImpl.isMenuItemNameExists() Menu Item Name is empty");
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_MENU_ITEM_NAME_IS_NULL_CODE,
					ErrorCodeMessages.ERR_MENU_ITEM_NAME_IS_NULL_MSG);
		} else {
			log.info("MenuItemServiceImpl.isMenuItemNameExists()  : menuItemName : " + menuItemName);
			var optMenuItem = menuItemRepository.findByMenuItemName(menuItemName);

			isMenuItemNameExists = optMenuItem.isPresent();
			log.info("MenuItemServiceImpl  : isMenuItemNameExists : " + isMenuItemNameExists);
		}
		log.info("MenuItemServiceImpl.isMenuItemNameExists() executed successfully");
		return isMenuItemNameExists;
	}

	@Override
	public MenuItemDTO getMenuItemByName(String menuItemName) {
		log.info("MenuItemServiceImpl.getMenuItemById() ENTERED ");
		if ( Strings.isNullOrEmpty(menuItemName) || menuItemName.isBlank()) {
			log.info("Menu Item Id is null !");
			throw new EmptyInputException(ErrorCodeMessages.ERR_MENU_ITEM_ID_IS_EMPTY_CODE,
					ErrorCodeMessages.ERR_MENU_ITEM_ID_IS_EMPTY_MSG);
		}
		Optional<MenuItem> optMenuItem = menuItemRepository.findByMenuItemName(menuItemName);
		if (!optMenuItem.isPresent()) {
			log.info("Menu Item is not found for Menu ItemName : " + menuItemName);
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_MENU_ITEM_DB_ENTITY_NOTFOUND_CODE,
					ErrorCodeMessages.ERR_MENU_ITEM_DB_ENTITY_NOTFOUND_MSG);
		}
		var menuItemDTO = new MenuItemDTO();
		mapper.map(optMenuItem.get(), menuItemDTO);
		log.info("MenuItemServiceImpl.getMenuItemById() executed successfully.");
		return menuItemDTO;
	}

}
