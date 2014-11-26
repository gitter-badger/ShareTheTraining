package models.forms;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import models.courses.Course;
import models.courses.CourseOrder;
import models.courses.CourseStatus;
import models.courses.OrderStatus;

public class CourseOrderForm {
	
	private String userEmail;

	private String userName;
	
	private String orderId;
	
	private String trainerName;
	
	private String trainerEmail;
	
	private String courseName;
	
	private Date  courseDate;
	
	private Date orderDate;
	
	private double price;
	
	private double gross;
	
	@JsonFormat(shape= JsonFormat.Shape.NUMBER_INT)
	private OrderStatus orderStatus = OrderStatus.CONFIRMED;

	
	
	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getTrainerName() {
		return trainerName;
	}

	public void setTrainerName(String trainerName) {
		this.trainerName = trainerName;
	}

	public String getTrainerEmail() {
		return trainerEmail;
	}

	public void setTrainerEmail(String trainerEmail) {
		this.trainerEmail = trainerEmail;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public Date getCourseDate() {
		return courseDate;
	}

	public void setCourseDate(Date courseDate) {
		this.courseDate = courseDate;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getGross() {
		return gross;
	}

	public void setGross(double gross) {
		this.gross = gross;
	}
	
	
	public boolean bindCourseOrder(CourseOrder courseOrder) {
		if (courseOrder == null
				|| courseOrder.getOrderId() != this.orderId)
			return false;

		courseOrder.setOrderStatus(orderStatus);
		
		return true;
	}
	
	public static CourseOrderForm bindCourseOrderForm(
			CourseOrder courseOrder) {
		if (courseOrder == null)
			return null;
		CourseOrderForm courseOrderForm = new CourseOrderForm();
		courseOrderForm.setCourseDate(courseOrder.getConcreteCourse().getCourseDate());
		courseOrderForm.setCourseName(courseOrder.getConcreteCourse().getCourseInfo().getCourseName());
		courseOrderForm.setGross(courseOrder.getGross());
		courseOrderForm.setOrderDate(courseOrder.getOrderDate());
		courseOrderForm.setOrderId(courseOrder.getOrderId());
		courseOrderForm.setPrice(courseOrder.getConcreteCourse().getCourseInfo().getPrice());
		courseOrderForm.setTrainerEmail(courseOrder.getConcreteCourse().getCourseInfo().getTrainer().getEmail());
		courseOrderForm.setUserName(courseOrder.getCustomer().getUsername());
		courseOrderForm.setTrainerName(courseOrder.getConcreteCourse().getCourseInfo().getTrainer().getName());
		courseOrderForm.setUserEmail(courseOrder.getCustomer().getEmail());
		
		
		return courseOrderForm;

	}
	
	
}
