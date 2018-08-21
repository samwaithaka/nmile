package com.nextramile.util;

import javax.faces.context.ExternalContext;
import javax.servlet.ServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.util.Factory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.PassThruAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

import com.nextramile.dao.UserDAO;
import com.nextramile.models.UserAccount;

public class AuthManager {
	private static Session session;
	
	public static String login(ExternalContext externalContext, String username, String password) {
		String successUrl = WebUtils.getSavedRequest((ServletRequest) externalContext.getRequest()).getRequestUrl();
		successUrl = successUrl.substring(successUrl.lastIndexOf("/") + 1);
		String configFileDir = externalContext.getRealPath("/WEB-INF/");
		Factory<SecurityManager> factory = new IniSecurityManagerFactory(configFileDir + "shiro.ini");
		SecurityManager securityManager = factory.getInstance();
		SecurityUtils.setSecurityManager(securityManager);
		Subject currentUser = SecurityUtils.getSubject();
		session = currentUser.getSession();
		String message = null;
		UserAccount user = UserDAO.findByUsername(username);
		if (!currentUser.isAuthenticated()) {
			UsernamePasswordToken token = new UsernamePasswordToken(username,password);
			token.setRememberMe(true);
			try {
				//checkLocked(user.getLocked());
				currentUser.login(token);
				//if(user.getAttempts() > 0) {
				//	user.setAttempts((byte)0);
				//	UserDAO.updateUser(user);
				//}
				if(user.getResetFlag() == true || user.getPasswordAge() > 30) {
					successUrl = "change-password.xhtml?faces-redirect=true";
				}
				session.setAttribute("user", user);
				message = "Logged in successfully";
			} catch (UnknownAccountException uae) {
				message = "The account is not in the system";
			} catch (IncorrectCredentialsException ice) {
				//byte attempts = user.getAttempts();
				//++attempts;
				//if(attempts < 3) {
				//	int remaining = 3-attempts;
				//	String text = remaining > 1 ? " attempts" : " attempt";
				//	message = "Username and password did not match. You have " + remaining + text + " left";
				//} else {
				//	message = "Account has been locked. Contact administrator";
				//}
				//user.setAttempts(attempts);
				//UserDAO.updateUser(user);
				//if(attempts >= 3) {
				//	user.setLocked(true);
				//	UserDAO.updateUser(user);
				//}
				message = "Username and password did not match.";
			} catch (LockedAccountException lae) {
				message = "This account is locked. Contact administrator";
			} catch (AuthenticationException ae) {
				message = "Authentication error has occured";
			}
		} else {
			message = "Authenticated already";
			session.setAttribute("user", user);
			if(user.getResetFlag() == true || user.getPasswordAge() > 30) {
				successUrl = "change-password.xhtml?faces-redirect=true";
			}
		}
		session.setAttribute("message", message);
		return successUrl + "?faces-redirect=true";
	}

	private static void checkLocked(boolean locked) throws LockedAccountException {
		if(locked == true) {
			throw new LockedAccountException();
		}
	}

	public static Session getSession() {
		return session;
	}
}