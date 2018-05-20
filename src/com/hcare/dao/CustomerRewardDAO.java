package com.hcare.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.hcare.models.Customer;
import com.hcare.models.CustomerReward;

/**
 * @author Samuel
 *
 */
public class CustomerRewardDAO {
	private static final String PERSISTENCE_UNIT_NAME = "hcare";
    private static EntityManager em;
    private static EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

    public static CustomerReward addCustomerReward(CustomerReward customerReward) {
    	customerReward.setCreatedOn(new Timestamp(System.currentTimeMillis()));
    	customerReward.setEditedOn(new Timestamp(System.currentTimeMillis()));
    	customerReward.setCreatedBy(customerReward.getEditedBy());
    	em = factory.createEntityManager();
        em.getTransaction().begin();
        em.persist(customerReward);
        em.getTransaction().commit();
        em.close();
        return customerReward;
    }
    
    public static void updateCustomerReward(CustomerReward customerReward) {
    	em = factory.createEntityManager();
        em.getTransaction().begin();
        CustomerReward customerReward2 = em.find(CustomerReward.class, customerReward.getId());
        customerReward2.setEditedOn(new Timestamp(System.currentTimeMillis()));
        customerReward2.setEditedBy(customerReward.getEditedBy());
        customerReward2.setCustomer(customerReward.getCustomer());
        customerReward2.setReward(customerReward.getReward());
        customerReward2.setActive(customerReward.getActive());
        em.persist(customerReward2);
        em.getTransaction().commit();
        em.close();
    }
    
    public static CustomerReward find(int id) {
     	em = factory.createEntityManager();
     	CustomerReward customerReward = em.find(CustomerReward.class, id);
     	em.close();
     	return customerReward;
    }
    
    public static List<CustomerReward> findByCustomerRewardsName(Customer customer) {
    	em = factory.createEntityManager();
    	Query q = em.createQuery("select u from CustomerReward u WHERE u.customer = :customer");
        q.setParameter("customer", customer);
    	List<CustomerReward> customerRewardList2 = new ArrayList<CustomerReward>();
    	try {
    	    @SuppressWarnings("unchecked")
			List<CustomerReward> customerRewardList = q.getResultList();
        	for(CustomerReward customerReward : customerRewardList) {
        		customerRewardList2.add(customerReward);
        	}
    	} catch(NoResultException e) {
    		System.out.println("No Results Exception");
    	}
    	return customerRewardList2;
    }
    
    @SuppressWarnings("unchecked")
	public static List<CustomerReward> getCustomerRewardList() {
    	em = factory.createEntityManager();
    	Query q = em.createQuery("SELECT u FROM CustomerReward u WHERE u.active=true");
    	List<CustomerReward> customerRewardList2 = new ArrayList<CustomerReward>();
    	try {
    	    List<CustomerReward> customerRewardList = q.getResultList();
        	for(CustomerReward customerReward : customerRewardList) {
        		customerRewardList2.add(customerReward);
        	}
    	} catch(NoResultException e) {
    		System.out.println("No Results Exception");
    	}
    	return customerRewardList2;
    }
}