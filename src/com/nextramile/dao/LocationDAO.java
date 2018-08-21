/**
 * 
 */
package com.nextramile.dao;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.nextramile.models.Location;

/**
 * @author Samuel
 *
 */
public class LocationDAO {
	private static final String PERSISTENCE_UNIT_NAME = "nextramile";
    private static EntityManager em;
    private static EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

    public static Location addLocation(Location location) {
    	location.setCreatedOn(new Timestamp(System.currentTimeMillis()));
    	em = factory.createEntityManager();
        em.getTransaction().begin();
        em.persist(location);
        em.getTransaction().commit();
        em.close();
        return location;
    }
    
    public static void updateLocation(Location location) {
    	em = factory.createEntityManager();
        em.getTransaction().begin();
        Location location2 = em.find(Location.class, location.getId());
        location.setEditedBy(location.getEditedBy());
        location2.setEditedOn(new Timestamp(System.currentTimeMillis()));
        location2.setLocationName(location.getLocationName());
        location2.setDeliveryFee(location.getDeliveryFee());
        location2.setActive(location.getActive());
        em.persist(location2);
        em.getTransaction().commit();
        em.close();
    }
    
    public static Location find(int id) {
     	em = factory.createEntityManager();
     	Location location = em.find(Location.class, id);
     	em.close();
     	return location;
     }
    
    @SuppressWarnings("unchecked")
	public static List<Location> getLocationList() {
    	em = factory.createEntityManager();
    	Query q = em.createQuery("SELECT r FROM Location r WHERE r.active=true");
    	List<Location> locationList = q.getResultList();
    	return locationList;
    }
}
