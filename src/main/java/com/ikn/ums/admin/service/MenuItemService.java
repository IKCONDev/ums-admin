package com.ikn.ums.admin.service;

import java.util.List;
import java.util.Optional;

import com.ikn.ums.admin.dto.MenuItemDTO;
import com.ikn.ums.admin.entity.MenuItem;

public interface MenuItemService {

    public MenuItem createMenuItem(MenuItemDTO  menuItemDTO) ;
    public MenuItem updateMenuItem(MenuItemDTO menuItemDTO);	       
    public void deleteMenuItemById(Long menuItemId) ;
    public void deleteSelectedMenuItemByIds(List<Long> menuItemIds);
    public Optional<MenuItem> getMenuItemById(Long id);
    public List<MenuItem> getAllMenuItems();

}
