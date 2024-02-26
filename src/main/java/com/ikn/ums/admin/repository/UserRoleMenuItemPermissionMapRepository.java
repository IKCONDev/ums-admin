package com.ikn.ums.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ikn.ums.admin.entity.UserRoleMenuItemPermissionMap;
import java.util.List;


@Repository
public interface UserRoleMenuItemPermissionMapRepository extends JpaRepository<UserRoleMenuItemPermissionMap, Long> {
	
	List<UserRoleMenuItemPermissionMap> findByEmail(String email);
	
	void deleteByEmail(String email);
	
	@Modifying
	@Query("DELETE FROM UserRoleMenuItemPermissionMap WHERE roleId=:roleId AND menuItemIdList=:menuItemId")
	void deleteUserRoleMenuItemPermissionMapOfUserByMenuItemId(Long roleId, String menuItemId);

}
