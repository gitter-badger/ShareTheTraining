package controllers.course;

import java.util.Collection;

import javax.persistence.EntityManager;

import models.courses.Course;
import models.filters.FilterBuilder;
import models.forms.CourseForm;

public interface ICourseHandler {
	public Course getCourseById(String courseId);

	public Collection<Course> getCourseByCategory(int category, int pageNumber,
			int pageSize);

	public Collection<Course> getCourseByTrainer(String trainerEmail,
			int pageNumber, int pageSize);

	public Collection<Course> getCourseByCustomRule(FilterBuilder cb,
			int pageNumber, int pageSize);

	public boolean modifyMaximum(String courseId, int maximum);

	public boolean modifyMinimum(String courseId, int minimum);

	public boolean addNewCourse(String trainerEmail, CourseForm courseForm);

}
