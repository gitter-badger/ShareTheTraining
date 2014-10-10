package models;

import javax.persistence.Entity;
import javax.persistence.EntityManager;

@Entity
public class Admin extends User {

	protected Admin(String email, String username, String password) {
		super(email, username, password);
	}

	public static Admin create(String email, String userName, String password,
			EntityManager em) {
		Admin admin = new Admin(email, userName, password);
		em.persist(admin);
		return admin;
	}
}
