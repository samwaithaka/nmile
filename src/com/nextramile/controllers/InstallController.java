package com.nextramile.controllers;

import java.io.File;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.nextramile.dao.RoleDAO;
import com.nextramile.dao.UserDAO;
import com.nextramile.dao.UserRoleDAO;
import com.nextramile.models.Role;
import com.nextramile.models.UserAccount;
import com.nextramile.models.UserRole;
import com.nextramile.util.Configs;
import com.nextramile.util.Emailer;
//import com.nextramile.util.PasswordEncryptor;

@ManagedBean(name="installController", eager=true)
@SessionScoped
public class InstallController {
	
	private UserAccount user;
	private Role role;
	private UserRole userRole;
	private String adminEmail;
	private String adminUser;
	
	@PostConstruct
	public void init() {
		user = new UserAccount();
		role = new Role();
		userRole = new UserRole();
	}
	
	public InstallController() {
	}
	
	public String install() {
		if(UserDAO.getUserList().size() == 0) {
			user.setUsername(adminUser);
			user.setEmailAddress(adminEmail);
			String password = Long.toHexString(Double.doubleToLongBits(Math.random())).substring(0,8);
			//user.setPassword(PasswordEncryptor.encryptPassword(password));
			user.setActive(true);
			user.setResetFlag(true);
			user.setCreatedBy("process");
			user.setEditedBy("process");
			user = UserDAO.addUser(user);
					
			role.setActive(true);
			role.setRoleName("Admin");
			role = RoleDAO.addRole(role);
			
			userRole.setUserAccount(user);
			userRole.setRole(role);
			
			userRole = UserRoleDAO.addUserRole(userRole);
			if(userRole.getId() > 0) {
				
				String from = Configs.getConfig("adminemail");
				String to = user.getEmailAddress();
				String subject = Configs.getConfig("systemname") + " Installation";
				String body = "" +
						"Hello  Admin!" +
						"\n\nYou have successfully installed " + Configs.getConfig("systemname") + ". Use the following " +
						"credentials to login, and then change your password as prompted: " +
						"\n\nUsername: " + user.getUsername() +
						"\nPassword: " + password +
						"\n\nAccess the system from this link: " + Configs.getConfig("appurl") + Configs.getConfig("uri") + 
						"\n\nSystem Admin";
				
				    //Create necessary directories
				    String appDataDirectory = System.getProperty("user.home") + "/." + Configs.getConfig("uri");
					String logsDirectory = appDataDirectory + "logs/";
					File directory1 = new File(appDataDirectory);
					File directory2 = new File(logsDirectory);
					directory1.mkdirs();
					directory2.mkdirs();
				Emailer.send(from, to, subject, body);
				
				return "install-complete.xhtml?faces-redirect=true";
			} else {
				return "";
			}
		} else {
			return "index.xhtml?faces-redirect=true";
		}
	}

	public String getAdminEmail() {
		return adminEmail;
	}

	public void setAdminEmail(String adminEmail) {
		this.adminEmail = adminEmail;
	}

	public String getAdminUser() {
		return adminUser;
	}

	public void setAdminUser(String adminUser) {
		this.adminUser = adminUser;
	}
}