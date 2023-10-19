package com.ikn.ums.admin.service.impl;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ikn.ums.admin.entity.Organization;
import com.ikn.ums.admin.exception.EmptyInputException;
import com.ikn.ums.admin.exception.EmptyListException;
import com.ikn.ums.admin.exception.EntityNotFoundException;
import com.ikn.ums.admin.exception.ErrorCodeMessages;
import com.ikn.ums.admin.repository.OrgRepository;
import com.ikn.ums.admin.service.OrgService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrgServiceImpl implements OrgService {
	
    @Autowired
    private OrgRepository orgRepository;
    
    @Autowired
    private ModelMapper mapper;

	@Override
	public List<Organization> getAllOrgs() {
		log.info("OrgServiceImpl.getAllOrgs() ENTERED.");
		List<Organization> orgsList = null;
		orgsList = orgRepository.findAll();
		return orgsList;
	}

	@Override
	public Organization getOrgById(Integer orgId) {
		log.info("OrgServiceImpl.getOrgById() ENTERED : orgId : " + orgId);
		if (orgId <= 0)
			throw new EmptyInputException(ErrorCodeMessages.ERR_ORG_ID_IS_EMPTY_CODE,
					ErrorCodeMessages.ERR_ORG_ID_IS_EMPTY_MSG);
		Optional<Organization> optOrg = orgRepository.findById(orgId);
		Organization org = null;
		if(optOrg.isPresent()) {
			org = optOrg.get();
		}
		
		return org;
	}

	@Override
	public Organization createOrg(Organization org) {
		log.info("OrgServiceImpl.createOrg() ENTERED");
		if (org == null) 
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_ORG_ENTITY_IS_NULL_CODE,
					ErrorCodeMessages.ERR_ORG_ENTITY_IS_NULL_MSG);
		Organization savedOrg = orgRepository.save(org);
		return savedOrg;
	}

	@Override
	public Organization updateOrg(Organization org) {
		Optional<Organization> optOrg = orgRepository.findById(org.getOrgId());
		Organization dbOrg = null;
		if(optOrg.isPresent()) {
			dbOrg = optOrg.get();
		}
		mapper.map(org, dbOrg);
		Organization updatedOrg = orgRepository.save(dbOrg);
		return updatedOrg;
	}

	@Override
	public void deleteOrgById(Integer orgId) {
		log.info("OrgServiceImpl.deleteOrg() ENTERED : orgId : " + orgId);
		if (orgId <= 0)
			throw new EmptyInputException(ErrorCodeMessages.ERR_ROLE_ID_IS_EMPTY_CODE,
					ErrorCodeMessages.ERR_ROLE_ID_IS_EMPTY_MSG);
		orgRepository.deleteById(orgId);
	}

}
