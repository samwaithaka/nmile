package com.nextramile.util;

import javax.faces.context.ExternalContext;

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

import com.nextramile.dao.UserDAO;
import com.nextramile.models.UserAccount;

public class AuthManager {
   
    private static Session session;
   
    public static String login(ExternalContext externalContext, String username, String password) {
        String configFileDir = externalContext.getRealPath("/WEB-INF/");
        Factory<SecurityManager> factory = new IniSecurityManagerFactory(configFileDir + "shiro.ini");
        SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);
        Subject currentUser = SecurityUtils.getSubject();
        session = currentUser.getSession();
        PassThruAuthenticationFilter filter = new PassThruAuthenticationFilter();
        String url = filter.getSuccessUrl();
        String message = null;
        if(!currentUser.isAuthenticated()) {
            UsernamePasswordToken token = new UsernamePasswordToken(username,password);
            //this is all you have to do to support 'remember me' (no config - built in!):
            token.setRememberMe(true);
            try {
                currentUser.login(token);
                UserAccount user = UserDAO.findByUsername(username);
                session.setAttribute("user", user);
                session.setAttribute("isLoggedIn", "Yes");
                url = "index.xhtml?faces-redirect=true";
                message = "Logged in successfully";
                System.out.println(message);
            } catch (UnknownAccountException uae) {
                message = "The account is not in the system";
                System.out.println(message);
            } catch (IncorrectCredentialsException ice) {
                message = "Username and password did not match";
                System.out.println(message);
            } catch (LockedAccountException lae) {
                message = "This account has been locked";
                System.out.println(message);
            } catch (AuthenticationException ae) {
                message = "An error has occured";
                System.out.println(message);
            }
        } else {
            message = "Authenticated already";
            System.out.println(message);
        }
        session.setAttribute("message", message);
        return url;
    }
   
    public static Session getSession() {
        return session;
    }
}