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

import com.nextramile.models.Contact;
import com.nextramile.models.Customer;

/**
 * @author Samuel
 *
 */
public class ContactDAO {
	private static final String PERSISTENCE_UNIT_NAME = "nextramile";
    private static EntityManager em;
    private static EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

    public static Contact addContact(Contact contact) {
    	if(checkExisting(contact) == false) {
	    	contact.setCreatedOn(new Timestamp(System.currentTimeMillis()));
	    	em = factory.createEntityManager();
	        em.getTransaction().begin();
	        em.persist(contact);
	        em.getTransaction().commit();
	        em.close();
    	}
        return contact;
    }
    
    public static void updateContact(Contact contact) {
    	em = factory.createEntityManager();
        em.getTransaction().begin();
        Contact contact2 = em.find(Contact.class, contact.getId());
        contact.setEditedBy(contact.getEditedBy());
        contact2.setEditedOn(new Timestamp(System.currentTimeMillis()));
        contact2.setEmail(contact.getEmail());
        contact2.setActive(contact.getActive());
        em.persist(contact2);
        em.getTransaction().commit();
        em.close();
    }
    
    public static Contact find(int id) {
     	em = factory.createEntityManager();
     	Contact contact = em.find(Contact.class, id);
     	em.close();
     	return contact;
     }
    
    public static boolean checkExisting(Contact contact) {
    	em = factory.createEntityManager();
    	Query q = em.createQuery("select u from Contact u WHERE u.email = :email");
    	q.setParameter("email", contact.getEmail());
    	System.out.println(q.getResultList() + ": " + contact);
        boolean findContact = (q.getResultList().size() > 0);
        em.close();
        return findContact;
    }
  
    @SuppressWarnings("unchecked")
	public static List<Contact> getContactList() {
    	em = factory.createEntityManager();
    	Query q = em.createQuery("SELECT r FROM Contact r WHERE r.active=true");
    	List<Contact> contactList = q.getResultList();
    	return contactList;
    }
}
