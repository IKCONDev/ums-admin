package com.ikn.ums.admin.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ikn.ums.admin.dto.OrganizationDTO;
import com.ikn.ums.admin.entity.Organization;
import com.ikn.ums.admin.exception.EmptyInputException;
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
	public List<OrganizationDTO> getAllOrgs() {
		List<OrganizationDTO> orgDTOList = new ArrayList<>();
		log.info("getAllOrgs() ENTERED.");
		log.info("getAllOrgs() is under execution...");
		List<Organization> orgsList = null;
		orgsList = orgRepository.findAll();
		orgsList.forEach(entity -> {
			OrganizationDTO dto = new OrganizationDTO();
			mapper.map(entity, dto);
			orgDTOList.add(dto);
		});
		log.info("getAllOrgs() executed successfully");
		return orgDTOList;
	}

	@Override
	public OrganizationDTO getOrgById(Integer orgId) {
		log.info("getOrgById() ENTERED : orgId : " + orgId);
		if (orgId <= 0)
			throw new EmptyInputException(ErrorCodeMessages.ERR_ORG_ID_IS_EMPTY_CODE,
					ErrorCodeMessages.ERR_ORG_ID_IS_EMPTY_MSG);
		log.info("getOrgById() is under execution..");
		Optional<Organization> optOrg = orgRepository.findById(orgId);
		Organization org = null;
		if(optOrg.isEmpty()) {
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_ORG_DB_ENTITY_NOTFOUND_CODE, 
					ErrorCodeMessages.ERR_ORG_DB_ENTITY_NOTFOUND_MSG);
		}
		org = optOrg.get();
		OrganizationDTO orgDTO = new OrganizationDTO();
		mapper.map(org, orgDTO);
		log.info("getOrgById() executed successfully");
		return orgDTO;
	}
    @Transactional
	@Override
	public OrganizationDTO createOrg(OrganizationDTO org) {
		log.info("createOrg() ENTERED");
		if (org == null) 
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_ORG_ENTITY_IS_NULL_CODE,
					ErrorCodeMessages.ERR_ORG_ENTITY_IS_NULL_MSG);
		log.info("createOrg() is under execution...");
		Organization entity = new Organization();
		mapper.map(org, entity);
		Organization savedOrg = orgRepository.save(entity);
		OrganizationDTO savedOrgDTO = new OrganizationDTO();
		mapper.map(savedOrg, savedOrgDTO);
		log.info("createOrg() exected successfully");
		return savedOrgDTO;
	}

	@Transactional
	@Override
	public OrganizationDTO updateOrg(OrganizationDTO org) {
		log.info("updateOrg() ENTERED with args - org");
		log.info("updateOrg() is under execution...");
		Optional<Organization> optOrg = orgRepository.findById(org.getOrgId());
		Organization dbOrg = null;
		if(optOrg.isEmpty()) {
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_ORG_DB_ENTITY_NOTFOUND_CODE, 
					ErrorCodeMessages.ERR_ORG_DB_ENTITY_NOTFOUND_MSG);
		}
		dbOrg = optOrg.get();
		mapper.map(org, dbOrg);
		Organization updatedOrg = orgRepository.save(dbOrg);
		OrganizationDTO orgDTO = new OrganizationDTO();
		mapper.map(updatedOrg, orgDTO);
		log.info("updateOrg() exected successfully");
		return orgDTO;
	}
	@Transactional
	@Override
	public void deleteOrgById(Integer orgId) {
		log.info("deleteOrg() ENTERED : orgId : " + orgId);
		log.info("deleteOrg() is under execution...");
		if (orgId <= 0)
			throw new EmptyInputException(ErrorCodeMessages.ERR_ROLE_ID_IS_EMPTY_CODE,
					ErrorCodeMessages.ERR_ROLE_ID_IS_EMPTY_MSG);
		orgRepository.deleteById(orgId);
		log.info("deleteOrg() executed successfully");
	}

	@Transactional
	@Override
	public void deleteOrgPic(Integer orgId) {
		log.info("deleteOrgPic() Entered !");
		if (orgId <= 0) {
			log.info("deleteOrgPic() EmptyInputException orgId is 0 or empty.");
			throw new EmptyInputException(ErrorCodeMessages.ERR_ROLE_ID_IS_EMPTY_CODE,
					ErrorCodeMessages.ERR_ROLE_ID_IS_EMPTY_MSG);
		}
		Organization dbUser = orgRepository.findByOrgId(orgId);
		if (dbUser == null) {
			log.info("deleteOrgPic() the orgId is not found in the database !");
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_USER_DB_ENTITY_IS_NULL_CODE,
					ErrorCodeMessages.ERR_USER_DB_ENTITY_IS_NULL_MSG);
		}
		dbUser.setOrganizationImage(null);
		OrganizationDTO dto = new OrganizationDTO();
		mapper.map(dbUser, dto);
		updateOrg(dto);
		log.info("deleteOrgPic() execution sucessfull !");
	}
}
		
	


