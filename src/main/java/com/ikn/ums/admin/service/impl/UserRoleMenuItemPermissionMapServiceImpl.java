package com.ikn.ums.admin.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ikn.ums.admin.dto.UserRoleMenuItemPermissionMapDTO;
import com.ikn.ums.admin.entity.UserRoleMenuItemPermissionMap;
import com.ikn.ums.admin.repository.UserRoleMenuItemPermissionMapRepository;
import com.ikn.ums.admin.service.UserRoleMenuItemPermissionMapService;

@Service
public class UserRoleMenuItemPermissionMapServiceImpl implements UserRoleMenuItemPermissionMapService {
	
	@Autowired
	private UserRoleMenuItemPermissionMapRepository userRoleMenuItemPermissionMapRepository;
	
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
		Optional<UserRoleMenuItemPermissionMap> optDbUserRoleMenuItemPermissionMap = userRoleMenuItemPermissionMapRepository.findByEmail(userRoleMenuItemPermissionMapDTO.getEmail());
		UserRoleMenuItemPermissionMap dbUserRoleMenuItemPermissionMap = optDbUserRoleMenuItemPermissionMap.get();
		dbUserRoleMenuItemPermissionMap.setEmail(userRoleMenuItemPermissionMapDTO.getEmail());
		dbUserRoleMenuItemPermissionMap.setMenuItemIdList(userRoleMenuItemPermissionMapDTO.getMenuItemIdList());
		dbUserRoleMenuItemPermissionMap.setPermissionIdList(userRoleMenuItemPermissionMapDTO.getPermissionIdList());
		dbUserRoleMenuItemPermissionMap.setRoleId(userRoleMenuItemPermissionMapDTO.getRoleId());
		dbUserRoleMenuItemPermissionMap.setModifiedDateTime(LocalDateTime.now());
		UserRoleMenuItemPermissionMapDTO updateUserRolePermissionMapDTO = new UserRoleMenuItemPermissionMapDTO();
		mapper.map(dbUserRoleMenuItemPermissionMap, updateUserRolePermissionMapDTO);
		return updateUserRolePermissionMapDTO;
	}

	@Override
	public List<UserRoleMenuItemPermissionMap> getAllUserRoleMenuItemPermissionMap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserRoleMenuItemPermissionMapDTO getUserRoleMenuItemPermissionMapByUserId(String email) {
		UserRoleMenuItemPermissionMap userRoleMenuItemPermissionMap = null;
		UserRoleMenuItemPermissionMapDTO userRoleMenuItemPermissionMapDTO = null;
		Optional<UserRoleMenuItemPermissionMap> optUserRoleMenuItemPermissionMap = userRoleMenuItemPermissionMapRepository.findByEmail(email);
		if(optUserRoleMenuItemPermissionMap.isPresent()) {
			userRoleMenuItemPermissionMap = optUserRoleMenuItemPermissionMap.get();
			userRoleMenuItemPermissionMapDTO = new UserRoleMenuItemPermissionMapDTO();
		}
		mapper.map(userRoleMenuItemPermissionMap, userRoleMenuItemPermissionMapDTO);
		return userRoleMenuItemPermissionMapDTO;
	}

	@Transactional(value = TxType.REQUIRED)
	@Override
	public void deleteUserRoleMenuItemPermissionMapByUserId(String email) {
		
	}

}
