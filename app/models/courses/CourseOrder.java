package models.courses;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;

import models.users.Customer;
import common.BaseModelObject;

@Entity
public class CourseOrder extends BaseModelObject {

	public static CourseOrder create(String orderId,
			ConcreteCourse concreteCourse, Customer customer, EntityManager em) {
		CourseOrder order = new CourseOrder(orderId, concreteCourse, customer);
		em.persist(order);
		order.putSolrDoc();
		return order;
	}
	
	

	protected CourseOrder(String orderId, ConcreteCourse concreteCourse,
			Customer customer) {
		this.orderId = orderId;
		this.concreteCourse = concreteCourse;
		this.customer = customer;
	}

	private String orderId;

	@ManyToOne
	private ConcreteCourse concreteCourse;

	@ManyToOne
	private Customer customer;

	public String getOrderId() {
		return orderId;
	}

	public ConcreteCourse getConcreteCourse() {
		return concreteCourse;
	}

	public void setConcreteCourse(ConcreteCourse concreteCourse) {
		this.concreteCourse = concreteCourse;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

}
