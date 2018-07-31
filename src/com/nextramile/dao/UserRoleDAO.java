/**
 * 
 */
package com.nextramile.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.nextramile.models.Role;
import com.nextramile.models.UserAccount;
import com.nextramile.models.UserRole;

/**
 * @author Samuel
 *
 */
public class UserRoleDAO {
	private static final String PERSISTENCE_UNIT_NAME = "nextramile";
    private static EntityManager em;
    private static EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

    public static UserRole addUserRole(UserRole userRole) {
    	// Finding if there's a user role for particular account, and role, if none, create one, otherwise update
    	UserRole userRole2 = find(userRole.getUserAccount(),userRole.getRole());
    	if(userRole2.getId() == 0){
    		em = factory.createEntityManager();
            em.getTransaction().begin();
            userRole.setCreatedOn(new Timestamp(System.currentTimeMillis()));
            userRole.setCreatedBy("user");
            userRole.setEditedOn(new Timestamp(System.currentTimeMillis()));
            userRole.setEditedBy("user");
            em.persist(userRole);
            em.getTransaction().commit();
            em.close();
            return userRole;
    	} else {
    		userRole2.setActive(true);
    		updateUserRole(userRole2);
    		return userRole2;
    	}
    	
    }
    
    public static void updateUserRole(UserRole userRole) {
        em = factory.createEntityManager();
        em.getTransaction().begin();
        UserRole userRole2 = em.find(UserRole.class, userRole.getId());
        userRole2.setEditedOn(new Timestamp(System.currentTimeMillis()));
        userRole2.setEditedBy("user");
        userRole2.setUserAccount(userRole.getUserAccount());
        userRole2.setRole(userRole.getRole());
        userRole2.setActive(userRole.getActive());
        em.persist(userRole2);
        em.getTransaction().commit();
        em.close();
    }
    
    public static void deleteUserRole(UserRole userRole) {
    	UserRole userRole2 = find(userRole.getUserAccount(),userRole.getRole());
        userRole2.setActive(false);
        updateUserRole(userRole2);
    }
    
    public static UserRole find(UserAccount user, Role role) {
     	em = factory.createEntityManager();
     	Query q = em.createQuery("SELECT ur FROM UserRole ur WHERE ur.role = :role AND ur.user = :user");
     	q.setParameter("user", user);
     	q.setParameter("role", role);
     	UserRole userRole;
     	try {
     	    userRole = (UserRole) q.getSingleResult();
     	} catch(NoResultException e) {
     		userRole = new UserRole();
     	}
     	em.close();
     	return userRole;
    }

    @SuppressWarnings("unchecked")
	public static List<Role> getActiveRoles(UserAccount user) {
    	em = factory.createEntityManager();
    	String sql = "SELECT id FROM role WHERE active=true AND id IN "
    			+ "(SELECT role_id FROM user_role WHERE active=true AND user_account_id = ?userAccountId)";
    	Query q = em.createNativeQuery(sql);
    	q.setParameter("userAccountId", user.getId());
    	List<Integer> ids = q.getResultList();
    	List<Role> userRoleList = new ArrayList<Role>();
    	for (int roleId : ids) {
    		userRoleList.add(RoleDAO.find(roleId));
    	}
    	return userRoleList;
    }
    
    @SuppressWarnings("unchecked")
	public static List<Role> getInActiveRoles(UserAccount user) {
    	em = factory.createEntityManager();
    	String sql = "SELECT id FROM role WHERE active=true AND id NOT IN "
    			+ "(SELECT role_id FROM user_role WHERE active=true AND user_account_id = ?userAccountId)";
    	Query q = em.createNativeQuery(sql);
    	q.setParameter("userAccountId", user.getId());
    	List<Integer> ids = q.getResultList();
    	List<Role> userRoleList = new ArrayList<Role>();
    	for (int roleId : ids) {
    		userRoleList.add(RoleDAO.find(roleId));
    	}
    	return userRoleList;
    }
}
