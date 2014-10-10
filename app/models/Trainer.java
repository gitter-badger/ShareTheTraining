package models;

import javax.persistence.Entity;
import javax.persistence.EntityManager;

@Entity
public class Trainer extends User{

	protected Trainer(String email, String username, String password) {
		super(email, username, password);
	}
	
	public static Trainer create(String email, String userName, String password, EntityManager em) {
		Trainer trainer = new Trainer(email, userName, password);
		em.persist(trainer);
		return trainer;
	}

}
