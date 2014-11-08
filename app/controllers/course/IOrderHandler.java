package controllers.course;

import models.courses.CourseOrder;

public interface IOrderHandler {
	public CourseOrder getCourseOrderByCustomer(String userEmail);

	public CourseOrder getCourseOrderByCourse(Integer id);

	public CourseOrder getCourseOrderByConcreteCourse(Integer id);
}
