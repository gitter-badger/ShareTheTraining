package controllers.user;

import java.util.Collection;
import java.util.Date;

import models.courses.ConcreteCourse;
import models.courses.Course;
import models.courses.CourseOrder;
import models.users.Customer;
import models.users.NotificationItem;
import models.users.Trainer;

public interface INotificationHandler {

	public Collection<Trainer> getNewTrainers(Date lastTime);

	public Collection<Customer> getNewCustomers(Date lastTime);

	public Collection<Course> getNewCourses(Date lastTime);

	public Collection<ConcreteCourse> getNewConcreteCourse(Date lastTime);

	public Collection<CourseOrder> getNewCourseOrder(Date lastTime);

	public void updateNotifiedDate(NotificationItem notificationItem, Date current);

	public Date getLastNotifiedDate(NotificationItem notificationItem);
	
}
