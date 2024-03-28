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
		log.info("createOrg() entered with args - organization");
		if (org == null) {
			log.info("updateOrg() EntityNotFoundException : Organization object is null or empty");
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_ORG_ENTITY_IS_NULL_CODE,
					ErrorCodeMessages.ERR_ORG_ENTITY_IS_NULL_MSG);
		}
		try {
			log.info("createOrg() is under execution...");
			var savedOrg = orgService.createOrg(org);
			log.info("createOrg() executed successfully.");
			return new ResponseEntity<>(savedOrg, HttpStatus.CREATED);
		} catch (EntityNotFoundException businessException) {
			log.error("createOrg() exited with exception : Exception occured while saving Organization."
					+ businessException.getMessage(), businessException);
			throw businessException;
		} catch (Exception e) {
			log.error("createOrg() exited with exception : Exception occured while saving Organization."
					+ e.getMessage(), e);
			throw new ControllerException(e.getCause().toString(), e.getMessage());
		}
	}

	@PutMapping("/update")
	public ResponseEntity<OrganizationDTO> updateOrg(@RequestBody OrganizationDTO org) {
		log.info("updateOrg() entered with args - org");
		if (org == null || org.equals(null)) {
			log.info("updateOrg() EntityNotFoundException : Organization object is null or empty");
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_ORG_ENTITY_IS_NULL_CODE,
					ErrorCodeMessages.ERR_ORG_ENTITY_IS_NULL_MSG);
		}
		try {
			log.info("updateOrg() is under execution...");
			var updateOrg = orgService.createOrg(org);
			log.info("updateOrg() executed successfully.");
			return new ResponseEntity<>(updateOrg, HttpStatus.CREATED);
		} catch (EntityNotFoundException businessException) {
			log.error("updateOrg() exited with exception : Exception occured while saving Organization."
					+ businessException.getMessage(), businessException);
			throw businessException;
		} catch (Exception e) {
			log.error("updateOrg() exited with exception : Exception occured while updating Organization."
					+ e.getMessage(), e);
			throw new ControllerException(ErrorCodeMessages.ERR_ORG_UPDATE_UNSUCCESS_CODE,
					ErrorCodeMessages.ERR_ORG_UPDATE_UNSUCCESS_MSG);
		}
	}

	@DeleteMapping("/delete/{orgId}")
	public ResponseEntity<Boolean> deleteOrg(@PathVariable("orgId") Integer orgId) {
		log.info("deleteOrgByOrgId() entered with args - orgId");
		if (orgId <= 0 || orgId == null) {
			log.info("deleteOrgByOrgId() EmptyInputException : orgId is empty");
			throw new EmptyInputException(ErrorCodeMessages.ERR_ORG_ID_NOT_FOUND_CODE,
					ErrorCodeMessages.ERR_ORG_ID_NOT_FOUND_MSG);
		}
		try {
			log.info("deleteOrgByOrgId() is under execution...");
			orgService.deleteOrgById(orgId);
			log.info("deleteOrgByOrgId() executed successfully");
			return new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);
		} catch (EmptyInputException businessException) {
			log.error(
					"deleteOrgByOrgId() exited with exception : Exception occured while saving Organization."
							+ businessException.getMessage(),businessException);
			throw businessException;
		} catch (Exception e) {
			log.error("deleteOrgByOrgId() exited with exception : Exception occured while deleting organization."
							+ e.getMessage(),e);
			throw new ControllerException(ErrorCodeMessages.ERR_ORG_DELETE_UNSUCCESS_CODE,
					ErrorCodeMessages.ERR_ORG_DELETE_UNSUCCESS_MSG);
		}
	}

	@GetMapping("{orgId}")
	public ResponseEntity<OrganizationDTO> getOrg(@PathVariable("orgId") Integer orgId) {
		log.info("getOrg() entered with args - orgId");
		if (orgId <= 0 || orgId == null) {
			log.info("getOrg() EmptyInputException : orgId is empty");
			throw new EmptyInputException(ErrorCodeMessages.ERR_ORG_ID_NOT_FOUND_CODE,
					ErrorCodeMessages.ERR_ORG_ID_NOT_FOUND_MSG);
		}
		try {
			log.info("getOrg() is under execution...");
			var orgList = orgService.getAllOrgs();
			OrganizationDTO org = new OrganizationDTO();
			if (orgList.size() == 1) {
				org = orgList.get(0);
				return new ResponseEntity<>(org, HttpStatus.OK);
			}
			log.info("getOrg() executed successfully");
			return new ResponseEntity<>(org, HttpStatus.OK);
		} catch (EmptyInputException businessException) {
			log.error("getOrg() exited with exception : Exception occured while saving Organization."+ businessException.getMessage(), businessException);
			throw businessException;
		} catch (Exception e) {
			log.error(
					"getOrg() exited with exception : Exception occured while getting organization details."
							+ e.getMessage(),e);
			throw new ControllerException(ErrorCodeMessages.ERR_ORG_GET_UNSUCCESS_CODE,
					ErrorCodeMessages.ERR_ORG_GET_UNSUCCESS_MSG);
		}
	}

	@PostMapping("/saveOrgPic")
	public ResponseEntity<OrganizationDTO> updateOrgPic(@RequestParam MultipartFile orgPic) {
		log.info("updateOrgPic() entered with args - orgpic");
		if (orgPic == null) {
			log.info("updateOrgPic() ImageNotFoundException : Multipart object is obtained is null.");
			throw new ImageNotFoundException(ErrorCodeMessages.ERR_ORG_IMAGE_NULL_CODE,
					ErrorCodeMessages.ERR_ORG_IMAGE_NULL_MSG);
		}
		OrganizationDTO org = null;
		try {
			log.info("getOrg() is under execution...");
			List<OrganizationDTO> orgList = orgService.getAllOrgs();
			if (orgList.size() == 1) {
				org = orgList.get(0);
			}
			
			orgService.updateOrgPic(org,orgPic);
			log.info("updateOrgPic() executed successfully");
			return new ResponseEntity<>(org, HttpStatus.OK);
		} catch (Exception e) {
			log.error(
					"updateOrgPic() exited with exception : Exception occured while updating Organization profile picture."
							+ e.getMessage(),e);
			throw new ControllerException(ErrorCodeMessages.ERR_ORG_PROFILE_UPLOAD_UNSUCCESS_CODE,
					ErrorCodeMessages.ERR_ORG_PROFILE_UPLOAD_UNSUCCESS_MSG);
		}

	}

	@DeleteMapping("/deleteOrgPic")
	public ResponseEntity<Boolean> deleteOrgPic(@RequestParam Integer orgId) {
		log.info("deleteOrgPic() entered with org - email");
		if (orgId <= 0 || orgId == null) {
			log.info("deleteOrgPic() EmptyInputException : orgId is empty");
			throw new EmptyInputException(ErrorCodeMessages.ERR_ORG_ID_NOT_FOUND_CODE,
					ErrorCodeMessages.ERR_ORG_ID_NOT_FOUND_MSG);
		}
		try {
			log.info("deleteOrgPic() is under execution...");
			orgService.deleteOrgPic(orgId);
			log.info("deleteOrgPic() executed succesfully");
			return new ResponseEntity<>(Boolean.TRUE,HttpStatus.OK);
		} catch (EmptyInputException businessException) {
			log.error(
					"deleteOrgPic() exited with exception : Exception occured while saving Organization."
							+ businessException.getMessage(),businessException);
			throw businessException;
		} catch (Exception e) {
			log.error("deleteOrgPic() : Exception Occurred." + e.getMessage(), e);
			throw new ControllerException(ErrorCodeMessages.ERR_USER_DELETE_PROFILEPIC_UNSUCCESS_CODE,
					ErrorCodeMessages.ERR_USER_DELETE_PROFILEPIC_UNSUCCESS_MSG);
		}

	}

}
