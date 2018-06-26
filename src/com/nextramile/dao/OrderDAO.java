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
import com.nextramile.models.CustomerOrder;

/**
 * @author Samuel
 *
 */
public class OrderDAO {
	private static final String PERSISTENCE_UNIT_NAME = "hcare";
    private static EntityManager em;
    private static EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

    public static CustomerOrder addOrder(CustomerOrder order) {
    	CustomerOrder order2 = findOrder(order);
    	if(order2.getId() == 0) {
	    	order.setCreatedOn(new Timestamp(System.currentTimeMillis()));
	    	order.setEditedOn(new Timestamp(System.currentTimeMillis()));
	    	order.setCreatedBy(order.getEditedBy());
	    	em = factory.createEntityManager();
	        em.getTransaction().begin();
	        em.persist(order);
	        em.getTransaction().commit();
	        em.close();
    	} else {
    		order = order2;
    	}
        return order;
    }
    
    public static CustomerOrder updateOrder(CustomerOrder order) {
    	em = factory.createEntityManager();
        em.getTransaction().begin();
        CustomerOrder order2 = em.find(CustomerOrder.class, order.getId());
        order2.setEditedOn(new Timestamp(System.currentTimeMillis()));
        order2.setEditedBy(order.getEditedBy());
        order2.setCustomer(order.getCustomer());
        order2.setProduct(order.getProduct());
        order2.setColor(order.getColor());
        order2.setSize(order.getSize());
        order2.setQuantity(order.getQuantity());
        order2.setCheckout(order.getCheckout());
        order2.setDeliveryAddress(order.getDeliveryAddress());
        order2.setActive(order.getActive());
        em.persist(order2);
        em.getTransaction().commit();
        em.close();
        return order2;
    }
    
    public static CustomerOrder find(int id) {
     	em = factory.createEntityManager();
     	CustomerOrder order = em.find(CustomerOrder.class, id);
     	em.close();
     	return order;
    }
    
    public static List<CustomerOrder> findByOrdersName(Customer customer) {
    	em = factory.createEntityManager();
    	Query q = em.createQuery("select u from Order u WHERE u.customer = :customer");
        q.setParameter("customer", customer);
    	List<CustomerOrder> orderList2 = new ArrayList<CustomerOrder>();
    	try {
    	    @SuppressWarnings("unchecked")
			List<CustomerOrder> orderList = q.getResultList();
        	for(CustomerOrder order : orderList) {
        		orderList2.add(order);
        	}
    	} catch(NoResultException e) {
    		System.out.println("No Results Exception");
    	}
    	return orderList2;
    }
    
    @SuppressWarnings("unchecked")
 	public static List<CustomerOrder> getCustomerOrderList(Customer customer) {
     	em = factory.createEntityManager();
     	Query q = em.createQuery("SELECT o FROM CustomerOrder o WHERE o.checkout=false and o.active = true and o.customer = :customer");
     	q.setParameter("customer", customer);
     	List<CustomerOrder> orderList2 = new ArrayList<CustomerOrder>();
     	try {
     	    List<CustomerOrder> orderList = q.getResultList();
         	for(CustomerOrder order : orderList) {
         		orderList2.add(order);
         	}
     	} catch(NoResultException e) {
     		System.out.println("No Results Exception");
     	}
     	return orderList2;
     }
    
    public static CustomerOrder findOrder(CustomerOrder customerOrder) {
    	em = factory.createEntityManager();
    	Query q = em.createQuery("select u from CustomerOrder u WHERE u.customer = :customer and u.product = :product and u.checkout = false");
    	q.setParameter("customer", customerOrder.getCustomer());
    	q.setParameter("product", customerOrder.getProduct());
    	try {
    	    customerOrder = (CustomerOrder) q.getSingleResult();
    	} catch(NoResultException e) {
    		//
    	}
        em.close();
        return customerOrder;
    }
    
    @SuppressWarnings("unchecked")
 	public static List<CustomerOrder> getOrderList() {
     	em = factory.createEntityManager();
     	Query q = em.createQuery("SELECT u FROM Order u WHERE u.active=true");
     	List<CustomerOrder> orderList2 = new ArrayList<CustomerOrder>();
     	try {
     	    List<CustomerOrder> orderList = q.getResultList();
         	for(CustomerOrder order : orderList) {
         		orderList2.add(order);
         	}
     	} catch(NoResultException e) {
     		System.out.println("No Results Exception");
     	}
     	return orderList2;
     }
}