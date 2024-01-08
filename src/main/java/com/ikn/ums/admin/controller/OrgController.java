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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ikn.ums.admin.dto.OrganizationDTO;
import com.ikn.ums.admin.exception.ControllerException;
import com.ikn.ums.admin.exception.EmptyInputException;
import com.ikn.ums.admin.exception.EntityNotFoundException;
import com.ikn.ums.admin.exception.ErrorCodeMessages;
import com.ikn.ums.admin.exception.ImageNotFoundException;
import com.ikn.ums.admin.service.OrgService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/org")
@Slf4j
public class OrgController {

	@Autowired
	private OrgService orgService;

	@PostMapping("/save")
	public ResponseEntity<OrganizationDTO> createOrg(@RequestBody OrganizationDTO org) {
		log.info("OrgController.createOrg() entered with args - organization");
		if (org == null) {
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_ORG_ENTITY_IS_NULL_CODE,
					ErrorCodeMessages.ERR_ORG_ENTITY_IS_NULL_MSG);
		}
		try {
			log.info("OrgController.createOrg() is under execution...");
			OrganizationDTO savedOrg = orgService.createOrg(org);
			log.info("OrgController.createOrg() executed successfully.");
			return new ResponseEntity<>(savedOrg, HttpStatus.CREATED);
		} catch (EntityNotFoundException businessException) {
			log.error("OrgController.createOrg() exited with exception : Exception occured while saving Organization."
					+ businessException.getMessage(), businessException);
			throw businessException;
		} catch (Exception e) {
			log.error("OrgController.createOrg() exited with exception : Exception occured while saving Organization."
					+ e.getMessage(), e);
			throw new ControllerException(e.getCause().toString(), e.getMessage());
		}
	}

	@PutMapping("/update")
	public ResponseEntity<OrganizationDTO> updateOrg(@RequestBody OrganizationDTO org) {
		log.info("OrgController.updateOrg() entered with args - org");
		if (org == null || org.equals(null)) {
			log.info("OrgController.updateOrg() EntityNotFoundException : Organization object is null ");
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_ORG_ENTITY_IS_NULL_CODE,
					ErrorCodeMessages.ERR_ORG_ENTITY_IS_NULL_MSG);
		}
		try {
			log.info("OrgController.updateOrg() is under execution...");
			OrganizationDTO updateOrg = orgService.createOrg(org);
			log.info("OrgController.updateOrg() executed successfully.");
			return new ResponseEntity<>(updateOrg, HttpStatus.CREATED);
		} catch (EntityNotFoundException businessException) {
			log.error("OrgController.updateOrg() exited with exception : Exception occured while saving Organization."
					+ businessException.getMessage(), businessException);
			throw businessException;
		} catch (Exception e) {
			log.error("OrgController.updateOrg() exited with exception : Exception occured while updating Organization."
					+ e.getMessage(), e);
			throw new ControllerException(ErrorCodeMessages.ERR_ORG_UPDATE_UNSUCCESS_CODE,
					ErrorCodeMessages.ERR_ORG_UPDATE_UNSUCCESS_MSG);
		}
	}

	@DeleteMapping("/delete/{orgId}")
	public ResponseEntity<Boolean> deleteOrg(@PathVariable("orgId") Integer orgId) {
		boolean isDeleted = false;
		log.info("OrgController.deleteOrgByOrgId() entered with args - orgId");
		if (orgId <= 0 || orgId == null) {
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
		} catch (EmptyInputException businessException) {
			log.error(
					"OrgController.deleteOrgByOrgId() exited with exception : Exception occured while saving Organization."
							+ businessException.getMessage(),businessException);
			throw businessException;
		} catch (Exception e) {
			log.error("OrgController.deleteOrgByOrgId() exited with exception : Exception occured while deleting organization."
							+ e.getMessage(),e);
			throw new ControllerException(ErrorCodeMessages.ERR_ORG_DELETE_UNSUCCESS_CODE,
					ErrorCodeMessages.ERR_ORG_DELETE_UNSUCCESS_MSG);
		}
	}

	@GetMapping("{orgId}")
	public ResponseEntity<OrganizationDTO> getOrg(@PathVariable("orgId") Integer orgId) {
		log.info("OrgController.getOrg() entered with args - orgId");
		if (orgId <= 0 || orgId == null) {
			log.info("OrgController.getOrg() EmptyInputException : orgId is empty");
			throw new EmptyInputException(ErrorCodeMessages.ERR_ORG_ID_NOT_FOUND_CODE,
					ErrorCodeMessages.ERR_ORG_ID_NOT_FOUND_MSG);
		}
		try {
			log.info("OrgController.getOrg() is under execution...");
			List<OrganizationDTO> orgList = orgService.getAllOrgs();
			OrganizationDTO org = new OrganizationDTO();
			if (orgList.size() == 1) {
				org = orgList.get(0);
				return new ResponseEntity<>(org, HttpStatus.OK);
			}
			log.info("OrgController.getOrg() executed successfully");
			return new ResponseEntity<>(org, HttpStatus.OK);
		} catch (EmptyInputException businessException) {
			log.error("OrgController.getOrg() exited with exception : Exception occured while saving Organization."+ businessException.getMessage(), businessException);
			throw businessException;
		} catch (Exception e) {
			log.error(
					"OrgController.getOrg() exited with exception : Exception occured while getting organization details."
							+ e.getMessage(),e);
			throw new ControllerException(ErrorCodeMessages.ERR_ORG_GET_UNSUCCESS_CODE,
					ErrorCodeMessages.ERR_ORG_GET_UNSUCCESS_MSG);
		}
	}

	@PostMapping("/saveOrgPic")
	public ResponseEntity<OrganizationDTO> updateOrgPic(@RequestParam MultipartFile orgPic) {
		log.info("OrgController.updateOrgPic() entered with args - orgpic");
		if (orgPic == null) {
			log.info("OrgController.updateOrgPic() ImageNotFoundException : Multipart object is obtained is null.");
			throw new ImageNotFoundException(ErrorCodeMessages.ERR_ORG_IMAGE_NULL_CODE,
					ErrorCodeMessages.ERR_ORG_IMAGE_NULL_MSG);
		}
		OrganizationDTO org = null;
		try {
			log.info("OrgController.getOrg() is under execution...");
			List<OrganizationDTO> orgList = orgService.getAllOrgs();
			if (orgList.size() == 1) {
				org = orgList.get(0);
			}
			org.setOrganizationImage(orgPic.getBytes());
			updateOrg(org);
			log.info("OrgController.updateOrgPic() executed successfully");
			return new ResponseEntity<>(org, HttpStatus.OK);
		} catch (Exception e) {
			throw new ControllerException(ErrorCodeMessages.ERR_ORG_PROFILE_UPLOAD_UNSUCCESS_CODE,
					ErrorCodeMessages.ERR_ORG_PROFILE_UPLOAD_UNSUCCESS_MSG);
		}

	}

	@DeleteMapping("/deleteOrgPic")
	public ResponseEntity<Boolean> deleteOrgPic(@RequestParam Integer orgId) {
		log.info("OrgController.deleteOrgPic() entered with org - email");
		if (orgId <= 0 || orgId == null) {
			log.info("OrgController.deleteOrgPic() EmptyInputException : orgId is empty");
			throw new EmptyInputException(ErrorCodeMessages.ERR_ORG_ID_NOT_FOUND_CODE,
					ErrorCodeMessages.ERR_ORG_ID_NOT_FOUND_MSG);
		}
		try {
			log.info("OrgController.deleteOrgPic() is under execution...");
			orgService.deleteOrgPic(orgId);
			log.info("OrgController.deleteOrgPic() executed succesfully");
			return new ResponseEntity<>(Boolean.TRUE,HttpStatus.OK);
		} catch (EmptyInputException businessException) {
			log.error(
					"OrgController.deleteOrgPic() exited with exception : Exception occured while saving Organization."
							+ businessException.getMessage(),businessException);
			throw businessException;
		} catch (Exception e) {
			log.error("OrgController.deleteOrgPic() : Exception Occurred." + e.getMessage(), e);
			throw new ControllerException(ErrorCodeMessages.ERR_USER_DELETE_PROFILEPIC_UNSUCCESS_CODE,
					ErrorCodeMessages.ERR_USER_DELETE_PROFILEPIC_UNSUCCESS_MSG);
		}

	}

}
