package com.nextramile.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.nextramile.models.Blog;

/**
 * @author Samuel
 *
 */
public class BlogDAO {
	private static final String PERSISTENCE_UNIT_NAME = "nextramile";
    private static EntityManager em;
    private static EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

    public static Blog addBlog(Blog blog) {
    	blog.setSlug(createUniqueSlug(blog));
    	blog.setCreatedOn(new Timestamp(System.currentTimeMillis()));
    	blog.setEditedOn(new Timestamp(System.currentTimeMillis()));
    	blog.setCreatedBy(blog.getEditedBy());
    	em = factory.createEntityManager();
        em.getTransaction().begin();
        em.persist(blog);
        em.getTransaction().commit();
        em.close();
        return blog;
    }
    
    public static Blog updateBlog(Blog blog) {
    	em = factory.createEntityManager();
        em.getTransaction().begin();
        Blog blog2 = em.find(Blog.class, blog.getId());
        blog2.setEditedOn(new Timestamp(System.currentTimeMillis()));
        blog2.setEditedBy(blog.getEditedBy());
        blog2.setTitle(blog.getTitle());
        blog2.setDescriptionMeta(blog.getDescriptionMeta());
        blog2.setKeywordsMeta(blog.getKeywordsMeta());
        blog2.setSlug(blog.getSlug());
        blog2.setImageFileName(blog.getImageFileName());
        blog2.setShortText(blog.getShortText());
        blog2.setLongText(blog.getLongText());
        blog2.setActive(blog.getActive());
        em.persist(blog2);
        em.getTransaction().commit();
        em.close();
        return blog2;
    }
    
    
    private static String createUniqueSlug(Blog blog) {
    	blog = findBySlug(blog.getSlug());
    	if(blog.getId() > 0) {
    		String slug = blog.getSlug();
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
        	return slug;
    	} else {
    		return blog.getSlug();
    	}
    }
    
    public static Blog find(int id) {
     	em = factory.createEntityManager();
     	Blog blog = em.find(Blog.class, id);
     	em.close();
     	return blog;
    }
    
    public static Blog findBySlug(String slug) {
    	em = factory.createEntityManager();
    	Query q = em.createQuery("select u from Blog u WHERE u.slug = :slug");
        q.setParameter("slug", slug);
     	Blog blog = (Blog) q.getSingleResult();
     	em.close();
     	return blog;
    }
    
    @SuppressWarnings("unchecked")
	public static List<Blog> getBlogList() {
    	em = factory.createEntityManager();
    	Query q = em.createQuery("SELECT u FROM Blog u WHERE u.active=true");
    	List<Blog> blogList2 = new ArrayList<Blog>();
    	try {
    	    List<Blog> blogList = q.getResultList();
        	for(Blog blog : blogList) {
        		blogList2.add(blog);
        	}
    	} catch(NoResultException e) {
    		System.out.println("No Results Exception");
    	}
    	return blogList2;
    }
    
    @SuppressWarnings("unchecked")
	public static List<HashMap<String,String>> getNavigation() {
    	em = factory.createEntityManager();
    	Query q = em.createQuery("SELECT u FROM Blog u WHERE u.active=true");
    	List<HashMap<String,String>> navigation = new ArrayList<HashMap<String,String>>();
    	try {
    	    List<Blog> blogList = q.getResultList();
        	for(Blog blog : blogList) {
        		HashMap<String,String> item = new HashMap<String,String>();
        		item.put(blog.getSlug(), blog.getTitle());
        		navigation.add(item);
        	}
    	} catch(NoResultException e) {
    		System.out.println("No Results Exception");
    	}
    	return navigation;
    }
}