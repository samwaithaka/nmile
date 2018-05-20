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

/**
 * @author Samuel
 *
 */
public class CustomerDAO {
	private static final String PERSISTENCE_UNIT_NAME = "hcare";
    private static EntityManager em;
    private static EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

    public static Customer addCustomer(Customer customer) {
    	customer.setCreatedOn(new Timestamp(System.currentTimeMillis()));
    	customer.setEditedOn(new Timestamp(System.currentTimeMillis()));
    	customer.setCreatedBy(customer.getEditedBy());
    	em = factory.createEntityManager();
        em.getTransaction().begin();
        em.persist(customer);
        em.getTransaction().commit();
        em.close();
        return customer;
    }
    
    public static void updateCustomer(Customer customer) {
    	em = factory.createEntityManager();
        em.getTransaction().begin();
        Customer customer2 = em.find(Customer.class, customer.getId());
        customer2.setEditedOn(new Timestamp(System.currentTimeMillis()));
        customer2.setEditedBy(customer.getEditedBy());
        customer2.setEmail(customer.getEmail());
        customer2.setCustomerName(customer.getCustomerName());
        customer2.setPhone(customer.getPhone());
        customer2.setReferer(customer.getReferer());
        customer2.setPath(customer.getPath());
        customer2.setDepth(customer.getDepth());
        customer2.setActive(customer.getActive());
        em.persist(customer2);
        em.getTransaction().commit();
        em.close();
    }
    
    public static Customer find(int id) {
     	em = factory.createEntityManager();
     	Customer customer = em.find(Customer.class, id);
     	em.close();
     	return customer;
    }
    
    public static Customer findByCustomername(String customername) {
    	em = factory.createEntityManager();
    	Query q = em.createQuery("select u from Customer u WHERE u.customername = :customername");
        q.setParameter("customername", customername);
     	Customer customer = (Customer) q.getSingleResult();
     	em.close();
     	return customer;
    }
    
    public static boolean checkExisting(Customer customer) {
    	em = factory.createEntityManager();
    	Query q = em.createQuery("select u from Customer u WHERE u.email = :email");
    	q.setParameter("email", customer.getEmail());
        boolean findCustomer = (q.getResultList().size() == 0);
        em.close();
        return findCustomer;
    }
    
    @SuppressWarnings("unchecked")
	public static List<Customer> getCustomerList() {
    	em = factory.createEntityManager();
    	Query q = em.createQuery("SELECT u FROM Customer u WHERE u.active=true");
    	List<Customer> customerList2 = new ArrayList<Customer>();
    	try {
    	    List<Customer> customerList = q.getResultList();
        	for(Customer customer : customerList) {
        		customerList2.add(customer);
        	}
    	} catch(NoResultException e) {
    		System.out.println("No Results Exception");
    	}
    	return customerList2;
    }
}