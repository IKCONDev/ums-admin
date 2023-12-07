package com.ikn.ums.admin.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ikn.ums.admin.entity.Organization;

@Service
public interface OrgService {

    public List<Organization> getAllOrgs();
    public Organization getOrgById(Integer orgId);
    public Organization createOrg(Organization org) ;
    public Organization updateOrg(Organization updatedOrg);	       
    public void deleteOrgById(Integer id) ;
	public void deleteOrgPic(Integer orgId);

}
