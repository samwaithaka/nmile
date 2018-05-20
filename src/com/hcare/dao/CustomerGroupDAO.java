package com.hcare.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.hcare.models.CustomerGroup;

/**
 * @author Samuel
 *
 */
public class CustomerGroupDAO {
	private static final String PERSISTENCE_UNIT_NAME = "hcare";
    private static EntityManager em;
    private static EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

    public static CustomerGroup addCustomerGroup(CustomerGroup customerGroup) {
    	customerGroup.setCreatedOn(new Timestamp(System.currentTimeMillis()));
    	customerGroup.setEditedOn(new Timestamp(System.currentTimeMillis()));
    	customerGroup.setCreatedBy(customerGroup.getEditedBy());
    	em = factory.createEntityManager();
        em.getTransaction().begin();
        em.persist(customerGroup);
        em.getTransaction().commit();
        em.close();
        return customerGroup;
    }
    
    public static void updateCustomerGroup(CustomerGroup customerGroup) {
    	em = factory.createEntityManager();
        em.getTransaction().begin();
        CustomerGroup customerGroup2 = em.find(CustomerGroup.class, customerGroup.getId());
        customerGroup2.setEditedOn(new Timestamp(System.currentTimeMillis()));
        customerGroup2.setEditedBy(customerGroup.getEditedBy());
        customerGroup2.setCustomer(customerGroup.getCustomer());
        customerGroup2.setGroup(customerGroup.getGroup());
        customerGroup2.setActive(customerGroup.getActive());
        em.persist(customerGroup2);
        em.getTransaction().commit();
        em.close();
    }
    
    public static CustomerGroup find(int id) {
     	em = factory.createEntityManager();
     	CustomerGroup customerGroup = em.find(CustomerGroup.class, id);
     	em.close();
     	return customerGroup;
    }
    
    @SuppressWarnings("unchecked")
	public static List<CustomerGroup> getCustomerGroupList() {
    	em = factory.createEntityManager();
    	Query q = em.createQuery("SELECT u FROM CustomerGroup u WHERE u.active=true");
    	List<CustomerGroup> customerGroupList2 = new ArrayList<CustomerGroup>();
    	try {
    	    List<CustomerGroup> customerGroupList = q.getResultList();
        	for(CustomerGroup customerGroup : customerGroupList) {
        		customerGroupList2.add(customerGroup);
        	}
    	} catch(NoResultException e) {
    		System.out.println("No Results Exception");
    	}
    	return customerGroupList2;
    }
}