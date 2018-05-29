package com.hcare.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.hcare.models.Color;

/**
 * @author Samuel
 *
 */
public class ColorDAO {
	private static final String PERSISTENCE_UNIT_NAME = "hcare";
    private static EntityManager em;
    private static EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

    public static Color addColor(Color color) {
    	color.setCreatedOn(new Timestamp(System.currentTimeMillis()));
    	color.setEditedOn(new Timestamp(System.currentTimeMillis()));
    	color.setCreatedBy(color.getEditedBy());
    	em = factory.createEntityManager();
        em.getTransaction().begin();
        em.persist(color);
        em.getTransaction().commit();
        em.close();
        return color;
    }
    
    public static Color updateColor(Color color) {
    	em = factory.createEntityManager();
        em.getTransaction().begin();
        Color color2 = em.find(Color.class, color.getId());
        color2.setEditedOn(new Timestamp(System.currentTimeMillis()));
        color2.setEditedBy(color.getEditedBy());
        color2.setColorName(color.getColorName());
        color2.setActive(color.getActive());
        em.persist(color2);
        em.getTransaction().commit();
        em.close();
        return color2;
    }
    
    public static Color find(int id) {
     	em = factory.createEntityManager();
     	Color color = em.find(Color.class, id);
     	em.close();
     	return color;
    }
    
    public static Color findByColorName(String colorName) {
    	em = factory.createEntityManager();
    	Query q = em.createQuery("select u from Color u WHERE u.colorName = :colorName");
        q.setParameter("colorName", colorName);
     	Color color = (Color) q.getSingleResult();
     	em.close();
     	return color;
    }
    
    @SuppressWarnings("unchecked")
	public static List<Color> getColorList() {
    	em = factory.createEntityManager();
    	Query q = em.createQuery("SELECT u FROM Color u WHERE u.active=true");
    	List<Color> colorList2 = new ArrayList<Color>();
    	try {
    	    List<Color> colorList = q.getResultList();
        	for(Color color : colorList) {
        		colorList2.add(color);
        	}
    	} catch(NoResultException e) {
    		System.out.println("No Results Exception");
    	}
    	return colorList2;
    }
}