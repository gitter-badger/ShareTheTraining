package controllers.course;

import java.util.Collection;

import models.courses.CourseOrder;

public interface IOrderHandler {
	public Collection<CourseOrder> getCourseOrderByCustomer(String userEmail);

	public Collection<CourseOrder> getCourseOrderByCourse(Integer id);

	public Collection<CourseOrder> getCourseOrderByConcreteCourse(String concreteCourseId);
}
