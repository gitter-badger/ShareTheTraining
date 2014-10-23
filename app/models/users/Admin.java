package models.users;

import javax.persistence.Entity;
import javax.persistence.EntityManager;

import models.spellchecker.SolrDao;

import org.apache.solr.common.SolrInputDocument;

@Entity
public class Admin extends User {
	
	public static Admin create(String email, String username, String password, EntityManager em) {
		Admin admin = new Admin(email, username, password);
		em.persist(admin);
		return admin;

	}

	protected Admin(String email, String username, String password) {
		super(email, username, password);
	}

}
