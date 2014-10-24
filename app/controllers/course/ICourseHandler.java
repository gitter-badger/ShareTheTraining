package controllers.course;

import java.util.Collection;

import javax.persistence.EntityManager;

import models.courses.Course;
import models.filters.FilterBuilder;

public interface ICourseHandler {
	public Course getCourseById(String courseId, EntityManager em);

	public Collection<Course> getCourseByCategory(int category, int pageNumber,
			int pageSize, EntityManager em);

	public Collection<Course> getCourseByTrainer(String trainerEmail,
			int pageNumber, int pageSize, EntityManager em);

	public Collection<Course> getCourseByCustomRule(FilterBuilder cb,
			int pageNumber, int pageSize, EntityManager em);
}
