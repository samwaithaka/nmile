package com.nextramile.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.nextramile.models.ShoppingCart;
import com.nextramile.models.ShoppingCartItem;

/**
 * @author Samuel
 *
 */
public class CartItemDAO {
	private static final String PERSISTENCE_UNIT_NAME = "nextramile";
    private static EntityManager em;
    private static EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

    public static ShoppingCartItem addShoppingCartItem(ShoppingCartItem shoppingCartItem) {
    	ShoppingCartItem shoppingCartItem2 = findPendingShoppingCartItem(shoppingCartItem);
    	if(shoppingCartItem2.getId() == 0) {
	    	shoppingCartItem.setCreatedOn(new Timestamp(System.currentTimeMillis()));
	    	shoppingCartItem.setEditedOn(new Timestamp(System.currentTimeMillis()));
	    	shoppingCartItem.setCreatedBy(shoppingCartItem.getEditedBy());
	    	em = factory.createEntityManager();
	        em.getTransaction().begin();
	        em.persist(shoppingCartItem);
	        em.getTransaction().commit();
	        em.close();
    	} else {
    		shoppingCartItem.setId(shoppingCartItem2.getId());
    		shoppingCartItem.setActive(true);
    		shoppingCartItem = updateShoppingCartItem(shoppingCartItem);
    	}
        return shoppingCartItem;
    }
    
    public static ShoppingCartItem updateShoppingCartItem(ShoppingCartItem shoppingCartItem) {
    	em = factory.createEntityManager();
        em.getTransaction().begin();
        ShoppingCartItem shoppingCartItem2 = em.find(ShoppingCartItem.class, shoppingCartItem.getId());
        shoppingCartItem2.setEditedOn(new Timestamp(System.currentTimeMillis()));
        shoppingCartItem2.setEditedBy(shoppingCartItem.getEditedBy());
        shoppingCartItem2.setShoppingCart(shoppingCartItem.getShoppingCart());
        shoppingCartItem2.setProduct(shoppingCartItem.getProduct());
        shoppingCartItem2.setQuantity(shoppingCartItem.getQuantity());
        shoppingCartItem2.setActive(shoppingCartItem.getActive());
        em.persist(shoppingCartItem2);
        em.getTransaction().commit();
        em.close();
        return shoppingCartItem2;
    }
    
    public static ShoppingCartItem find(int id) {
     	em = factory.createEntityManager();
     	ShoppingCartItem shoppingCartItem = em.find(ShoppingCartItem.class, id);
     	em.close();
     	return shoppingCartItem;
    }
    
    public static ShoppingCartItem findPendingShoppingCartItem(ShoppingCartItem shoppingCartItem) {
    	em = factory.createEntityManager();
    	Query q = em.createQuery("select u from ShoppingCartItem u WHERE u.shoppingCart = :shoppingCart and u.product = :product");
    	q.setParameter("shoppingCart", shoppingCartItem.getShoppingCart());
    	q.setParameter("product", shoppingCartItem.getProduct());
    	shoppingCartItem = new ShoppingCartItem();
    	try {
    	    shoppingCartItem = (ShoppingCartItem) q.getSingleResult();
    	} catch(NoResultException e) {
    		//
    	}
        em.close();
        return shoppingCartItem;
    }
    
    @SuppressWarnings("unchecked")
  	public static List<ShoppingCartItem> getCartItems(ShoppingCart shoppingCart) {
      	em = factory.createEntityManager();
      	Query q = em.createQuery("SELECT u FROM ShoppingCartItem u WHERE u.shoppingCart = :shoppingCart AND u.active=true");
      	List<ShoppingCartItem> shoppingCartItemList2 = new ArrayList<ShoppingCartItem>();
      	q.setParameter("shoppingCart", shoppingCart);
      	try {
      	    List<ShoppingCartItem> shoppingCartItemList = q.getResultList();
          	for(ShoppingCartItem shoppingCartItem : shoppingCartItemList) {
          		shoppingCartItemList2.add(shoppingCartItem);
          	}
      	} catch(NoResultException e) {
      		System.out.println("No Results Exception");
      	}
      	return shoppingCartItemList2;
      }
    
    @SuppressWarnings("unchecked")
  	public static List<ShoppingCartItem> getShoppingCartItemList() {
      	em = factory.createEntityManager();
      	Query q = em.createQuery("SELECT u FROM ShoppingCartItem u WHERE u.active=true");
      	List<ShoppingCartItem> shoppingCartItemList2 = new ArrayList<ShoppingCartItem>();
      	try {
      	    List<ShoppingCartItem> shoppingCartItemList = q.getResultList();
          	for(ShoppingCartItem shoppingCartItem : shoppingCartItemList) {
          		shoppingCartItemList2.add(shoppingCartItem);
          	}
      	} catch(NoResultException e) {
      		System.out.println("No Results Exception");
      	}
      	return shoppingCartItemList2;
      }
}