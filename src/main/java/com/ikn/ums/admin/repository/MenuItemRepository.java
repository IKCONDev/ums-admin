package com.ikn.ums.admin.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ikn.ums.admin.entity.MenuItem;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long > {

	Optional<MenuItem> findByMenuItemName(String name);
	 
	@Query ("FROM MenuItem WHERE menuItemStatus=:menuItemStatus")
	List<MenuItem> findAllMenuItems(String menuItemStatus);
	 
	/**
	 * Need to create the below TABLE for executing the Query
	 * 
	 * WE WILL CREATE THE ROLES AND MENU ITEM MAP TABLE ----- WHICH CONTAINS THE MENU ITEMS ASSIGNED TO ROLES AND NEED TO CHECK THE USAGE
	 * 
	 */

    @Query(value = "select count(*) from role_menu_items_tab where menu_item_id = ?1", nativeQuery = true)
    Long countMenuItemUsage(Long menuItemId);
//
//    void deleteByPermission(String permission);
  
}
