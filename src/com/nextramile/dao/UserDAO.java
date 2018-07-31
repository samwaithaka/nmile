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

import com.nextramile.models.UserAccount;

/**
 * @author Samuel
 *
 */
public class UserDAO {
	private static final String PERSISTENCE_UNIT_NAME = "nextramile";
    private static EntityManager em;
    private static EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

    public static UserAccount addUser(UserAccount user) {
    	user.setCreatedOn(new Timestamp(System.currentTimeMillis()));
    	user.setEditedOn(new Timestamp(System.currentTimeMillis()));
    	user.setCreatedBy(user.getEditedBy());
    	em = factory.createEntityManager();
        em.getTransaction().begin();
        em.persist(user);
        em.getTransaction().commit();
        em.close();
        return user;
    }
    
    public static void updateUser(UserAccount user) {
    	em = factory.createEntityManager();
        em.getTransaction().begin();
        UserAccount user2 = em.find(UserAccount.class, user.getId());
        user2.setEditedOn(new Timestamp(System.currentTimeMillis()));
        user2.setEmailAddress(user.getEmailAddress());
        user2.setFirstName(user.getFirstName());
        user2.setLastName(user.getLastName());
        user2.setUsername(user.getUsername());
        user2.setEditedBy(user.getEditedBy());
        em.persist(user2);
        em.getTransaction().commit();
        em.close();
    }
    
    public static void updateUserPassword(UserAccount user) {
    	em = factory.createEntityManager();
        em.getTransaction().begin();
        UserAccount user2 = em.find(UserAccount.class, user.getId());
        user2.setEditedOn(new Timestamp(System.currentTimeMillis()));
        user2.setPassword(user.getPassword());
        user2.setResetFlag(user.getResetFlag());
        em.persist(user2);
        em.getTransaction().commit();
        em.close();
    }

    
    public static UserAccount find(int id) {
     	em = factory.createEntityManager();
     	UserAccount user = em.find(UserAccount.class, id);
     	int passwordAge = (int)(System.currentTimeMillis() - user.getEditedOn().getTime())/(3600*24*1000);
     	user.setPasswordAge(passwordAge);
     	em.close();
     	return user;
    }
    
    public static UserAccount findByUsername(String username) {
    	em = factory.createEntityManager();
    	Query q = em.createQuery("select u from UserAccount u WHERE u.username = :username");
        q.setParameter("username", username);
     	UserAccount user = (UserAccount) q.getSingleResult();
     	em.close();
     	return user;
    }
    
    public static boolean checkExisting(UserAccount user) {
    	em = factory.createEntityManager();
    	Query q = em.createQuery("select u from UserAccount u WHERE u.emailAddress = :emailAddress OR u.username = :username");
    	q.setParameter("emailAddress", user.getEmailAddress());
        q.setParameter("username", user.getUsername());
        boolean findUser = (q.getResultList().size() == 0);
        em.close();
        return findUser;
    }
    
    @SuppressWarnings("unchecked")
	public static List<UserAccount> getUserList() {
    	em = factory.createEntityManager();
    	Query q = em.createQuery("SELECT u FROM UserAccount u WHERE u.active=true");
    	List<UserAccount> userList2 = new ArrayList<UserAccount>();
    	try {
    	    List<UserAccount> userList = q.getResultList();
        	for(UserAccount user : userList) {
        		int passwordAge = (int)(new Timestamp(System.currentTimeMillis()).getTime() - user.getEditedOn().getTime())/(3600*24*1000);
             	user.setPasswordAge(passwordAge);
        		userList2.add(user);
        	}
    	} catch(NoResultException e) {
    		System.out.println("No Results Exception");
    	}
    	return userList2;
    }
}
