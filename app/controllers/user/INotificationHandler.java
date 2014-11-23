package controllers.user;

import java.util.Collection;
import java.util.Date;

import models.courses.ConcreteCourse;
import models.courses.Course;
import models.courses.CourseOrder;
import models.users.Customer;
import models.users.Trainer;

public interface INotificationHandler {

	Collection<Trainer> getNewTrainers(Date currentTime);

	Collection<Customer> getNewCustomers(Date currentTime);

	Collection<Course> getNewCourses(Date currentTime);

	Collection<ConcreteCourse> getNewConcreteCourse(Date currentTime);

	Collection<CourseOrder> getNewCourseOrder(Date currentTime);
	
}
