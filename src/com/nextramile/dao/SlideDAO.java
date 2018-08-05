/**
 * 
 */
package com.nextramile.dao;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.nextramile.models.Slide;

/**
 * @author Samuel
 *
 */
public class SlideDAO {
	private static final String PERSISTENCE_UNIT_NAME = "nextramile";
    private static EntityManager em;
    private static EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

    public static Slide addSlide(Slide slide) {
    	slide.setCreatedOn(new Timestamp(System.currentTimeMillis()));
    	em = factory.createEntityManager();
        em.getTransaction().begin();
        em.persist(slide);
        em.getTransaction().commit();
        em.close();
        return slide;
    }
    
    public static void updateSlide(Slide slide) {
    	em = factory.createEntityManager();
        em.getTransaction().begin();
        Slide slide2 = em.find(Slide.class, slide.getId());
        slide.setEditedBy(slide.getEditedBy());
        slide2.setEditedOn(new Timestamp(System.currentTimeMillis()));
        slide2.setSlideTitle(slide.getSlideTitle());
        slide2.setSlideText(slide.getSlideText());
        slide2.setSlidePhoto(slide.getSlidePhoto());
        slide2.setDeleted(slide.getDeleted());
        slide2.setActive(slide.getActive());
        em.persist(slide2);
        em.getTransaction().commit();
        em.close();
    }
    
    public static Slide find(int id) {
     	em = factory.createEntityManager();
     	Slide slide = em.find(Slide.class, id);
     	em.close();
     	return slide;
     }
    
    @SuppressWarnings("unchecked")
	public static List<Slide> getSlideList() {
    	em = factory.createEntityManager();
    	Query q = em.createQuery("SELECT r FROM Slide r WHERE r.deleted=false");
    	List<Slide> slideList = q.getResultList();
    	return slideList;
    }
}
