package com.nextramile.controllers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;

import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.crypto.hash.DefaultHashService;
import org.apache.shiro.crypto.hash.Sha256Hash;

import com.nextramile.dao.UserDAO;
import com.nextramile.models.UserAccount;
import com.nextramile.util.Configs;
import com.nextramile.util.Emailer;
//import com.nextramile.util.SessionManager;

@ManagedBean(name="userController", eager=true)
@SessionScoped
public class UserController {
	
	private UserAccount user, loggedInUser;
	private List<UserAccount> userAccountList;
	private String clearPassword;
	private UIComponent component;
	//private SessionManager sessionManager;
	
	
	@PostConstruct
	public void init() {
		user = new UserAccount();
	}
	
	public UserController() {
		userAccountList = UserDAO.getUserList();
		//CoreClass coreClass = new CoreClass();
		//sessionManager = coreClass.sessionManager;
		//String userId = sessionManager.getAttribute("userId");
		String userId = null;
		if(userId != null) {
		    //loggedInUser = UserDAO.find(Integer.parseInt(sessionManager.getAttribute("userId")));
			//Check if branchCode has changed, if so, set update flat
			boolean update = false;
			if(update == true) {
				//loggedInUser.setEditedBy(loggedInUser.getUsername());
				loggedInUser.setEditedBy("User");
				UserDAO.updateUser(loggedInUser);
			}
		}
	}
	
	public void reset(ActionEvent event){
		user = new UserAccount();
	}
	
	public String addUser() {
		clearPassword = Long.toHexString(Double.doubleToLongBits(Math.random())).substring(0,8);
		user.setPassword(encryptPassword(clearPassword));
		//user.setCreatedBy(loggedInUser.getUsername());
		user.setCreatedBy("User");
		//user.setEditedBy(loggedInUser.getUsername());
		user.setEditedBy("User");
		user.setResetFlag(true);
		if(UserDAO.checkExisting(user) == true) {
			UserDAO.addUser(user);
			String from = Configs.getConfig("adminemail");;
			String to = user.getEmailAddress();
			String subject = Configs.getConfig("systemname") + " User Account Creation";
			String body = 
					"Dear " + user.getFirstName() + ", " +
					"\n\nYour user account on has been successfully created. Use the following " +
					"credentials: " +
					"\n\nUsername: " + user.getUsername() +
					"\nPassword: " + clearPassword +
					"\n\nYou can access the system from this link: " + Configs.getConfig("appurl") + Configs.getConfig("uri") +
					"\n\nSystem Admin";
			Emailer.send(from, to, subject, body);
		} else {
			FacesContext fc = FacesContext.getCurrentInstance();
			FacesMessage msg = new FacesMessage("User Exists!","This user is already in the system.");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
	    	fc.addMessage(null, msg);
	    	fc.renderResponse();
		}
		userAccountList = UserDAO.getUserList();
	    user = new UserAccount();
	    return "admin-list-users.xhtml?faces-redirect=true";
	}
	
	public String updateUser() {
		user.setEditedBy(loggedInUser.getUsername());
	    UserDAO.updateUser(user);
	    userAccountList = UserDAO.getUserList();
	    user = new UserAccount();
	    return "admin-list-users.xhtml?faces-redirect=true";
	}
	
	/**
	 * @return the user
	 */
	public UserAccount getUserAccount() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUserAccount(UserAccount user) {
		this.user = user;
	}

	public UserAccount getLoggedInUser() {
		return loggedInUser;
	}

	public void setLoggedInUser(UserAccount loggedInUser) {
		this.loggedInUser = loggedInUser;
	}

	/**
	 * @return the userList
	 */
	public List<UserAccount> getUserAccountList() {
		return userAccountList;
	}

	/**
	 * @param userList the userList to set
	 */
	public void setUserAccountList(List<UserAccount> userAccountList) {
		this.userAccountList = userAccountList;
	}
	
	public void resetPassword() throws UnsupportedEncodingException {
		String newPassword = Long.toHexString(Double.doubleToLongBits(Math.random())).substring(0,8);
		user.setResetFlag(true);
		user.setEditedBy(loggedInUser.getUsername());
		user.setPassword(encryptPassword(newPassword));
		user.setActive(false);
		UserDAO.updateUserPassword(user);
		
		String from = Configs.getConfig("adminemail");
		String to = user.getEmailAddress();
		String subject = Configs.getConfig("systemname") + " Password Reset";
		String body = "" +
				"Dear " + user.getFirstName() + ", " +
				"\n\nYour password has been reset. Use the following " +
				"credentials, and then change your password as prompted: " +
				"\n\nUsername: " + user.getUsername() +
				"\nPassword: " + newPassword +
				"\n\nYou can access the system from this link: " + Configs.getConfig("appurl") + Configs.getConfig("uri") + 
				"\n\nSystem Admin";
		Emailer.send(from, to, subject, body);
		
		FacesContext fc = FacesContext.getCurrentInstance();
		FacesMessage msg = new FacesMessage("Reset Successfully","Password has been successfully reset and email sent to " + user.getFirstName() + " " + user.getLastName());
		msg.setSeverity(FacesMessage.SEVERITY_INFO);
		String iuAlertId = component.getClientId();
    	fc.addMessage(iuAlertId, msg);
    	fc.renderResponse();
	}
	
	public String changePassword() throws IOException {
		loggedInUser.setEditedBy(loggedInUser.getUsername());
		loggedInUser.setPassword(encryptPassword(loggedInUser.getPassword()));
		loggedInUser.setResetFlag(false);
		UserDAO.updateUserPassword(loggedInUser);
		return "logout.xhtml?faces-redirect=true";
	}
	
	private static String encryptPassword(String password)
	{
		DefaultHashService hashService = new DefaultHashService();
		hashService.setHashIterations(512);
		hashService.setHashAlgorithmName(Sha256Hash.ALGORITHM_NAME);
		//hashService.setPrivateSalt(new SimpleByteSource(PRIVATE_SALT)); // Same salt as in shiro.ini, but NOT base64-encoded.
		hashService.setGeneratePublicSalt(true);
		DefaultPasswordService passwordService = new DefaultPasswordService();
		passwordService.setHashService(hashService);
		String encryptedPassword = passwordService.encryptPassword(password);
		return encryptedPassword;
	}

	public void validatePassword(ComponentSystemEvent event) {
		String PASSWORD_PATTERN = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$";
		Pattern pattern;
		Matcher matcher;
		
		FacesContext fc = FacesContext.getCurrentInstance();
		UIComponent components = event.getComponent();
		// get password
		UIInput uiInputPassword = (UIInput) components.findComponent("password");
		String password = uiInputPassword.getLocalValue() == null ? ""
				: uiInputPassword.getLocalValue().toString();
		String passwordId = uiInputPassword.getClientId();
		// get confirm password
		UIInput uiInputConfirmPassword = (UIInput) components.findComponent("confirm-password");
		String confirmPassword = uiInputConfirmPassword.getLocalValue() == null ? ""
				: uiInputConfirmPassword.getLocalValue().toString();

		if (password.isEmpty() || confirmPassword.isEmpty()) {
			FacesMessage msg = new FacesMessage("Required!","Enter password and confirm!");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
        	fc.addMessage(passwordId, msg);
        	fc.renderResponse();
		    return;
		}
        // Password strengths rules
		pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        if(!matcher.matches()){
        	FacesMessage msg =
        			new FacesMessage("Weak Password!","Make sure that your password has combination of lower and upper case, a number and a special character");
        	msg.setSeverity(FacesMessage.SEVERITY_ERROR);
        	fc.addMessage(passwordId, msg);
        	fc.renderResponse();
        } else if (!password.equals(confirmPassword)) {
        	FacesMessage msg = new FacesMessage("Passwords do not match!");
        	msg.setSeverity(FacesMessage.SEVERITY_ERROR);
        	fc.addMessage(passwordId, msg);
        	fc.renderResponse();
        }
	}
	
	public UIComponent getComponent() {
		return component;
	}

	public void setComponent(UIComponent component) {
		this.component = component;
	}
}