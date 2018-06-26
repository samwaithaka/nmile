package com.nextramile.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.nextramile.models.Reward;

/**
 * @author Samuel
 *
 */
public class RewardDAO {
	private static final String PERSISTENCE_UNIT_NAME = "nextramile";
    private static EntityManager em;
    private static EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

    public static Reward addReward(Reward reward) {
    	reward.setCreatedOn(new Timestamp(System.currentTimeMillis()));
    	reward.setEditedOn(new Timestamp(System.currentTimeMillis()));
    	reward.setCreatedBy(reward.getEditedBy());
    	em = factory.createEntityManager();
        em.getTransaction().begin();
        em.persist(reward);
        em.getTransaction().commit();
        em.close();
        return reward;
    }
    
    public static Reward updateReward(Reward reward) {
    	em = factory.createEntityManager();
        em.getTransaction().begin();
        Reward reward2 = em.find(Reward.class, reward.getId());
        reward2.setEditedOn(new Timestamp(System.currentTimeMillis()));
        reward2.setEditedBy(reward.getEditedBy());
        reward2.setRewardName(reward.getRewardName());
        reward2.setActive(reward.getActive());
        em.persist(reward2);
        em.getTransaction().commit();
        em.close();
        return reward2;
    }
    
    public static Reward find(int id) {
     	em = factory.createEntityManager();
     	Reward reward = em.find(Reward.class, id);
     	em.close();
     	return reward;
    }
    
    public static Reward findByRewardName(String rewardName) {
    	em = factory.createEntityManager();
    	Query q = em.createQuery("select u from Reward u WHERE u.rewardName = :rewardName");
        q.setParameter("rewardName", rewardName);
     	Reward reward = (Reward) q.getSingleResult();
     	em.close();
     	return reward;
    }
    
    @SuppressWarnings("unchecked")
	public static List<Reward> getRewardList() {
    	em = factory.createEntityManager();
    	Query q = em.createQuery("SELECT u FROM Reward u WHERE u.active=true");
    	List<Reward> rewardList2 = new ArrayList<Reward>();
    	try {
    	    List<Reward> rewardList = q.getResultList();
        	for(Reward reward : rewardList) {
        		rewardList2.add(reward);
        	}
    	} catch(NoResultException e) {
    		System.out.println("No Results Exception");
    	}
    	return rewardList2;
    }
}