package models.courses;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import models.users.Customer;
import common.BaseModelObject;

@Entity
public class CourseOrder extends BaseModelObject {
	
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
