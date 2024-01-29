package com.ikn.ums.admin.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;
import com.ikn.ums.admin.dto.MenuItemDTO;
import com.ikn.ums.admin.dto.UserRoleMenuItemPermissionMapDTO;
import com.ikn.ums.admin.entity.UserRoleMenuItemPermissionMap;
import com.ikn.ums.admin.exception.EmptyInputException;
import com.ikn.ums.admin.exception.EntityNotFoundException;
import com.ikn.ums.admin.exception.ErrorCodeMessages;
import com.ikn.ums.admin.repository.UserRoleMenuItemPermissionMapRepository;
import com.ikn.ums.admin.service.MenuItemService;
import com.ikn.ums.admin.service.UserRoleMenuItemPermissionMapService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserRoleMenuItemPermissionMapServiceImpl implements UserRoleMenuItemPermissionMapService {
	
	@Autowired
	private UserRoleMenuItemPermissionMapRepository userRoleMenuItemPermissionMapRepository;
	
	@Autowired
	private MenuItemService menuItemService;
	
	@Autowired
	private ModelMapper mapper;

	@Transactional(value = TxType.REQUIRED)
	@Override
	public UserRoleMenuItemPermissionMapDTO createUserRoleMenuItemPermissionMap(
			UserRoleMenuItemPermissionMapDTO userRoleMenuItemPermissionMapDTO) {
		
		log.info(" createUserRoleMenuItemPermissionMap() Entered !");
		UserRoleMenuItemPermissionMap userRoleMenuItemPermissionMap = new UserRoleMenuItemPermissionMap();

		if (userRoleMenuItemPermissionMapDTO == null) {
			log.info(" createUserRoleMenuItemPermissionMap() : userRoleMenuItemPermissionMapDTO - Object is null !");
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_USER_ROLE_MENU_PER_IS_NULL_CODE,
					ErrorCodeMessages.ERR_USER_ROLE_MENU_PER_IS_NULL_MSG);
		}
		log.info(" createUserRoleMenuItemPermissionMap() is under execution...");
		mapper.map(userRoleMenuItemPermissionMapDTO, userRoleMenuItemPermissionMap);
		userRoleMenuItemPermissionMap.setCreatedDateTime(LocalDateTime.now());

		log.info(" createUserRoleMenuItemPermissionMap() : creating user role menuitem permission map started !");

		UserRoleMenuItemPermissionMap createdUserRoleMenuItemPermissionMap = userRoleMenuItemPermissionMapRepository
				.save(userRoleMenuItemPermissionMap);
		log.info(": createUserRoleMenuItemPermissionMap() : creating user role menuitem permission map executed sucessfully !");
		UserRoleMenuItemPermissionMapDTO createdUserRoleMenuItemPermissionMapDTO = new UserRoleMenuItemPermissionMapDTO();
		mapper.map(createdUserRoleMenuItemPermissionMap, createdUserRoleMenuItemPermissionMapDTO);
		log.info(" createUserRoleMenuItemPermissionMap() executed successfully");
		return createdUserRoleMenuItemPermissionMapDTO;
		
	}

	@Transactional(value = TxType.REQUIRED)
	@Override
	public UserRoleMenuItemPermissionMapDTO updateUserRoleMenuItemPermissionMap(
			UserRoleMenuItemPermissionMapDTO userRoleMenuItemPermissionMapDTO) {
		
		log.info(" updateUserRoleMenuItemPermissionMap() Entered !");
		
		if (userRoleMenuItemPermissionMapDTO == null) {
			log.info(" updateUserRoleMenuItemPermissionMap() : userRoleMenuItemPermissionMapDTO - Object is null !");
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_USER_ROLE_MENU_PER_IS_NULL_CODE,
					ErrorCodeMessages.ERR_USER_ROLE_MENU_PER_IS_NULL_MSG);
		}
		log.info(" updateUserRoleMenuItemPermissionMap() is under execution...");
		Optional<UserRoleMenuItemPermissionMap> optDbUserRoleMenuItemPermissionMap = userRoleMenuItemPermissionMapRepository.findById(userRoleMenuItemPermissionMapDTO.getId());
		if(optDbUserRoleMenuItemPermissionMap.isEmpty()) {
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_ROLE_DB_USERRMP_ENTITY_NOTFOUND_CODE, 
					ErrorCodeMessages.ERR_ROLE_DB_USERRMP_ENTITY_NOTFOUND_MSG);
		}
		UserRoleMenuItemPermissionMap dbUserRoleMenuItemPermissionMap = optDbUserRoleMenuItemPermissionMap.get();
		
		if (dbUserRoleMenuItemPermissionMap == null) {
			log.info(" updateUserRoleMenuItemPermissionMap() : dbUserRoleMenuItemPermissionMap - Object is null !");
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_USER_ROLE_MENU_PER_IS_NULL_CODE,
					ErrorCodeMessages.ERR_USER_ROLE_MENU_PER_IS_NULL_MSG);
		}

		log.info(" updateUserRoleMenuItemPermissionMap() : user id : " + userRoleMenuItemPermissionMapDTO.getEmail());
		log.info(" updateUserRoleMenuItemPermissionMap() : role id : " + userRoleMenuItemPermissionMapDTO.getRoleId());
		log.info(" updateUserRoleMenuItemPermissionMap() : menuitems : " + userRoleMenuItemPermissionMapDTO.getMenuItemIdList());
		log.info(" updateUserRoleMenuItemPermissionMap() : permission : " + userRoleMenuItemPermissionMapDTO.getPermissionIdList());
		
		dbUserRoleMenuItemPermissionMap.setEmail(userRoleMenuItemPermissionMapDTO.getEmail());
		dbUserRoleMenuItemPermissionMap.setMenuItemIdList(userRoleMenuItemPermissionMapDTO.getMenuItemIdList());
		dbUserRoleMenuItemPermissionMap.setPermissionIdList(userRoleMenuItemPermissionMapDTO.getPermissionIdList());
		dbUserRoleMenuItemPermissionMap.setRoleId(userRoleMenuItemPermissionMapDTO.getRoleId());
		dbUserRoleMenuItemPermissionMap.setModifiedDateTime(LocalDateTime.now());
		UserRoleMenuItemPermissionMapDTO updatedUserRolePermissionMapDTO = new UserRoleMenuItemPermissionMapDTO();
		mapper.map(dbUserRoleMenuItemPermissionMap, updatedUserRolePermissionMapDTO);
		log.info(" updateUserRoleMenuItemPermissionMap() executed successfully");
		return updatedUserRolePermissionMapDTO;
	}

	@Override
	public List<UserRoleMenuItemPermissionMapDTO> getAllUserRoleMenuItemPermissionMap() {
		log.info("getAllUserRoleMenuItemPermissionMap() entered with no args");
		log.info("getAllUserRoleMenuItemPermissionMap() is under execution...");
		List<UserRoleMenuItemPermissionMapDTO> userRPMDTOMap = new ArrayList<>();
		List<UserRoleMenuItemPermissionMap> userRPMEntityMap = userRoleMenuItemPermissionMapRepository.findAll();
		userRPMEntityMap.forEach(entity -> {
			UserRoleMenuItemPermissionMapDTO  dto = new UserRoleMenuItemPermissionMapDTO();
			mapper.map(entity, dto);
			userRPMDTOMap.add(dto);
		});
		log.info("getAllUserRoleMenuItemPermissionMap() executed successfully.");
		return userRPMDTOMap;
	}

	@Override
	public List<UserRoleMenuItemPermissionMapDTO> getUserRoleMenuItemPermissionMapsByUserId(String email) {
		log.info("getUserRoleMenuItemPermissionMapsByUserId() Entered : email : " + email);
		if (Strings.isNullOrEmpty(email) || email.isEmpty()) {
			log.info("getUserRoleMenuItemPermissionMapsByUserId() : The email is null or empty !");
			throw new EmptyInputException(ErrorCodeMessages.ERR_USER_ROLE_MENU_PER_EMAIL_ID_IS_NULL_CODE,
					ErrorCodeMessages.ERR_USER_ROLE_MENU_PER_EMAIL_ID_IS_NULL_MSG);
		}
		log.info("getUserRoleMenuItemPermissionMapsByUserId() is under execution...");
		List<UserRoleMenuItemPermissionMap> userRoleMenuItemPermissionMapList = userRoleMenuItemPermissionMapRepository.findByEmail(email);
		log.info(" getUserRoleMenuItemPermissionMapsByUserId() : userRoleMenuItemPermissionMapList Size : " + userRoleMenuItemPermissionMapList.size());
		List<UserRoleMenuItemPermissionMapDTO> dtos = new ArrayList<>();
			userRoleMenuItemPermissionMapList.forEach(entity -> {
				UserRoleMenuItemPermissionMapDTO dto = new UserRoleMenuItemPermissionMapDTO();
				mapper.map(entity, dto);
				MenuItemDTO menuItemDTO = menuItemService.getMenuItemById(Long.parseLong(dto.getMenuItemIdList()));
				dto.setMenuItem(menuItemDTO);
				dtos.add(dto);
			});
			//sort in ascending order using comparator
			dtos.sort((o1, o2) -> (int) (o1.getId() - o2.getId()));
			log.info("getUserRoleMenuItemPermissionMapsByUserId() executed successfully.");
		return dtos;
	}

	@Transactional
	@Override
	public void deleteAllUserRoleMenuItemPermissionMapByUserId(String email) {
		
		if (Strings.isNullOrEmpty(email) || email.isEmpty()) {
			log.info("deleteAllUserRoleMenuItemPermissionMapByUserId() : The email is null or empty !");
			throw new EmptyInputException(ErrorCodeMessages.ERR_USER_ROLE_MENU_PER_EMAIL_ID_IS_NULL_CODE,
					ErrorCodeMessages.ERR_USER_ROLE_MENU_PER_EMAIL_ID_IS_NULL_MSG);
		}
		log.info("deleteAllUserRoleMenuItemPermissionMapByUserId() Entered : email : " + email);
		log.info("deleteAllUserRoleMenuItemPermissionMapByUserId() is under execution...");
		userRoleMenuItemPermissionMapRepository.deleteByEmail(email);
		log.info("deleteAllUserRoleMenuItemPermissionMapByUserId() executed succesfully");
	}
	
	@Transactional
	@Override
	public List<UserRoleMenuItemPermissionMapDTO> saveAllUserRoleMenuItemPermissionMaps(
			List<UserRoleMenuItemPermissionMapDTO> userRoleMenuItemPermissionMapDTOList) {
		
		log.info(" saveAllUserRoleMenuItemPermissionMaps() Entered !");

		if (userRoleMenuItemPermissionMapDTOList.size() == 0) {
			log.info(" saveAllUserRoleMenuItemPermissionMaps() : userRoleMenuItemPermissionMapDTOList - List is empty !");
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_USER_ROLE_MENU_PER_LIST_IS_EMPTY_CODE,
					ErrorCodeMessages.ERR_USER_ROLE_MENU_PER_LIST_IS_EMPTY_MSG);
		}

		log.info(" saveAllUserRoleMenuItemPermissionMaps() Size : " + userRoleMenuItemPermissionMapDTOList.size());

		List<UserRoleMenuItemPermissionMap> entites = new ArrayList<>();
		userRoleMenuItemPermissionMapDTOList.forEach(dto -> {
			UserRoleMenuItemPermissionMap entity = new UserRoleMenuItemPermissionMap();
			mapper.map(dto, entity);
			entity.setCreatedDateTime(LocalDateTime.now());
			entites.add(entity);
		});
		
		log.info("saveAllUserRoleMenuItemPermissionMaps() is under execution...");
		List<UserRoleMenuItemPermissionMap> savedEntities = userRoleMenuItemPermissionMapRepository.saveAll(entites);
		List<UserRoleMenuItemPermissionMapDTO> dtos = new ArrayList<>();
		savedEntities.forEach(entity -> {
			UserRoleMenuItemPermissionMapDTO dto = new UserRoleMenuItemPermissionMapDTO();
			mapper.map(entity, dto);
			dtos.add(dto);
		});
		log.info("saveAllUserRoleMenuItemPermissionMaps() executed succesfully");
		return dtos;
	}

	@Override
	public List<UserRoleMenuItemPermissionMapDTO> updateAllUserRoleMenuItemPermissionMaps(
			List<UserRoleMenuItemPermissionMapDTO> userRoleMenuItemPermissionMapDTOList) {
		log.info(" updateAllUserRoleMenuItemPermissionMaps() Entered !");
		if (userRoleMenuItemPermissionMapDTOList.size() == 0) {
			log.info(" updateAllUserRoleMenuItemPermissionMaps() : userRoleMenuItemPermissionMapDTOList - List is empty !");
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_USER_ROLE_MENU_PER_LIST_IS_EMPTY_CODE,
					ErrorCodeMessages.ERR_USER_ROLE_MENU_PER_LIST_IS_EMPTY_MSG);
		}

		log.info(" updateAllUserRoleMenuItemPermissionMaps() Size : " + userRoleMenuItemPermissionMapDTOList.size());
		List<UserRoleMenuItemPermissionMap> entites = new ArrayList<>();
		userRoleMenuItemPermissionMapDTOList.forEach(dto -> {
			UserRoleMenuItemPermissionMap entity = new UserRoleMenuItemPermissionMap();
			mapper.map(dto, entity);
			entity.setModifiedDateTime(LocalDateTime.now());
			entites.add(entity);
		});
		log.info("updateAllUserRoleMenuItemPermissionMaps() is under execution...");
		//userRoleMenuItemPermissionMapRepository.
		List<UserRoleMenuItemPermissionMap> savedEntities = userRoleMenuItemPermissionMapRepository.saveAll(entites);		
		List<UserRoleMenuItemPermissionMapDTO> dtos = new ArrayList<>();
		savedEntities.forEach(entity -> {
			UserRoleMenuItemPermissionMapDTO dto = new UserRoleMenuItemPermissionMapDTO();
			mapper.map(entity, dto);
			dtos.add(dto);
		});
		log.info("updateAllUserRoleMenuItemPermissionMaps() executed succesfully");
		return dtos;
	}

}
