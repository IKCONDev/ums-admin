package com.ikn.ums.admin.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ikn.ums.admin.entity.Organization;

@Service
public interface OrgService {

    public List<Organization> getAllOrgs();
    public Optional<Organization> getOrgById(Integer orgId);
    public Organization createOrg(Organization org) ;
    public Organization updateOrg(Integer orgId, Organization updatedOrg);	       
    public void deleteOrg(Integer id) ;

}
