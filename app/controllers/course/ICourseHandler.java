package controllers.course;

import java.util.Collection;

import models.courses.ConcreteCourse;
import models.courses.Course;
import models.courses.CourseOrder;
import models.filters.FilterBuilder;
import models.forms.ConcreteCourseForm;
import models.forms.CourseForm;
import models.users.Customer;

public interface ICourseHandler {
	public Course getCourseById(Integer courseId);

	public Collection<Course> getCourseByCategory(int category, int pageNumber,
			int pageSize, String orderByColumn);

	public Collection<Course> getCourseByTrainer(String trainerEmail,
			int pageNumber, int pageSize, String orderByColumn);

	public Collection<Course> getCourseByCustomRule(FilterBuilder cb,
			String orderByColumn, int pageNumber, int pageSize);

	public boolean modifyMaximum(String courseId, int maximum);

	public boolean modifyMinimum(String courseId, int minimum);

	public boolean addNewCourse(String trainerEmail, CourseForm courseForm);

	public ConcreteCourse getCourseByEventbriteId(String eventbriteId);

	public ConcreteCourse getCourseByConcreteCourseId(String concreteCourseId);

	public boolean dropCourse(Customer customer, ConcreteCourse concreteCourse);

	public CourseOrder registerCourse(Customer customer,
			ConcreteCourse concreteCourse, String orderId);

	public boolean updateCourseInfo(String trainerEmail, CourseForm courseForm);

	public boolean deleteConcreteCourse(String concreteCourseId);

	public boolean deleteCourse(int courseId);

	public boolean addNewConcreteCourse(String trainerEmail,
			ConcreteCourseForm courseForm);

	public boolean updateConcreteCourse(String trainerEmail,
			ConcreteCourseForm courseForm);
}
