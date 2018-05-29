package com.hcare.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.hcare.models.Segment;

/**
 * @author Samuel
 *
 */
public class SegmentDAO {
	private static final String PERSISTENCE_UNIT_NAME = "hcare";
    private static EntityManager em;
    private static EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

    public static Segment addSegment(Segment segment) {
    	segment.setCreatedOn(new Timestamp(System.currentTimeMillis()));
    	segment.setEditedOn(new Timestamp(System.currentTimeMillis()));
    	segment.setCreatedBy(segment.getEditedBy());
    	em = factory.createEntityManager();
        em.getTransaction().begin();
        em.persist(segment);
        em.getTransaction().commit();
        em.close();
        return segment;
    }
    
    public static Segment updateSegment(Segment segment) {
    	em = factory.createEntityManager();
        em.getTransaction().begin();
        Segment segment2 = em.find(Segment.class, segment.getId());
        segment2.setEditedOn(new Timestamp(System.currentTimeMillis()));
        segment2.setEditedBy(segment.getEditedBy());
        segment2.setSegmentName(segment.getSegmentName());
        segment2.setActive(segment.getActive());
        em.persist(segment2);
        em.getTransaction().commit();
        em.close();
        return segment2;
    }
    
    public static Segment find(int id) {
     	em = factory.createEntityManager();
     	Segment segment = em.find(Segment.class, id);
     	em.close();
     	return segment;
    }
    
    public static Segment findBySegmentName(String segmentName) {
    	em = factory.createEntityManager();
    	Query q = em.createQuery("select u from Segment u WHERE u.segmentName = :segmentName");
        q.setParameter("segmentName", segmentName);
     	Segment segment = (Segment) q.getSingleResult();
     	em.close();
     	return segment;
    }
    
    @SuppressWarnings("unchecked")
	public static List<Segment> getSegmentList() {
    	em = factory.createEntityManager();
    	Query q = em.createQuery("SELECT u FROM Segment u WHERE u.active=true");
    	List<Segment> segmentList2 = new ArrayList<Segment>();
    	try {
    	    List<Segment> segmentList = q.getResultList();
        	for(Segment segment : segmentList) {
        		segmentList2.add(segment);
        	}
    	} catch(NoResultException e) {
    		System.out.println("No Results Exception");
    	}
    	return segmentList2;
    }
}