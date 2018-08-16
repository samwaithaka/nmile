package com.nextramile.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.nextramile.models.ProductCategory;

/**
 * @author Samuel
 *
 */
public class ProductCategoryDAO {
	private static final String PERSISTENCE_UNIT_NAME = "nextramile";
    private static EntityManager em;
    private static EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

    public static ProductCategory addProductCategory(ProductCategory productCategory) {
    	productCategory.setCreatedOn(new Timestamp(System.currentTimeMillis()));
    	productCategory.setEditedOn(new Timestamp(System.currentTimeMillis()));
    	productCategory.setCreatedBy(productCategory.getEditedBy());
    	em = factory.createEntityManager();
        em.getTransaction().begin();
        em.persist(productCategory);
        em.getTransaction().commit();
        em.close();
        return productCategory;
    }
    
    public static ProductCategory updateProductCategory(ProductCategory productCategory) {
    	em = factory.createEntityManager();
        em.getTransaction().begin();
        ProductCategory productCategory2 = em.find(ProductCategory.class, productCategory.getId());
        productCategory2.setEditedOn(new Timestamp(System.currentTimeMillis()));
        productCategory2.setEditedBy(productCategory.getEditedBy());
        productCategory2.setProductCategoryName(productCategory.getProductCategoryName());
        productCategory2.setActive(productCategory.getActive());
        em.persist(productCategory2);
        em.getTransaction().commit();
        em.close();
        return productCategory2;
    }
    
    public static ProductCategory find(int id) {
     	em = factory.createEntityManager();
     	ProductCategory productCategory = em.find(ProductCategory.class, id);
     	em.close();
     	return productCategory;
    }
    
    /*public static ProductCategory findByProductCategoryName(String productCategoryName) {
    	em = factory.createEntityManager();
    	Query q = em.createQuery("select u from ProductCategory u WHERE u.productCategoryName = :productCategoryName");
        q.setParameter("productCategoryName", productCategoryName);
     	ProductCategory productCategory = (ProductCategory) q.getSingleResult();
     	em.close();
     	return productCategory;
    }*/
    
    @SuppressWarnings("unchecked")
	public static List<ProductCategory> getProductCategoryList() {
    	em = factory.createEntityManager();
    	Query q = em.createQuery("SELECT u FROM ProductCategory u WHERE u.active=true");
    	List<ProductCategory> productCategoryList2 = new ArrayList<ProductCategory>();
    	try {
    	    List<ProductCategory> productCategoryList = q.getResultList();
        	for(ProductCategory productCategory : productCategoryList) {
        		productCategoryList2.add(productCategory);
        	}
    	} catch(NoResultException e) {
    		System.out.println("No Results Exception");
    	}
    	return productCategoryList2;
    }
}