package com.ikn.ums.admin.service;

import java.util.List;
import java.util.Optional;

import com.ikn.ums.admin.dto.MenuItemDTO;
import com.ikn.ums.admin.entity.MenuItem;

public interface MenuItemService {

    public MenuItemDTO createMenuItem(MenuItemDTO  menuItemDTO) ;
    public MenuItemDTO updateMenuItem(MenuItemDTO menuItemDTO);	       
    public void deleteMenuItemById(Long menuItemId) ;
    public void deleteSelectedMenuItemByIds(List<Long> menuItemIds);
    public MenuItemDTO getMenuItemById(Long id);
    public List<MenuItemDTO> getAllMenuItems();

}
