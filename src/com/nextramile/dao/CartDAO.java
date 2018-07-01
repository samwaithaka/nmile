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
import com.nextramile.models.ShoppingCart;
import com.nextramile.models.ShoppingCartItem;

/**
 * @author Samuel
 *
 */
public class CartDAO {
	private static final String PERSISTENCE_UNIT_NAME = "nextramile";
    private static EntityManager em;
    private static EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

    public static ShoppingCart addShoppingCart(ShoppingCart shoppingCart) {
    	ShoppingCart shoppingCart2 = findPendingShoppingCart(shoppingCart.getCustomer());
    	if(shoppingCart2.getId() == 0) {
	    	shoppingCart.setCreatedOn(new Timestamp(System.currentTimeMillis()));
	    	shoppingCart.setEditedOn(new Timestamp(System.currentTimeMillis()));
	    	shoppingCart.setCreatedBy(shoppingCart.getEditedBy());
	    	em = factory.createEntityManager();
	        em.getTransaction().begin();
	        em.persist(shoppingCart);
	        em.getTransaction().commit();
	        em.close();
    	} else {
    		shoppingCart = shoppingCart2;
    	}
        return shoppingCart;
    }
    
    public static ShoppingCart updateShoppingCart(ShoppingCart shoppingCart) {
    	em = factory.createEntityManager();
        em.getTransaction().begin();
        ShoppingCart shoppingCart2 = em.find(ShoppingCart.class, shoppingCart.getId());
        shoppingCart2.setEditedOn(new Timestamp(System.currentTimeMillis()));
        shoppingCart2.setEditedBy(shoppingCart.getEditedBy());
        shoppingCart2.setCustomer(shoppingCart.getCustomer());
        shoppingCart2.setActive(shoppingCart.getActive());
        em.persist(shoppingCart2);
        em.getTransaction().commit();
        em.close();
        return shoppingCart2;
    }
    
    public static ShoppingCart find(int id) {
     	em = factory.createEntityManager();
     	ShoppingCart shoppingCart = em.find(ShoppingCart.class, id);
     	em.close();
     	return shoppingCart;
    }
    
    public static ShoppingCart findPendingShoppingCart(Customer customer) {
    	em = factory.createEntityManager();
    	Query q = em.createQuery("select u from ShoppingCart u WHERE u.customer = :customer and u.active = true");
    	q.setParameter("customer", customer);
    	ShoppingCart shoppingCart = new ShoppingCart();
    	try {
    	    shoppingCart = (ShoppingCart) q.getSingleResult();
    	    int total = 0;
    	    for(ShoppingCartItem cartItem : shoppingCart.getShoppingCartItems()) {
    	    	total += cartItem.getQuantity() * cartItem.getProduct().getPrice();
    	    }
    	    shoppingCart.setTotal(total);
    	} catch(NoResultException e) {
    		//
    	}
        em.close();
        return shoppingCart;
    }
    
    @SuppressWarnings("unchecked")
 	public static List<ShoppingCart> getShoppingCartList() {
     	em = factory.createEntityManager();
     	Query q = em.createQuery("SELECT u FROM ShoppingCart u WHERE u.active=true");
     	List<ShoppingCart> shoppingCartList2 = new ArrayList<ShoppingCart>();
     	try {
     	    List<ShoppingCart> shoppingCartList = q.getResultList();
         	for(ShoppingCart shoppingCart : shoppingCartList) {
         		shoppingCartList2.add(shoppingCart);
         	}
     	} catch(NoResultException e) {
     		System.out.println("No Results Exception");
     	}
     	return shoppingCartList2;
     }
}