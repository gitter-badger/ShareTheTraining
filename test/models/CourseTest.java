package models;

import static org.fest.assertions.Assertions.assertThat;
import models.courses.Course;

import org.junit.Test;

import common.BaseTest;


public class CourseTest extends BaseTest{
	
	@Test
	public void testCourseCreation() {
		Course course = Course.create("234", 1, "lala", this.getmEm());
		this.getmEm().getTransaction().begin();
		this.getmEm().persist(course);
		this.getmEm().getTransaction().commit();
		assertThat(course.getId()).isNotNull();
		this.getmEm().getTransaction().begin();
		this.getmEm().remove(course);
		this.getmEm().getTransaction().commit();
	}
}