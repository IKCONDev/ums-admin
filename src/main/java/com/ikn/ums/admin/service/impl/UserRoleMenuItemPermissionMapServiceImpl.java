package com.ikn.ums.admin.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ikn.ums.admin.dto.MenuItemDTO;
import com.ikn.ums.admin.dto.UserRoleMenuItemPermissionMapDTO;
import com.ikn.ums.admin.entity.MenuItem;
import com.ikn.ums.admin.entity.UserRoleMenuItemPermissionMap;
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
		UserRoleMenuItemPermissionMap userRoleMenuItemPermissionMap = new UserRoleMenuItemPermissionMap();
		mapper.map(userRoleMenuItemPermissionMapDTO, userRoleMenuItemPermissionMap);
		userRoleMenuItemPermissionMap.setCreatedDateTime(LocalDateTime.now());
		UserRoleMenuItemPermissionMap createdUserRoleMenuItemPermissionMap = userRoleMenuItemPermissionMapRepository.save(userRoleMenuItemPermissionMap);
		UserRoleMenuItemPermissionMapDTO createdUserRoleMenuItemPermissionMapDTO = new UserRoleMenuItemPermissionMapDTO();
		mapper.map(createdUserRoleMenuItemPermissionMap, createdUserRoleMenuItemPermissionMapDTO);
		return createdUserRoleMenuItemPermissionMapDTO;
	}

	@Transactional(value = TxType.REQUIRED)
	@Override
	public UserRoleMenuItemPermissionMapDTO updateUserRoleMenuItemPermissionMap(
			UserRoleMenuItemPermissionMapDTO userRoleMenuItemPermissionMapDTO) {
		Optional<UserRoleMenuItemPermissionMap> optDbUserRoleMenuItemPermissionMap = userRoleMenuItemPermissionMapRepository.findById(userRoleMenuItemPermissionMapDTO.getId());
		UserRoleMenuItemPermissionMap dbUserRoleMenuItemPermissionMap = optDbUserRoleMenuItemPermissionMap.get();
		dbUserRoleMenuItemPermissionMap.setEmail(userRoleMenuItemPermissionMapDTO.getEmail());
		dbUserRoleMenuItemPermissionMap.setMenuItemIdList(userRoleMenuItemPermissionMapDTO.getMenuItemIdList());
		dbUserRoleMenuItemPermissionMap.setPermissionIdList(userRoleMenuItemPermissionMapDTO.getPermissionIdList());
		dbUserRoleMenuItemPermissionMap.setRoleId(userRoleMenuItemPermissionMapDTO.getRoleId());
		dbUserRoleMenuItemPermissionMap.setModifiedDateTime(LocalDateTime.now());
		UserRoleMenuItemPermissionMapDTO updatedUserRolePermissionMapDTO = new UserRoleMenuItemPermissionMapDTO();
		mapper.map(dbUserRoleMenuItemPermissionMap, updatedUserRolePermissionMapDTO);
		return updatedUserRolePermissionMapDTO;
	}

	@Override
	public List<UserRoleMenuItemPermissionMap> getAllUserRoleMenuItemPermissionMap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserRoleMenuItemPermissionMapDTO> getUserRoleMenuItemPermissionMapsByUserId(String email) {
		List<UserRoleMenuItemPermissionMap> userRoleMenuItemPermissionMapList = userRoleMenuItemPermissionMapRepository.findByEmail(email);
		List<UserRoleMenuItemPermissionMapDTO> dtos = new ArrayList<>();
			userRoleMenuItemPermissionMapList.forEach(entity -> {
				UserRoleMenuItemPermissionMapDTO dto = new UserRoleMenuItemPermissionMapDTO();
				mapper.map(entity, dto);
				MenuItemDTO menuItemDTO = menuItemService.getMenuItemById(Long.parseLong(dto.getMenuItemIdList()));
				dto.setMenuItem(menuItemDTO);
				dtos.add(dto);
			});
			//sort in ascending order using comparator
			dtos.sort(new Comparator<UserRoleMenuItemPermissionMapDTO>() {
				@Override
				public int compare(UserRoleMenuItemPermissionMapDTO o1, UserRoleMenuItemPermissionMapDTO o2) {
					return (int) (o1.getId() - o2.getId());
				}
			});
		return dtos;
	}

	@Transactional
	@Override
	public void deleteAllUserRoleMenuItemPermissionMapByUserId(String email) {
		log.info("deleteAllUserRoleMenuItemPermissionMapByUserId() entered with args : userId/emailId");
		log.info("deleteAllUserRoleMenuItemPermissionMapByUserId() is under execution...");
		userRoleMenuItemPermissionMapRepository.deleteByEmail(email);
		log.info("deleteAllUserRoleMenuItemPermissionMapByUserId() executed succesfully");
	}
	
	@Transactional
	@Override
	public List<UserRoleMenuItemPermissionMapDTO> saveAllUserRoleMenuItemPermissionMaps(
			List<UserRoleMenuItemPermissionMapDTO> userRoleMenuItemPermissionMapDTOList) {
		List<UserRoleMenuItemPermissionMap> entites = new ArrayList<>();
		userRoleMenuItemPermissionMapDTOList.forEach(dto -> {
			UserRoleMenuItemPermissionMap entity = new UserRoleMenuItemPermissionMap();
			mapper.map(dto, entity);
			entity.setCreatedDateTime(LocalDateTime.now());
			entites.add(entity);
		});
		List<UserRoleMenuItemPermissionMap> savedEntities = userRoleMenuItemPermissionMapRepository.saveAll(entites);
		List<UserRoleMenuItemPermissionMapDTO> dtos = new ArrayList<>();
		savedEntities.forEach(entity -> {
			UserRoleMenuItemPermissionMapDTO dto = new UserRoleMenuItemPermissionMapDTO();
			mapper.map(entity, dto);
			dtos.add(dto);
		});
		return dtos;
	}

	@Override
	public List<UserRoleMenuItemPermissionMapDTO> updateAllUserRoleMenuItemPermissionMaps(
			List<UserRoleMenuItemPermissionMapDTO> userRoleMenuItemPermissionMapDTOList) {
		List<UserRoleMenuItemPermissionMap> entites = new ArrayList<>();
		userRoleMenuItemPermissionMapDTOList.forEach(dto -> {
			UserRoleMenuItemPermissionMap entity = new UserRoleMenuItemPermissionMap();
			mapper.map(dto, entity);
			entity.setModifiedDateTime(LocalDateTime.now());
			entites.add(entity);
		});
		//userRoleMenuItemPermissionMapRepository.
		List<UserRoleMenuItemPermissionMap> savedEntities = userRoleMenuItemPermissionMapRepository.saveAll(entites);
		List<UserRoleMenuItemPermissionMapDTO> dtos = new ArrayList<>();
		savedEntities.forEach(entity -> {
			UserRoleMenuItemPermissionMapDTO dto = new UserRoleMenuItemPermissionMapDTO();
			mapper.map(entity, dto);
			dtos.add(dto);
		});
		return dtos;
	}

}
