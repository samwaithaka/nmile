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
import com.nextramile.models.WishList;
import com.nextramile.models.WishListItem;

/**
 * @author Samuel
 *
 */
public class WishListDAO {
	private static final String PERSISTENCE_UNIT_NAME = "nextramile";
    private static EntityManager em;
    private static EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

    public static WishList addWishList(WishList wishList) {
    	WishList wishList2 = findPendingWishList(wishList.getCustomer());
    	if(wishList2.getId() == 0) {
	    	wishList.setCreatedOn(new Timestamp(System.currentTimeMillis()));
	    	wishList.setEditedOn(new Timestamp(System.currentTimeMillis()));
	    	wishList.setCreatedBy(wishList.getEditedBy());
	    	em = factory.createEntityManager();
	        em.getTransaction().begin();
	        em.persist(wishList);
	        em.getTransaction().commit();
	        em.close();
    	} else {
    		wishList = wishList2;
    	}
        return wishList;
    }
    
    public static WishList updateWishList(WishList wishList) {
    	em = factory.createEntityManager();
        em.getTransaction().begin();
        WishList wishList2 = em.find(WishList.class, wishList.getId());
        wishList2.setEditedOn(new Timestamp(System.currentTimeMillis()));
        wishList2.setEditedBy(wishList.getEditedBy());
        wishList2.setCustomer(wishList.getCustomer());
        wishList2.setActive(wishList.getActive());
        em.persist(wishList2);
        em.getTransaction().commit();
        em.close();
        return wishList2;
    }
    
    public static WishList find(int id) {
     	em = factory.createEntityManager();
     	WishList wishList = em.find(WishList.class, id);
     	em.close();
     	return wishList;
    }
    
    public static WishList findPendingWishList(Customer customer) {
    	em = factory.createEntityManager();
    	Query q = em.createQuery("select u from WishList u WHERE u.customer = :customer and u.active = true");
    	q.setParameter("customer", customer);
    	WishList wishList = new WishList();
    	try {
    	    wishList = (WishList) q.getSingleResult();
    	    int total = 0;
    	    for(WishListItem wishListItem : WishListItemDAO.getWishListItems(wishList)) {
    	    	total += wishListItem.getQuantity() * wishListItem.getProduct().getPrice();
    	    }
    	    wishList.setTotal(total);
    	} catch(NoResultException e) {
    		//
    	}
        em.close();
        return wishList;
    }
    
    @SuppressWarnings("unchecked")
 	public static List<WishList> getWishListList() {
     	em = factory.createEntityManager();
     	Query q = em.createQuery("SELECT u FROM WishList u WHERE u.active=true");
     	List<WishList> wishListList2 = new ArrayList<WishList>();
     	try {
     	    List<WishList> wishListList = q.getResultList();
         	for(WishList wishList : wishListList) {
         		wishListList2.add(wishList);
         	}
     	} catch(NoResultException e) {
     		System.out.println("No Results Exception");
     	}
     	return wishListList2;
     }
}