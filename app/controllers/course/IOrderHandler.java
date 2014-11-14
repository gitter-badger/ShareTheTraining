package controllers.course;

import java.util.Collection;

import models.courses.CourseOrder;
import models.filters.FilterBuilder;

public interface IOrderHandler {
	public Collection<CourseOrder> getCourseOrderByCustomer(String userEmail);

	public Collection<CourseOrder> getCourseOrderByCourse(Integer id);

	public Collection<CourseOrder> getCourseOrderByConcreteCourse(String concreteCourseId);

	public Collection<CourseOrder> getCourseOrderByCustomRule(FilterBuilder cb,
			String orderByColumn, int pageNumber, int pageSize);

	public CourseOrder getCourseOrderByOrderId(String orderId);
}
