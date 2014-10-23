package models.users;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import models.courses.ConcreteCourse;
import models.courses.Review;
import play.Logger;
import play.db.jpa.JPA;

@Entity
public class Customer extends User {

	public static Customer create(String email, String username, String password, EntityManager em) {
		Customer customer = new Customer(email, username, password);
		em.persist(customer);
		return customer;

	}

	protected Customer(String email, String username, String password) {
		super(email, username, password);
	}

	@ManyToMany(cascade = { CascadeType.ALL })
	private Collection<ConcreteCourse> selectedCourses = new ArrayList<ConcreteCourse>();

	@OneToMany(mappedBy = "author", cascade = { CascadeType.ALL })
	private Collection<Review> reviews = new ArrayList<Review>();


}