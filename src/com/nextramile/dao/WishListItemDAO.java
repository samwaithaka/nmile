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
import com.nextramile.models.Product;
import com.nextramile.models.WishList;
import com.nextramile.models.WishListItem;

/**
 * @author Samuel
 *
 */
public class WishListItemDAO {
	private static final String PERSISTENCE_UNIT_NAME = "nextramile";
    private static EntityManager em;
    private static EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

    public static WishListItem addWishListItem(WishListItem wishListItem) {
    	WishListItem wishListItem2 = findPendingWishListItem(wishListItem);
    	if(wishListItem2.getId() == 0) {
	    	wishListItem.setCreatedOn(new Timestamp(System.currentTimeMillis()));
	    	wishListItem.setEditedOn(new Timestamp(System.currentTimeMillis()));
	    	wishListItem.setCreatedBy(wishListItem.getEditedBy());
	    	em = factory.createEntityManager();
	        em.getTransaction().begin();
	        em.persist(wishListItem);
	        em.getTransaction().commit();
	        em.close();
    	} else {
    		wishListItem.setId(wishListItem2.getId());
    		wishListItem.setActive(true);
    		wishListItem = updateWishListItem(wishListItem);
    	}
        return wishListItem;
    }
    
    public static WishListItem updateWishListItem(WishListItem wishListItem) {
    	em = factory.createEntityManager();
    	em.getTransaction().begin();
    	WishListItem wishListItem2 = em.find(WishListItem.class, wishListItem.getId());
    	if(wishListItem2 != null) {
    		wishListItem2.setEditedOn(new Timestamp(System.currentTimeMillis()));
    		wishListItem2.setEditedBy(wishListItem.getEditedBy());
    		wishListItem2.setWishList(wishListItem.getWishList());
    		wishListItem2.setProduct(wishListItem.getProduct());
    		wishListItem2.setQuantity(wishListItem.getQuantity());
    		wishListItem2.setActive(wishListItem.getActive());
    		em.persist(wishListItem2);
    		em.getTransaction().commit();
    	}
    	em.close();
    	return wishListItem2;
    }
    
    public static WishListItem find(int id) {
     	em = factory.createEntityManager();
     	WishListItem wishListItem = em.find(WishListItem.class, id);
     	em.close();
     	return wishListItem;
    }
    
    public static WishListItem findPendingWishListItem(WishListItem wishListItem) {
    	em = factory.createEntityManager();
    	Query q = em.createQuery("select u from WishListItem u WHERE u.wishList = :wishList and u.product = :product");
    	q.setParameter("wishList", wishListItem.getWishList());
    	q.setParameter("product", wishListItem.getProduct());
    	wishListItem = new WishListItem();
    	try {
    	    wishListItem = (WishListItem) q.getSingleResult();
    	} catch(NoResultException e) {
    		//
    	}
        em.close();
        return wishListItem;
    }
    
    public static WishListItem getWishListItemByCustomerProduct(Customer customer, Product product) {
    	em = factory.createEntityManager();
    	Query q = em.createQuery("select u from WishListItem u WHERE u.wishList = :wishList and u.product = :product and u.active = true");
    	WishList wishList = WishListDAO.findPendingWishList(customer);
    	q.setParameter("wishList", wishList);
    	q.setParameter("product", product);
    	WishListItem wishListItem = new WishListItem();
    	try {
    	    wishListItem = (WishListItem) q.getSingleResult();
    	} catch(NoResultException e) {
    		//
    	}
        em.close();
        return wishListItem;
    }
    
    @SuppressWarnings("unchecked")
  	public static List<WishListItem> getWishListItems(WishList wishList) {
      	em = factory.createEntityManager();
      	Query q = em.createQuery("SELECT u FROM WishListItem u WHERE u.wishList = :wishList AND u.active=true");
      	List<WishListItem> wishListItemList2 = new ArrayList<WishListItem>();
      	q.setParameter("wishList", wishList);
      	try {
      	    List<WishListItem> wishListItemList = q.getResultList();
          	for(WishListItem wishListItem : wishListItemList) {
          		wishListItemList2.add(wishListItem);
          	}
      	} catch(NoResultException e) {
      		System.out.println("No Results Exception");
      	}
      	return wishListItemList2;
      }
    
    @SuppressWarnings("unchecked")
  	public static List<WishListItem> getWishListItemList() {
      	em = factory.createEntityManager();
      	Query q = em.createQuery("SELECT u FROM WishListItem u WHERE u.active=true");
      	List<WishListItem> wishListItemList2 = new ArrayList<WishListItem>();
      	try {
      	    List<WishListItem> wishListItemList = q.getResultList();
          	for(WishListItem wishListItem : wishListItemList) {
          		wishListItemList2.add(wishListItem);
          	}
      	} catch(NoResultException e) {
      		System.out.println("No Results Exception");
      	}
      	return wishListItemList2;
      }
}