package com.nextramile.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.nextramile.models.CustomerSegment;

/**
 * @author Samuel
 *
 */
public class CustomerSegmentDAO {
	private static final String PERSISTENCE_UNIT_NAME = "hcare";
    private static EntityManager em;
    private static EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

    public static CustomerSegment addCustomerSegment(CustomerSegment customerSegment) {
    	customerSegment.setCreatedOn(new Timestamp(System.currentTimeMillis()));
    	customerSegment.setEditedOn(new Timestamp(System.currentTimeMillis()));
    	customerSegment.setCreatedBy(customerSegment.getEditedBy());
    	em = factory.createEntityManager();
        em.getTransaction().begin();
        em.persist(customerSegment);
        em.getTransaction().commit();
        em.close();
        return customerSegment;
    }
    
    public static CustomerSegment updateCustomerSegment(CustomerSegment customerSegment) {
    	em = factory.createEntityManager();
        em.getTransaction().begin();
        CustomerSegment customerSegment2 = em.find(CustomerSegment.class, customerSegment.getId());
        customerSegment2.setEditedOn(new Timestamp(System.currentTimeMillis()));
        customerSegment2.setEditedBy(customerSegment.getEditedBy());
        //customerSegment2.setCustomer(customerSegment.getCustomer());
        //customerSegment2.setSegment(customerSegment.getSegment());
        customerSegment2.setActive(customerSegment.getActive());
        em.persist(customerSegment2);
        em.getTransaction().commit();
        em.close();
        return customerSegment2;
    }
    
    public static CustomerSegment find(int id) {
     	em = factory.createEntityManager();
     	CustomerSegment customerSegment = em.find(CustomerSegment.class, id);
     	em.close();
     	return customerSegment;
    }
    
    @SuppressWarnings("unchecked")
	public static List<CustomerSegment> getCustomerSegmentList() {
    	em = factory.createEntityManager();
    	Query q = em.createQuery("SELECT u FROM CustomerSegment u WHERE u.active=true");
    	List<CustomerSegment> customerSegmentList2 = new ArrayList<CustomerSegment>();
    	try {
    	    List<CustomerSegment> customerSegmentList = q.getResultList();
        	for(CustomerSegment customerSegment : customerSegmentList) {
        		customerSegmentList2.add(customerSegment);
        	}
    	} catch(NoResultException e) {
    		System.out.println("No Results Exception");
    	}
    	return customerSegmentList2;
    }
}