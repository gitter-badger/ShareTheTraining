package models.courses;

import javax.persistence.ManyToOne;

import common.BaseModelObject;
import models.users.Customer;

public class RegistrationOrder extends BaseModelObject {
	
	private String orderId;
	
	@ManyToOne
	private ConcreteCourse concreteCourse;
	
	@ManyToOne
	private Customer customer;
}
