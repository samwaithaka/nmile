package com.nextramile.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.nextramile.models.BlogCategory;

/**
 * @author Samuel
 *
 */
public class BlogCategoryDAO {
	private static final String PERSISTENCE_UNIT_NAME = "nextramile";
    private static EntityManager em;
    private static EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

    public static BlogCategory addBlogCategory(BlogCategory blogCategory) {
    	blogCategory.setCreatedOn(new Timestamp(System.currentTimeMillis()));
    	blogCategory.setEditedOn(new Timestamp(System.currentTimeMillis()));
    	blogCategory.setCreatedBy(blogCategory.getEditedBy());
    	em = factory.createEntityManager();
        em.getTransaction().begin();
        em.persist(blogCategory);
        em.getTransaction().commit();
        em.close();
        return blogCategory;
    }
    
    public static BlogCategory updateBlogCategory(BlogCategory blogCategory) {
    	em = factory.createEntityManager();
        em.getTransaction().begin();
        BlogCategory blogCategory2 = em.find(BlogCategory.class, blogCategory.getId());
        blogCategory2.setEditedOn(new Timestamp(System.currentTimeMillis()));
        blogCategory2.setEditedBy(blogCategory.getEditedBy());
        blogCategory2.setBlogCategoryName(blogCategory.getBlogCategoryName());
        blogCategory2.setActive(blogCategory.getActive());
        em.persist(blogCategory2);
        em.getTransaction().commit();
        em.close();
        return blogCategory2;
    }
    
    public static BlogCategory find(int id) {
     	em = factory.createEntityManager();
     	BlogCategory blogCategory = em.find(BlogCategory.class, id);
     	em.close();
     	return blogCategory;
    }
    
    public static BlogCategory findByBlogCategoryName(String blogCategoryName) {
    	em = factory.createEntityManager();
    	Query q = em.createQuery("select u from BlogCategory u WHERE u.blogCategoryName = :blogCategoryName");
        q.setParameter("blogCategoryName", blogCategoryName);
     	BlogCategory blogCategory = (BlogCategory) q.getSingleResult();
     	em.close();
     	return blogCategory;
    }
    
    @SuppressWarnings("unchecked")
	public static List<BlogCategory> getBlogCategoryList() {
    	em = factory.createEntityManager();
    	Query q = em.createQuery("SELECT u FROM BlogCategory u WHERE u.active=true");
    	List<BlogCategory> blogCategoryList2 = new ArrayList<BlogCategory>();
    	try {
    	    List<BlogCategory> blogCategoryList = q.getResultList();
        	for(BlogCategory blogCategory : blogCategoryList) {
        		blogCategoryList2.add(blogCategory);
        	}
    	} catch(NoResultException e) {
    		System.out.println("No Results Exception");
    	}
    	return blogCategoryList2;
    }
}