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
import com.hcare.models.Order;

/**
 * @author Samuel
 *
 */
public class OrderDAO {
	private static final String PERSISTENCE_UNIT_NAME = "hcare";
    private static EntityManager em;
    private static EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

    public static Order addOrder(Order order) {
    	order.setCreatedOn(new Timestamp(System.currentTimeMillis()));
    	order.setEditedOn(new Timestamp(System.currentTimeMillis()));
    	order.setCreatedBy(order.getEditedBy());
    	em = factory.createEntityManager();
        em.getTransaction().begin();
        em.persist(order);
        em.getTransaction().commit();
        em.close();
        return order;
    }
    
    public static void updateOrder(Order order) {
    	em = factory.createEntityManager();
        em.getTransaction().begin();
        Order order2 = em.find(Order.class, order.getId());
        order2.setEditedOn(new Timestamp(System.currentTimeMillis()));
        order2.setEditedBy(order.getEditedBy());
        order2.setCustomer(order.getCustomer());
        order2.setProduct(order.getProduct());
        order2.setColor(order.getColor());
        order2.setSize(order.getSize());
        order2.setDeliveryAddress(order.getDeliveryAddress());
        order2.setActive(order.getActive());
        em.persist(order2);
        em.getTransaction().commit();
        em.close();
    }
    
    public static Order find(int id) {
     	em = factory.createEntityManager();
     	Order order = em.find(Order.class, id);
     	em.close();
     	return order;
    }
    
    public static List<Order> findByOrdersName(Customer customer) {
    	em = factory.createEntityManager();
    	Query q = em.createQuery("select u from Order u WHERE u.customer = :customer");
        q.setParameter("customer", customer);
    	List<Order> orderList2 = new ArrayList<Order>();
    	try {
    	    @SuppressWarnings("unchecked")
			List<Order> orderList = q.getResultList();
        	for(Order order : orderList) {
        		orderList2.add(order);
        	}
    	} catch(NoResultException e) {
    		System.out.println("No Results Exception");
    	}
    	return orderList2;
    }
    
    @SuppressWarnings("unchecked")
	public static List<Order> getOrderList() {
    	em = factory.createEntityManager();
    	Query q = em.createQuery("SELECT u FROM Order u WHERE u.active=true");
    	List<Order> orderList2 = new ArrayList<Order>();
    	try {
    	    List<Order> orderList = q.getResultList();
        	for(Order order : orderList) {
        		orderList2.add(order);
        	}
    	} catch(NoResultException e) {
    		System.out.println("No Results Exception");
    	}
    	return orderList2;
    }
}