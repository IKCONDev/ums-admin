package com.ikn.ums.admin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ikn.ums.admin.entity.Organization;
import com.ikn.ums.admin.exception.ControllerException;
import com.ikn.ums.admin.exception.EmptyInputException;
import com.ikn.ums.admin.exception.EntityNotFoundException;
import com.ikn.ums.admin.exception.ErrorCodeMessages;
import com.ikn.ums.admin.service.OrgService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/org")
@Slf4j
public class OrgController {

	
	@Autowired
	private OrgService orgService;
	
	@PostMapping("/save")
	public ResponseEntity<?> createOrg(@RequestBody Organization org) {
		log.info("OrgController.createOrg() entered with args - organization");
		if(org == null || org.equals(null)) {
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_ORG_ENTITY_IS_NULL_CODE,
					ErrorCodeMessages.ERR_ORG_ENTITY_IS_NULL_MSG);
		}
		try {
			log.info("OrgController.createOrg() is under execution...");
			Organization savedOrg = orgService.createOrg(org);
			log.info("OrgController.createOrg() executed successfully.");
			return new ResponseEntity<>(savedOrg, HttpStatus.CREATED);
		} catch (Exception e) {
			log.info("OrgController.createOrg() exited with exception : Exception occured while saving Organization.");
			ControllerException umsCE = new ControllerException(ErrorCodeMessages.ORG_SAVE_UNSUCCESS_CODE,
					ErrorCodeMessages.ORG_SAVE_UNSUCCESS_MSG);
			throw umsCE;
		}
	}
	
	
	@PutMapping("/update")
	public ResponseEntity<?> updateOrg(@RequestBody Organization org) {
		log.info("OrgController.updateOrg() entered with args - org");
		if(org == null || org.equals(null)) {
			log.info("OrgController.updateOrg() EntityNotFoundException : Organization object is null ");
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_ORG_ENTITY_IS_NULL_CODE,
					ErrorCodeMessages.ERR_ORG_ENTITY_IS_NULL_MSG);
		}
		try {
			log.info("OrgController.updateOrg() is under execution...");
			Organization updateOrg = orgService.createOrg(org);
			log.info("OrgController.updateOrg() executed successfully.");
			return new ResponseEntity<>(updateOrg, HttpStatus.CREATED);
		} catch (Exception e) {
			log.info("OrgController.updateOrg() exited with exception : Exception occured while updating Organization.");
			ControllerException umsCE = new ControllerException(ErrorCodeMessages.ERR_ORG_UPDATE_UNSUCCESS_CODE,
					ErrorCodeMessages.ERR_ORG_UPDATE_UNSUCCESS_MSG);
			throw umsCE;
		}
	}
	
	@DeleteMapping("/delete/{orgId}")
	public ResponseEntity<?> deleteOrg(@PathVariable("orgId") Integer orgId){
		boolean isDeleted = false;
		log.info("OrgController.deleteOrgByOrgId() entered with args - orgId");
		if(orgId <= 0 || orgId == null) {
			log.info("OrgController.deleteOrgByOrgId() EmptyInputException : orgId is empty");
			throw new EmptyInputException(ErrorCodeMessages.ERR_ORG_ID_NOT_FOUND_CODE,
					ErrorCodeMessages.ERR_ORG_ID_NOT_FOUND_MSG);
		}
		try {
			log.info("OrgController.deleteOrgByOrgId() is under execution...");
			orgService.deleteOrgById(orgId);
			isDeleted = true;
			log.info("OrgController.deleteOrgByOrgId() executed successfully");
			return new ResponseEntity<>(isDeleted, HttpStatus.OK);
		}catch (Exception e) {
			log.info("OrgController.deleteOrgByOrgId() exited with exception : Exception occured while deleting organization.");
			ControllerException umsCE = new ControllerException(ErrorCodeMessages.ERR_ORG_DELETE_UNSUCCESS_CODE,
			ErrorCodeMessages.ERR_ORG_DELETE_UNSUCCESS_MSG);
			throw umsCE;
		}
	}
	
	@GetMapping("{orgId}")
	public ResponseEntity<?> getOrg(@PathVariable("orgId") Integer orgId){
		log.info("OrgController.getOrg() entered with args - orgId");
		if(orgId <= 0 || orgId == null) {
			log.info("OrgController.getOrg() EmptyInputException : orgId is empty");
			throw new EmptyInputException(ErrorCodeMessages.ERR_ORG_ID_NOT_FOUND_CODE,
					ErrorCodeMessages.ERR_ORG_ID_NOT_FOUND_MSG);
		}
		try {
			log.info("OrgController.getOrg() is under execution...");
			List<Organization> orgList = orgService.getAllOrgs();
			Organization org = null;
			if(orgList.size() == 1) {
				 org = orgList.get(0);
			}
			if(org == null || org.equals(null)) {;
				//return an empty org if no org is present in DB for handling null.
				return new ResponseEntity<>(new Organization(), HttpStatus.OK);
			}
			log.info("OrgController.getOrg() executed successfully");
			return new ResponseEntity<>(org, HttpStatus.OK);
		}catch (Exception e) {
			log.info("OrgController.getOrg() exited with exception : Exception occured while getting organization details.");
			ControllerException umsCE = new ControllerException(ErrorCodeMessages.ERR_ORG_GET_UNSUCCESS_CODE,
			ErrorCodeMessages.ERR_ORG_GET_UNSUCCESS_MSG);
			throw umsCE;
		}
	}
	
}
