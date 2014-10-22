package models.users;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import models.courses.ConcreteCourse;
import models.courses.Review;
import play.Logger;
import play.db.jpa.JPA;

@Entity
public class Customer extends User {

	public static Logger LOG = new Logger();

	protected Customer(String email, String username, String password) {
		super(email, username, password);
	}

	public Customer() {

	}

	public static Customer create(String email, String username, String password) {
		LOG.info("user:" + email);
		Customer cus = new Customer(email, username, password);
		JPA.em().persist(cus);
		return cus;

	}

	public static Customer authenticate(String email, String password) {
		Customer cus = new Customer();
		cus.setEmail("dd@gmail.com");
		cus.setPassword("99");
		if (email.equals("dd@gmail.com") && password.equals("99")) {
			return cus;
		}
		return null;
	}

	@ManyToMany(cascade = { CascadeType.ALL })
	private Collection<ConcreteCourse> selectedCourses = new ArrayList<ConcreteCourse>();

	@OneToMany(mappedBy = "author", cascade = { CascadeType.ALL })
	private Collection<Review> reviews = new ArrayList<Review>();

}
