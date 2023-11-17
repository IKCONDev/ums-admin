package com.ikn.ums.admin.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ikn.ums.admin.entity.Permission;
import com.ikn.ums.admin.entity.Role;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long>{

	//  Optional<Permission> findByPermissionId(Long permissionId);  
		Optional<Permission> findByPermissionValue(String name);
		 
		@Query ("FROM Permission WHERE permissionStatus=:permissionStatus")
		List<Permission> findAllPermissions(String permissionStatus);
		 
		/**
		 * Need to create the below TABLE for executing the Query
		 * 
		 * WE WILL CREATE THE ROLES AND PERMISSIONS MAP TABLE ----- WHICH CONTAINS THE PERMISSIONS ASSIGNED TO ROLES AND NEED TO CHECK THE USAGE
		 * 
		 * CREATE TABLE roles_permissions ( `role_id` BIGINT(20) NOT NULL,
		 * `permission_id` BIGINT(20) NOT NULL, PRIMARY KEY (role_id, permission_id), FOREIGN
		 * KEY (permission_id) REFERENCES permissions(id), FOREIGN KEY (role_id)
		 * REFERENCES roles(id) );
		 * 
		 * @param permissionId
		 * @return
		 */

	    @Query(value = "select count(*) from roles_permissions where permission_id = ?1", nativeQuery = true)
	    Long countPermissionUsage(Long permissionId);
//
//	    void deleteByPermission(String permission);
}
