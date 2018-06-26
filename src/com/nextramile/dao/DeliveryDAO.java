package com.nextramile.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.nextramile.models.CustomerOrder;
import com.nextramile.models.Delivery;

/**
 * @author Samuel
 *
 */
public class DeliveryDAO {
	private static final String PERSISTENCE_UNIT_NAME = "hcare";
    private static EntityManager em;
    private static EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

    public static Delivery addDelivery(Delivery delivery) {
    	delivery.setCreatedOn(new Timestamp(System.currentTimeMillis()));
    	delivery.setEditedOn(new Timestamp(System.currentTimeMillis()));
    	delivery.setCreatedBy(delivery.getEditedBy());
    	em = factory.createEntityManager();
        em.getTransaction().begin();
        em.persist(delivery);
        em.getTransaction().commit();
        em.close();
        return delivery;
    }
    
    public static Delivery updateDelivery(Delivery delivery) {
    	em = factory.createEntityManager();
        em.getTransaction().begin();
        Delivery delivery2 = em.find(Delivery.class, delivery.getId());
        delivery2.setEditedOn(new Timestamp(System.currentTimeMillis()));
        delivery2.setEditedBy(delivery.getEditedBy());
        delivery2.setCost(delivery.getCost());
        delivery2.setOrder(delivery.getOrder());
        delivery2.setRemarks(delivery.getRemarks());
        delivery2.setStatus(delivery.getStatus());
        delivery2.setActive(delivery.getActive());
        em.persist(delivery2);
        em.getTransaction().commit();
        em.close();
        return delivery2;
    }
    
    public static Delivery find(int id) {
     	em = factory.createEntityManager();
     	Delivery delivery = em.find(Delivery.class, id);
     	em.close();
     	return delivery;
    }
    
    public static List<Delivery> findByDeliveriesByCustomer(CustomerOrder order) {
    	em = factory.createEntityManager();
    	Query q = em.createQuery("select u from Delivery u WHERE u.customer = :customer");
        q.setParameter("customer", order.getCustomer());
    	List<Delivery> deliveryList2 = new ArrayList<Delivery>();
    	try {
    	    @SuppressWarnings("unchecked")
			List<Delivery> deliveryList = q.getResultList();
        	for(Delivery delivery : deliveryList) {
        		deliveryList2.add(delivery);
        	}
    	} catch(NoResultException e) {
    		System.out.println("No Results Exception");
    	}
    	return deliveryList2;
    }
    
    @SuppressWarnings("unchecked")
	public static List<Delivery> getDeliveryList() {
    	em = factory.createEntityManager();
    	Query q = em.createQuery("SELECT u FROM Delivery u WHERE u.active=true");
    	List<Delivery> deliveryList2 = new ArrayList<Delivery>();
    	try {
    	    List<Delivery> deliveryList = q.getResultList();
        	for(Delivery delivery : deliveryList) {
        		deliveryList2.add(delivery);
        	}
    	} catch(NoResultException e) {
    		System.out.println("No Results Exception");
    	}
    	return deliveryList2;
    }
}