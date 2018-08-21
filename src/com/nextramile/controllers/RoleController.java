package com.nextramile.controllers;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.apache.shiro.session.Session;

import com.nextramile.dao.RoleDAO;
import com.nextramile.dao.UserDAO;
import com.nextramile.models.Role;
import com.nextramile.models.UserAccount;
//import com.nextramile.utils.CoreClass;
//import com.nextramile.utils.SessionManager;
import com.nextramile.util.AuthManager;

@ManagedBean(name="roleController", eager=true)
@SessionScoped
public class RoleController {
	
	private Role role;
	private List<Role> roleList;
	private UserAccount user;
	
	@PostConstruct
	public void init() {
		role = new Role();
	}
	
	public RoleController() {
		roleList = RoleDAO.getRoleList();
		Session session = AuthManager.getSession();
        user = (UserAccount) session.getAttribute("user");
	}
	
	public String addRole() {
		role.setCreatedBy(user.getUsername());
		role.setCreatedBy("User");
	    RoleDAO.addRole(role);
	    roleList = RoleDAO.getRoleList();
	    role = new Role();
	    return "admin-list-roles.xhtml?faces-redirect=true";
	}
	
	public String updateRole() {
		role.setEditedBy(user.getUsername());
		role.setEditedBy("User");
	    RoleDAO.updateRole(role);
	    roleList = RoleDAO.getRoleList();
	    role = new Role();
	    return "admin-list-roles.xhtml?faces-redirect=true";
	}
	
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public List<Role> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}
}