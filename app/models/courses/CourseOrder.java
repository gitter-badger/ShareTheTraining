package models.courses;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;

import models.users.Customer;
import common.BaseModelObject;

@Entity
public class CourseOrder extends BaseModelObject {

	public static CourseOrder create(String orderId,
			ConcreteCourse concreteCourse, Customer customer, Date orderDate,
			OrderStatus orderStatus, EntityManager em) {
		CourseOrder order = new CourseOrder(orderId, concreteCourse, customer,
				orderDate, orderStatus);
		em.persist(order);
		order.putSolrDoc();
		return order;
	}

	protected CourseOrder(String orderId, ConcreteCourse concreteCourse,
			Customer customer, Date orderDate, OrderStatus orderStatus) {
		super();
		this.orderId = orderId;
		this.concreteCourse = concreteCourse;
		this.customer = customer;
		this.orderDate = orderDate;
		this.orderStatus = orderStatus;
	}

	private String orderId;

	@ManyToOne
	private ConcreteCourse concreteCourse;

	@ManyToOne
	private Customer customer;

	private Date orderDate;

	private OrderStatus orderStatus = OrderStatus.PENDING;

	private double gross = -1;
	
	protected CourseOrder() {
		super();
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

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

	public double getGross() {
		return gross;
	}

	public void setGross(double gross) {
		this.gross = gross;
	}

}
