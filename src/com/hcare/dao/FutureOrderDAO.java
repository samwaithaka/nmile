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
import com.hcare.models.FutureOrder;

/**
 * @author Samuel
 *
 */
public class FutureOrderDAO {
	private static final String PERSISTENCE_UNIT_NAME = "hcare";
    private static EntityManager em;
    private static EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

    public static FutureOrder addFutureOrder(FutureOrder futureOrder) {
    	futureOrder.setCreatedOn(new Timestamp(System.currentTimeMillis()));
    	futureOrder.setEditedOn(new Timestamp(System.currentTimeMillis()));
    	futureOrder.setCreatedBy(futureOrder.getEditedBy());
    	em = factory.createEntityManager();
        em.getTransaction().begin();
        em.persist(futureOrder);
        em.getTransaction().commit();
        em.close();
        return futureOrder;
    }
    
    public static void updateFutureOrder(FutureOrder futureOrder) {
    	em = factory.createEntityManager();
        em.getTransaction().begin();
        FutureOrder futureOrder2 = em.find(FutureOrder.class, futureOrder.getId());
        futureOrder2.setEditedOn(new Timestamp(System.currentTimeMillis()));
        futureOrder2.setEditedBy(futureOrder.getEditedBy());
        futureOrder2.setCustomer(futureOrder.getCustomer());
        futureOrder2.setProduct(futureOrder.getProduct());
        futureOrder2.setOrderDate(futureOrder.getOrderDate());
        futureOrder2.setRemark(futureOrder.getRemark());
        futureOrder2.setTaken(futureOrder.getTaken());
        futureOrder2.setActive(futureOrder.getActive());
        em.persist(futureOrder2);
        em.getTransaction().commit();
        em.close();
    }
    
    public static FutureOrder find(int id) {
     	em = factory.createEntityManager();
     	FutureOrder futureOrder = em.find(FutureOrder.class, id);
     	em.close();
     	return futureOrder;
    }
    
    public static List<FutureOrder> findByFutureOrdersByCustomer(Customer customer) {
    	em = factory.createEntityManager();
    	Query q = em.createQuery("select u from FutureOrder u WHERE u.customer=:customer");
        q.setParameter("customer", customer);
    	List<FutureOrder> futureOrderList2 = new ArrayList<FutureOrder>();
    	try {
    	    @SuppressWarnings("unchecked")
			List<FutureOrder> futureOrderList = q.getResultList();
        	for(FutureOrder futureOrder : futureOrderList) {
        		futureOrderList2.add(futureOrder);
        	}
    	} catch(NoResultException e) {
    		System.out.println("No Results Exception");
    	}
    	return futureOrderList2;
    }
    
    @SuppressWarnings("unchecked")
	public static List<FutureOrder> getFutureOrderList() {
    	em = factory.createEntityManager();
    	Query q = em.createQuery("SELECT u FROM FutureOrder u WHERE u.active=true");
    	List<FutureOrder> futureOrderList2 = new ArrayList<FutureOrder>();
    	try {
    	    List<FutureOrder> futureOrderList = q.getResultList();
        	for(FutureOrder futureOrder : futureOrderList) {
        		futureOrderList2.add(futureOrder);
        	}
    	} catch(NoResultException e) {
    		System.out.println("No Results Exception");
    	}
    	return futureOrderList2;
    }
}