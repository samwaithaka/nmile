package com.nextramile.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.nextramile.models.Blog;
import com.nextramile.models.Product;

/**
 * @author Samuel
 *
 */
public class ProductDAO {
	private static final String PERSISTENCE_UNIT_NAME = "nextramile";
    private static EntityManager em;
    private static EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

    public static Product addProduct(Product product) {
    	String slug = createUniqueSlug(product);
    	product.setSlug(slug);
    	product.setCreatedOn(new Timestamp(System.currentTimeMillis()));
    	product.setEditedOn(new Timestamp(System.currentTimeMillis()));
    	product.setCreatedBy(product.getEditedBy());
    	em = factory.createEntityManager();
        em.getTransaction().begin();
        em.persist(product);
        em.getTransaction().commit();
        em.close();
        return product;
    }
    
    public static Product updateProduct(Product product) {
    	em = factory.createEntityManager();
        em.getTransaction().begin();
        Product product2 = em.find(Product.class, product.getId());
        product2.setEditedOn(new Timestamp(System.currentTimeMillis()));
        product2.setEditedBy(product.getEditedBy());
        product2.setProductName(product.getProductName());
        product2.setSlug(product.getSlug());
        product2.setDescription(product.getDescription());
        product2.setFileName(product.getFileName());
        product2.setPrice(product.getPrice());
        product2.setRefBlogPost(product.getRefBlogPost());
        product2.setShareMessage(product.getShareMessage());
        product2.setActive(product.getActive());
        em.persist(product2);
        em.getTransaction().commit();
        em.close();
        return product2;
    }
    
    private static String createUniqueSlug(Product product) {
    	String slug = product.getSlug();
    	product = findBySlug(slug);
    	if(product.getId() > 0) {
    		String suffix = slug.substring(slug.lastIndexOf("-")+1);
        	String subSlug = slug.replace(suffix, "");
        	try {
        		int value = Integer.parseInt(suffix);
        	    value = value + 1;
        	    suffix = "" + value;
        	} catch(NumberFormatException e) {
        		suffix = suffix + "-1";
        	}
        	slug = subSlug + suffix;
    	}
    	return slug;
    }
    
    public static Product findBySlug(String slug) {
    	em = factory.createEntityManager();
    	Query q = em.createQuery("select u from Product u WHERE u.slug = :slug");
        q.setParameter("slug", slug);
        Product product = new Product();
        try {
        	product = (Product) q.getSingleResult();
        } catch(NoResultException e) {}
     	em.close();
     	return product;
    }

    public static Product find(int id) {
     	em = factory.createEntityManager();
     	Product product = em.find(Product.class, id);
     	em.close();
     	return product;
    }
    
    public static Product findByProductName(String productName) {
    	em = factory.createEntityManager();
    	Query q = em.createQuery("select u from Product u WHERE u.productName = :productName");
        q.setParameter("productName", productName);
     	Product product = (Product) q.getSingleResult();
     	em.close();
     	return product;
    }
    
    @SuppressWarnings("unchecked")
	public static List<Product> getProductList() {
    	em = factory.createEntityManager();
    	Query q = em.createQuery("SELECT u FROM Product u WHERE u.active=true");
    	List<Product> productList2 = new ArrayList<Product>();
    	try {
    	    List<Product> productList = q.getResultList();
        	for(Product product : productList) {
        		product.setRefBlogPost(new Blog());
        		productList2.add(product);
        	}
    	} catch(NoResultException e) {
    		System.out.println("No Results Exception");
    	}
    	return productList2;
    }
}