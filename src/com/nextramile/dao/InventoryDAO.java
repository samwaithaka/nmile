package com.nextramile.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.nextramile.models.Inventory;

/**
 * @author Samuel
 *
 */
public class InventoryDAO {
	private static final String PERSISTENCE_UNIT_NAME = "nextramile";
    private static EntityManager em;
    private static EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

    public static Inventory addInventory(Inventory inventory) {
    	inventory.setCreatedOn(new Timestamp(System.currentTimeMillis()));
    	inventory.setEditedOn(new Timestamp(System.currentTimeMillis()));
    	inventory.setCreatedBy(inventory.getEditedBy());
    	em = factory.createEntityManager();
        em.getTransaction().begin();
        em.persist(inventory);
        em.getTransaction().commit();
        em.close();
        return inventory;
    }
    
    public static Inventory updateInventory(Inventory inventory) {
    	em = factory.createEntityManager();
        em.getTransaction().begin();
        Inventory inventory2 = em.find(Inventory.class, inventory.getId());
        inventory2.setEditedOn(new Timestamp(System.currentTimeMillis()));
        inventory2.setEditedBy(inventory.getEditedBy());
        inventory2.setProduct(inventory.getProduct());
        inventory2.setSize(inventory.getSize());
        inventory2.setColor(inventory.getColor());
        inventory2.setQuantity(inventory.getQuantity());
        inventory2.setActive(inventory.getActive());
        em.persist(inventory2);
        em.getTransaction().commit();
        em.close();
        return inventory2;
    }
    
    public static Inventory find(int id) {
     	em = factory.createEntityManager();
     	Inventory inventory = em.find(Inventory.class, id);
     	em.close();
     	return inventory;
    }
    
    public static Inventory findByInventoryName(String inventoryName) {
    	em = factory.createEntityManager();
    	Query q = em.createQuery("select u from Inventory u WHERE u.inventoryName = :inventoryName");
        q.setParameter("inventoryName", inventoryName);
     	Inventory inventory = (Inventory) q.getSingleResult();
     	em.close();
     	return inventory;
    }
    
    @SuppressWarnings("unchecked")
	public static List<Inventory> getInventoryList() {
    	em = factory.createEntityManager();
    	Query q = em.createQuery("SELECT u FROM Inventory u WHERE u.active=true");
    	List<Inventory> inventoryList2 = new ArrayList<Inventory>();
    	try {
    	    List<Inventory> inventoryList = q.getResultList();
        	for(Inventory inventory : inventoryList) {
        		inventoryList2.add(inventory);
        	}
    	} catch(NoResultException e) {
    		System.out.println("No Results Exception");
    	}
    	return inventoryList2;
    }
}