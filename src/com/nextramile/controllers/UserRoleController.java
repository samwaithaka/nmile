package com.nextramile.controllers;

import java.util.List;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.nextramile.dao.RoleDAO;
import com.nextramile.dao.UserRoleDAO;
import com.nextramile.models.Role;
import com.nextramile.models.UserAccount;
import com.nextramile.models.UserRole;

@ManagedBean(name="userRoleController", eager=true)
@SessionScoped
public class UserRoleController implements Serializable {
	
	private UserRole userRole;
	private String userRoleAction;
	private UserAccount user;
	private int[] userListOption;
	private List<Role> activeRoles;
	private List<Role> inActiveRoles;

	/**
	 * 	
	 */	
	private static final long serialVersionUID = 1L;
	
	@PostConstruct
	public void init() {
		user = new UserAccount();
		userRole = new UserRole();
		userRole.setUserAccount(new UserAccount());
		userRole.setRole(new Role());
    }
	
	public UserRoleController() {								
	}
	
	public String assign() {
		for(int option : userListOption) {
			Role role = RoleDAO.find(option);
			userRole = new UserRole();
			userRole.setRole(role);
			userRole.setUserAccount(user);
			userRole = UserRoleDAO.addUserRole(userRole);
		}
		return null;
	}
	
	public String remove() {
		for(int option : userListOption) {
			Role role = RoleDAO.find(option);
			userRole = new UserRole();
			userRole.setRole(role);
			userRole.setUserAccount(user);
			UserRoleDAO.deleteUserRole(userRole);
		}
		return null;
	}

	public List<Role> getActiveRoles() {
		activeRoles = UserRoleDAO.getActiveRoles(user);
		return activeRoles;
	}
	
	public void setActiveRoles(List<Role> activeRoles) {
		this.activeRoles = activeRoles;
	}
	
	public List<Role> getInActiveRoles() {
		inActiveRoles = UserRoleDAO.getInActiveRoles(user);
		return inActiveRoles;
	}

	public void setInActiveRoles(List<Role> inActiveRoles) {
		this.inActiveRoles = inActiveRoles;
	}

	public int[] getUserListOption() {
		return userListOption;
	}
	
	public void setUserListOption(int[] userListOption) {
		this.userListOption = userListOption;
	}
		
	public UserAccount getUserAccount() {
		return user;
	}
	
	public void setUserAccount(UserAccount user) {
		this.user = user;
	}
	
	public UserRole getUserRole() {
		return userRole;
	}
	
	public void setUserRole(UserRole userRole) {
		this.userRole = userRole;
	}
	
	public void setUserRoleAction(String userRoleAction) {
		this.userRoleAction = userRoleAction;
	}

	public String getUserRoleAction() {
		return userRoleAction;
	}
}