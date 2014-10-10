package models;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.OneToMany;


@Entity
public class Trainer extends User{

	protected Trainer(String email, String username, String password) {
		super(email, username, password);
	}
	
	public static Trainer create(String email, String userName,
			String password, EntityManager em) {
		Trainer trainer = new Trainer(email, userName, password);
		em.persist(trainer);
		return trainer;
	}
	
	@OneToMany(mappedBy = "trainer", cascade = { CascadeType.ALL })
	private Collection<ConcreteCourse> courses = new ArrayList<ConcreteCourse>();

}
