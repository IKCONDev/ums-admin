package com.ikn.ums.admin.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ikn.ums.admin.entity.Role;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
	
	 Optional<Role> findByRoleName(String name);
	 
	 @Query ("FROM Role WHERE roleStatus=:roleStatus")
	 List<Role> findAllRoles(String roleStatus);
	
}
