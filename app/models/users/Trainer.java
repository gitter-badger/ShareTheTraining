package models.users;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.OneToMany;

import models.courses.Course;


@Entity
public class Trainer extends User{

	
	protected Trainer(String email, String username, String password) {
		super(email, username, password);
		// TODO Auto-generated constructor stub
	}

	@OneToMany(mappedBy = "trainer", cascade = { CascadeType.ALL })
	private Collection<Course> courses = new ArrayList<Course>();

}
