package com.nextramile.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.nextramile.models.Customer;
import com.nextramile.util.MSecurity;

/**
 * @author Samuel
 *
 */
public class CustomerDAO {
	private static final String PERSISTENCE_UNIT_NAME = "nextramile";
    private static EntityManager em;
    private static EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

    public static Customer addCustomer(Customer customer) {
    	Customer existingCustomer = findByEmail(customer.getEmail());
    	if(existingCustomer.getId() == 0) {
	    	customer.setCreatedOn(new Timestamp(System.currentTimeMillis()));
	    	customer.setEditedOn(new Timestamp(System.currentTimeMillis()));
	    	customer.setCreatedBy(customer.getEditedBy());
	    	String password = MSecurity.createMD5(customer.getPassword());
		    customer.setPassword(password);
	    	em = factory.createEntityManager();
	        em.getTransaction().begin();
	        em.persist(customer);
	        em.getTransaction().commit();	
	        em.close();
    	} else {
    		customer.setId(existingCustomer.getId());
    		updateCustomer(customer);
    	}
    	//Get referrer details
    	Customer referer = customer.getReferer();
    	String path = "";
	    int depth = 0;
    	if(referer != null) {
    	    if(referer.getId() > 0) {
    	        // Get path
    	        path = referer.getPath();
    	        // Get depth
    	        depth = referer.getDepth();
    	    }
    	}
    	customer.setDepth(depth + 1);
	    customer.setPath(customer.getId() + "_" + path);
	    customer = updateCustomer(customer);
        return customer;
    }
    
    public static Customer updateReferer(Customer customer) {
    	em = factory.createEntityManager();
        em.getTransaction().begin();
        Customer customer2 = em.find(Customer.class, customer.getId());
        customer2.setEditedOn(new Timestamp(System.currentTimeMillis()));
        customer2.setEditedBy(customer.getEditedBy());
        customer2.setReferer(customer.getReferer());
        em.persist(customer2);
        em.getTransaction().commit();
        em.close();
        return customer2;
    }
    
    public static Customer updateCustomer(Customer customer) {
    	em = factory.createEntityManager();
        em.getTransaction().begin();
        Customer customer2 = em.find(Customer.class, customer.getId());
        customer2.setEditedOn(new Timestamp(System.currentTimeMillis()));
        customer2.setEditedBy(customer.getEditedBy());
        customer2.setEmail(customer.getEmail());
        customer2.setPasswordResetToken(customer.getPasswordResetToken());
        customer2.setCustomerName(customer.getCustomerName());
        customer2.setPhone(customer.getPhone());
        customer2.setReferer(customer.getReferer());
        customer2.setPath(customer.getPath());
        customer2.setDepth(customer.getDepth());
        customer2.setActive(customer.getActive());
        em.persist(customer2);
        em.getTransaction().commit();
        em.close();
        return customer2;
    }
    
    public static Customer changePassword(Customer customer) {
    	em = factory.createEntityManager();
        em.getTransaction().begin();
        Customer customer2 = em.find(Customer.class, customer.getId());
        customer2.setEditedOn(new Timestamp(System.currentTimeMillis()));
        customer2.setEditedBy(customer.getEditedBy());
        String password = MSecurity.createMD5(customer.getPassword());
        customer2.setPassword(password);
        em.persist(customer2);
        em.getTransaction().commit();
        em.close();
        return customer2;
    }
    
    public static Customer find(int id) {
     	em = factory.createEntityManager();
     	Customer customer = em.find(Customer.class, id);
     	em.close();
     	return customer;
    }
    
    public static Customer findByEmail(String email) {
    	em = factory.createEntityManager();
    	Query q = em.createQuery("select u from Customer u WHERE u.email = :email");
        q.setParameter("email", email);
        Customer customer = new Customer();
        customer.setEmail(email);
        try {
     	    customer = (Customer) q.getSingleResult();
        } catch(NoResultException e) {}
     	em.close();
     	return customer;
    }
    
    public static Customer getCustomerByToken(Customer customer) {
    	em = factory.createEntityManager();
    	Query q = em.createQuery("select u from Customer u WHERE u.email = :email and u.passwordResetToken = :token");
    	q.setParameter("email", customer.getEmail());
    	q.setParameter("token", customer.getPasswordResetToken());
        customer = new Customer();
        try {
     	    customer = (Customer) q.getSingleResult();
        } catch(NoResultException e) {}
     	em.close();
     	return customer;
    }
    
    public static Customer getInvactiveCustomer(Customer customer) {
    	em = factory.createEntityManager();
    	Query q = em.createQuery("select u from Customer u WHERE u.email = :email and u.createdOn = :createdOn");
    	q.setParameter("email", customer.getEmail());
    	q.setParameter("createdOn", customer.getCreatedOn());
        customer = new Customer();
        try {
     	    customer = (Customer) q.getSingleResult();
        } catch(NoResultException e) {}
     	em.close();
     	return customer;
    }

    public static Customer login(Customer customer) {
    	em = factory.createEntityManager();
    	Query q = em.createQuery("select u from Customer u WHERE u.email = :email and u.password = :password");
        q.setParameter("email", customer.getEmail());
        q.setParameter("password", MSecurity.createMD5(customer.getPassword()));
        customer = new Customer();
        try {
     	    customer = (Customer) q.getSingleResult();
        } catch(NoResultException e) {}
     	em.close();
     	return customer;
    }

    public static boolean checkExisting(Customer customer) {
    	em = factory.createEntityManager();
    	Query q = em.createQuery("select u from Customer u WHERE u.email = :email");
    	q.setParameter("email", customer.getEmail());
        boolean findCustomer = (q.getResultList().size() > 0);
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
    	} catch(NoResultException e) {}
    	return customerList2;
    }
}