package com.ikn.ums.admin.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.filter.GenericFilterBean;

import com.ikn.ums.admin.entity.MenuItem;
import com.ikn.ums.admin.entity.Permission;
import com.ikn.ums.admin.entity.Role;
import com.ikn.ums.admin.service.RoleService;
import com.ikn.ums.admin.service.impl.RoleServiceImpl;

public class MenuItemsAndPermissionsFilter extends GenericFilterBean {
	
//    public MenuItemsAndPermissionsFilter(RoleService roleService) {
//        this.roleService = roleService;
//    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
      
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication != null && authentication.isAuthenticated()) {

			String userName = ((User) authentication.getPrincipal()).getUsername();
			RoleService roleService = new RoleServiceImpl();
			Optional<Role> optRole = roleService.getRoleByName(userName); // TODO: Change this method to getRoleById,
																			// get userId

			Role currentUserRole = optRole.get();

			// Fetch all the menu items and permissions based on the role
			List<MenuItem> menuItems = currentUserRole.getMenuItems();
			Permission permission = currentUserRole.getPermission();

			// Store menu items and permissions in the request or somewhere for later use
			request.setAttribute("menuItems", menuItems);
			request.setAttribute("permission", permission);
		}
		chain.doFilter(request, response);
	}

}
