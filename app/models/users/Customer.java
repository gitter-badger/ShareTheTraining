package models.users;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import be.objectify.deadbolt.core.models.Role;
import models.courses.ConcreteCourse;
import models.courses.Review;
import models.courses.WaitListRecord;

@Entity
public class Customer extends User {

	public static Customer create(String email, String username,
			String password, EntityManager em) {
		Customer customer = new Customer(email, username, password);
		em.persist(customer);
		return customer;

	}

	protected Customer(String email, String username, String password) {
		super(email, username, password);
		this.setUserRole(UserRole.CUSTOMER);
	}

	public Customer() {
	}

	private String cellPhone;

	private String phone;

	@Override
	public List<? extends Role> getRoles() {
		List<UserRole> list = new ArrayList<UserRole>();
		list.add(this.getUserRole());
		return list;
	}

	@ManyToMany(mappedBy = "selectedCustomers", cascade = { CascadeType.ALL })
	private Collection<ConcreteCourse> selectedCourses = new ArrayList<ConcreteCourse>();

	@OneToMany(mappedBy = "customer", cascade = { CascadeType.ALL })
	private Collection<WaitListRecord> waitListRecords = new ArrayList<WaitListRecord>();

	@OneToMany(mappedBy = "author", cascade = { CascadeType.ALL })
	private Collection<Review> reviews = new ArrayList<Review>();

	public Collection<ConcreteCourse> getSelectedCourses() {
		return selectedCourses;
	}

	public void setSelectedCourses(Collection<ConcreteCourse> selectedCourses) {
		this.selectedCourses = selectedCourses;
	}

	public Collection<WaitListRecord> getWaitListRecords() {
		return waitListRecords;
	}

	public void setWaitListRecords(Collection<WaitListRecord> waitListRecords) {
		this.waitListRecords = waitListRecords;
	}

	public Collection<Review> getReviews() {
		return reviews;
	}

	public void setReviews(Collection<Review> reviews) {
		this.reviews = reviews;
	}

}
