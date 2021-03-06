package com.nextramile.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.nextramile.models.DeliveryAddress;

/**
 * @author Samuel
 *
 */
public class DeliveryAddressDAO {
	private static final String PERSISTENCE_UNIT_NAME = "nextramile";
    private static EntityManager em;
    private static EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

    public static DeliveryAddress addDeliveryAddress(DeliveryAddress deliveryAddress) {
    	deliveryAddress.setCreatedOn(new Timestamp(System.currentTimeMillis()));
    	deliveryAddress.setEditedOn(new Timestamp(System.currentTimeMillis()));
    	deliveryAddress.setCreatedBy(deliveryAddress.getEditedBy());
    	em = factory.createEntityManager();
        em.getTransaction().begin();
        em.persist(deliveryAddress);
        em.getTransaction().commit();
        em.close();
        return deliveryAddress;
    }
    
    public static DeliveryAddress updateDeliveryAddress(DeliveryAddress deliveryAddress) {
    	em = factory.createEntityManager();
        em.getTransaction().begin();
        DeliveryAddress deliveryAddress2 = em.find(DeliveryAddress.class, deliveryAddress.getId());
        deliveryAddress2.setEditedOn(new Timestamp(System.currentTimeMillis()));
        deliveryAddress2.setEditedBy(deliveryAddress.getEditedBy());
        deliveryAddress2.setPhysicalAddress(deliveryAddress.getPhysicalAddress());
        deliveryAddress2.setDescription(deliveryAddress.getDescription());
        deliveryAddress2.setActive(deliveryAddress.getActive());
        deliveryAddress2.setLocation(deliveryAddress.getLocation());
        em.persist(deliveryAddress2);
        em.getTransaction().commit();
        em.close();
        return deliveryAddress2;
    }
    
    public static DeliveryAddress find(int id) {
     	em = factory.createEntityManager();
     	DeliveryAddress deliveryAddress = em.find(DeliveryAddress.class, id);
     	em.close();
     	return deliveryAddress;
    }
    
    @SuppressWarnings("unchecked")
	public static List<DeliveryAddress> getDeliveryAddressList() {
    	em = factory.createEntityManager();
    	Query q = em.createQuery("SELECT u FROM DeliveryAddress u WHERE u.active=true");
    	List<DeliveryAddress> deliveryAddressList2 = new ArrayList<DeliveryAddress>();
    	try {
    	    List<DeliveryAddress> deliveryAddressList = q.getResultList();
        	for(DeliveryAddress deliveryAddress : deliveryAddressList) {
        		deliveryAddressList2.add(deliveryAddress);
        	}
    	} catch(NoResultException e) {
    		System.out.println("No Results Exception");
    	}
    	return deliveryAddressList2;
    }
}