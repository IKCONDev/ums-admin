package com.ikn.ums.admin.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

	@Override
	public UserRoleMenuItemPermissionMap updateUserRoleMenuItemPermissionMap(
			UserRoleMenuItemPermissionMap userRoleMenuItemPermissionMap) {
		Optional<UserRoleMenuItemPermissionMap> optDbUserRoleMenuItemPermissionMap = userRoleMenuItemPermissionMapRepository.findById(userRoleMenuItemPermissionMap.getId());
		UserRoleMenuItemPermissionMap dbUserRoleMenuItemPermissionMap = optDbUserRoleMenuItemPermissionMap.get();
		dbUserRoleMenuItemPermissionMap.setEmail(userRoleMenuItemPermissionMap.getEmail());
		dbUserRoleMenuItemPermissionMap.setMenuItemIdList(userRoleMenuItemPermissionMap.getMenuItemIdList());
		dbUserRoleMenuItemPermissionMap.setPermissionIdList(userRoleMenuItemPermissionMap.getPermissionIdList());
		dbUserRoleMenuItemPermissionMap.setRoleId(userRoleMenuItemPermissionMap.getRoleId());
		dbUserRoleMenuItemPermissionMap.setModifiedDateTime(LocalDateTime.now());
		return dbUserRoleMenuItemPermissionMap;
	}

	@Override
	public List<UserRoleMenuItemPermissionMap> getAllUserRoleMenuItemPermissionMap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserRoleMenuItemPermissionMap getUserRoleMenuItemPermissionMapByUserId(String email) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteUserRoleMenuItemPermissionMapByUserId(String email) {
		// TODO Auto-generated method stub
		
	}

}
