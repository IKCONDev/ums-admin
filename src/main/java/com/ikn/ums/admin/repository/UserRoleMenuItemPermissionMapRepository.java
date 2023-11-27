package com.ikn.ums.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ikn.ums.admin.entity.UserRoleMenuItemPermissionMap;

@Repository
public interface UserRoleMenuItemPermissionMapRepository extends JpaRepository<UserRoleMenuItemPermissionMap, Long> {

}
