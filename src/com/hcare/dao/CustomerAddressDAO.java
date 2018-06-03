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
import com.hcare.models.CustomerAddress;

/**
 * @author Samuel
 *
 */
public class CustomerAddressDAO {
	private static final String PERSISTENCE_UNIT_NAME = "hcare";
    private static EntityManager em;
    private static EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

    public static CustomerAddress addCustomerAddress(CustomerAddress customerAddress) {
    	customerAddress.setCreatedOn(new Timestamp(System.currentTimeMillis()));
    	customerAddress.setEditedOn(new Timestamp(System.currentTimeMillis()));
    	customerAddress.setCreatedBy(customerAddress.getEditedBy());
    	em = factory.createEntityManager();
        em.getTransaction().begin();
        em.persist(customerAddress);
        em.getTransaction().commit();
        em.close();
        return customerAddress;
    }
    
    public static void makeCustomerAddressCurrent(CustomerAddress customerAddress) {
    	List<CustomerAddress> customerAddressList = findAddressByCustomer(customerAddress.getCustomer());
    	for(CustomerAddress customerAddress2 : customerAddressList) {
    		customerAddress2.setCurrent(false);
    		updateCustomerAddress(customerAddress2);
    	}
    	customerAddress.setCurrent(true);
    	updateCustomerAddress(customerAddress);
    }
    
    public static CustomerAddress updateCustomerAddress(CustomerAddress customerAddress) {
    	em = factory.createEntityManager();
        em.getTransaction().begin();
        CustomerAddress customerAddress2 = em.find(CustomerAddress.class, customerAddress.getId());
        customerAddress2.setEditedOn(new Timestamp(System.currentTimeMillis()));
        customerAddress2.setEditedBy(customerAddress.getEditedBy());
        customerAddress2.setCustomer(customerAddress.getCustomer());
        customerAddress2.setDeliveryAddress(customerAddress.getDeliveryAddress());
        customerAddress2.setActive(customerAddress.getActive());
        em.persist(customerAddress2);
        em.getTransaction().commit();
        em.close();
        return customerAddress2;
    }
    
    public static CustomerAddress find(int id) {
     	em = factory.createEntityManager();
     	CustomerAddress customerAddress = em.find(CustomerAddress.class, id);
     	em.close();
     	return customerAddress;
    }
    
    public static CustomerAddress findCurrentCustomerAddress(Customer customer) {
    	em = factory.createEntityManager();
    	Query q = em.createQuery("select u from CustomerAddress u WHERE u.customer = :customer and  u.current=true");
        q.setParameter("customer", customer);
    	CustomerAddress customerAddress = new CustomerAddress();
    	try {
			customerAddress = (CustomerAddress) q.getSingleResult();
    	} catch(NoResultException e) {
    		System.out.println("No Results Exception");
    	}
    	return customerAddress;
    }
    
    public static List<CustomerAddress> findAddressByCustomer(Customer customer) {
    	em = factory.createEntityManager();
    	Query q = em.createQuery("select u from CustomerAddress u WHERE u.customer = :customer and  u.active=true");
        q.setParameter("customer", customer);
    	List<CustomerAddress> customerAddressList2 = new ArrayList<CustomerAddress>();
    	try {
    	    @SuppressWarnings("unchecked")
			List<CustomerAddress> customerAddressList = q.getResultList();
        	for(CustomerAddress customerAddress : customerAddressList) {
        		customerAddressList2.add(customerAddress);
        	}
    	} catch(NoResultException e) {
    		System.out.println("No Results Exception");
    	}
    	return customerAddressList2;
    }
    
    @SuppressWarnings("unchecked")
	public static List<CustomerAddress> getCustomerAddressList() {
    	em = factory.createEntityManager();
    	Query q = em.createQuery("SELECT u FROM CustomerAddress u WHERE u.active=true");
    	List<CustomerAddress> customerAddressList2 = new ArrayList<CustomerAddress>();
    	try {
    	    List<CustomerAddress> customerAddressList = q.getResultList();
        	for(CustomerAddress customerAddress : customerAddressList) {
        		customerAddressList2.add(customerAddress);
        	}
    	} catch(NoResultException e) {
    		System.out.println("No Results Exception");
    	}
    	return customerAddressList2;
    }
}