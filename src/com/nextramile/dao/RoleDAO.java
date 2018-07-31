/**
 * 
 */
package com.nextramile.dao;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.nextramile.models.Role;

/**
 * @author Samuel
 *
 */
public class RoleDAO {
	private static final String PERSISTENCE_UNIT_NAME = "nextramile";
    private static EntityManager em;
    private static EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

    public static Role addRole(Role role) {
    	role.setCreatedOn(new Timestamp(System.currentTimeMillis()));
    	em = factory.createEntityManager();
        em.getTransaction().begin();
        em.persist(role);
        em.getTransaction().commit();
        em.close();
        return role;
    }
    
    public static void updateRole(Role role) {
    	em = factory.createEntityManager();
        em.getTransaction().begin();
        Role role2 = em.find(Role.class, role.getId());
        role.setEditedBy(role.getEditedBy());
        role2.setEditedOn(new Timestamp(System.currentTimeMillis()));
        role2.setRoleName(role.getRoleName());
        role2.setActive(role.getActive());
        em.persist(role2);
        em.getTransaction().commit();
        em.close();
    }
    
    public static Role find(int id) {
     	em = factory.createEntityManager();
     	Role role = em.find(Role.class, id);
     	em.close();
     	return role;
     }
    
    @SuppressWarnings("unchecked")
	public static List<Role> getRoleList() {
    	em = factory.createEntityManager();
    	Query q = em.createQuery("SELECT r FROM Role r WHERE r.active=true");
    	List<Role> roleList = q.getResultList();
    	return roleList;
    }
}
