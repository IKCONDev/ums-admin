package com.ikn.ums.admin.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ikn.ums.admin.dto.OrganizationDTO;

@Service
public interface OrgService {

    public List<OrganizationDTO> getAllOrgs();
    public OrganizationDTO getOrgById(Integer orgId);
    public OrganizationDTO createOrg(OrganizationDTO org) ;
    public OrganizationDTO updateOrg(OrganizationDTO updatedOrg);	       
    public void deleteOrgById(Integer id) ;
	public void deleteOrgPic(Integer orgId);

}
