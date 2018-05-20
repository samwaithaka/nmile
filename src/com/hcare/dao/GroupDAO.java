package com.hcare.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.hcare.models.Group;

/**
 * @author Samuel
 *
 */
public class GroupDAO {
	private static final String PERSISTENCE_UNIT_NAME = "hcare";
    private static EntityManager em;
    private static EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

    public static Group addGroup(Group group) {
    	group.setCreatedOn(new Timestamp(System.currentTimeMillis()));
    	group.setEditedOn(new Timestamp(System.currentTimeMillis()));
    	group.setCreatedBy(group.getEditedBy());
    	em = factory.createEntityManager();
        em.getTransaction().begin();
        em.persist(group);
        em.getTransaction().commit();
        em.close();
        return group;
    }
    
    public static void updateGroup(Group group) {
    	em = factory.createEntityManager();
        em.getTransaction().begin();
        Group group2 = em.find(Group.class, group.getId());
        group2.setEditedOn(new Timestamp(System.currentTimeMillis()));
        group2.setEditedBy(group.getEditedBy());
        group2.setGroupName(group.getGroupName());
        group2.setActive(group.getActive());
        em.persist(group2);
        em.getTransaction().commit();
        em.close();
    }
    
    public static Group find(int id) {
     	em = factory.createEntityManager();
     	Group group = em.find(Group.class, id);
     	em.close();
     	return group;
    }
    
    public static Group findByGroupName(String groupName) {
    	em = factory.createEntityManager();
    	Query q = em.createQuery("select u from Group u WHERE u.groupName = :groupName");
        q.setParameter("groupName", groupName);
     	Group group = (Group) q.getSingleResult();
     	em.close();
     	return group;
    }
    
    @SuppressWarnings("unchecked")
	public static List<Group> getGroupList() {
    	em = factory.createEntityManager();
    	Query q = em.createQuery("SELECT u FROM Group u WHERE u.active=true");
    	List<Group> groupList2 = new ArrayList<Group>();
    	try {
    	    List<Group> groupList = q.getResultList();
        	for(Group group : groupList) {
        		groupList2.add(group);
        	}
    	} catch(NoResultException e) {
    		System.out.println("No Results Exception");
    	}
    	return groupList2;
    }
}