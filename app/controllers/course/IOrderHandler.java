package controllers.course;

import java.util.Collection;
import java.util.Date;

import models.courses.ConcreteCourse;
import models.courses.CourseOrder;
import models.courses.OrderStatus;
import models.filters.FilterBuilder;
import models.users.Customer;

public interface IOrderHandler {
	public Collection<CourseOrder> getCourseOrderByCustomer(String userEmail);

	public Collection<CourseOrder> getCourseOrderByCourse(Integer id);

	public Collection<CourseOrder> getCourseOrderByConcreteCourse(String concreteCourseId);

	public Collection<CourseOrder> getCourseOrderByCustomRule(FilterBuilder cb,
			String orderByColumn, int pageNumber, int pageSize);

	public CourseOrder getCourseOrderByOrderId(String orderId);
	
	public CourseOrder newCourseOrder(String orderId,
			ConcreteCourse concreteCourse, Customer customer);
}
