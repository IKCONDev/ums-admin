package com.ikn.ums.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ikn.ums.admin.entity.UserRoleMenuItemPermissionMap;
import java.util.List;
import java.util.Optional;


@Repository
public interface UserRoleMenuItemPermissionMapRepository extends JpaRepository<UserRoleMenuItemPermissionMap, Long> {
	
	Optional<UserRoleMenuItemPermissionMap> findByEmail(String email);

}
