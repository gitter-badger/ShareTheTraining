package models;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;


@Entity
public class Customer extends User{

	protected Customer(String email, String username, String password) {
		super(email, username, password);
	}
	
	public static Customer create(String email, String userName,
			String password, EntityManager em) {
		Customer customer = new Customer(email, userName, password);
		em.persist(customer);
		return customer;
	}
	
	@ManyToMany(cascade = { CascadeType.ALL })
	private Collection<ConcreteCourse> selectedCourses = new ArrayList<ConcreteCourse>();
	
	@OneToMany(mappedBy = "author", cascade = { CascadeType.ALL })
	private Collection<Review> reviews = new ArrayList<Review>();

}
