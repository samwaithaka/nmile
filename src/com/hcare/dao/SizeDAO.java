package com.hcare.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.hcare.models.Size;

/**
 * @author Samuel
 *
 */
public class SizeDAO {
	private static final String PERSISTENCE_UNIT_NAME = "hcare";
    private static EntityManager em;
    private static EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

    public static Size addSize(Size size) {
    	size.setCreatedOn(new Timestamp(System.currentTimeMillis()));
    	size.setEditedOn(new Timestamp(System.currentTimeMillis()));
    	size.setCreatedBy(size.getEditedBy());
    	em = factory.createEntityManager();
        em.getTransaction().begin();
        em.persist(size);
        em.getTransaction().commit();
        em.close();
        return size;
    }
    
    public static Size updateSize(Size size) {
    	em = factory.createEntityManager();
        em.getTransaction().begin();
        Size size2 = em.find(Size.class, size.getId());
        size2.setEditedOn(new Timestamp(System.currentTimeMillis()));
        size2.setEditedBy(size.getEditedBy());
        size2.setSizeName(size.getSizeName());
        size2.setActive(size.getActive());
        em.persist(size2);
        em.getTransaction().commit();
        em.close();
        return size2;
    }
    
    public static Size find(int id) {
     	em = factory.createEntityManager();
     	Size size = em.find(Size.class, id);
     	em.close();
     	return size;
    }
    
    public static Size findBySizeName(String sizeName) {
    	em = factory.createEntityManager();
    	Query q = em.createQuery("select u from Size u WHERE u.sizeName = :sizeName");
        q.setParameter("sizeName", sizeName);
     	Size size = (Size) q.getSingleResult();
     	em.close();
     	return size;
    }
    
    @SuppressWarnings("unchecked")
	public static List<Size> getSizeList() {
    	em = factory.createEntityManager();
    	Query q = em.createQuery("SELECT u FROM Size u WHERE u.active=true");
    	List<Size> sizeList2 = new ArrayList<Size>();
    	try {
    	    List<Size> sizeList = q.getResultList();
        	for(Size size : sizeList) {
        		sizeList2.add(size);
        	}
    	} catch(NoResultException e) {
    		System.out.println("No Results Exception");
    	}
    	return sizeList2;
    }
}