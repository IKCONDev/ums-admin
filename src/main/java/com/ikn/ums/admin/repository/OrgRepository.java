package com.ikn.ums.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ikn.ums.admin.entity.Organization;

@Repository
public interface OrgRepository extends JpaRepository<Organization, Integer> {

	Organization findByOrgId(Integer orgId);

}
