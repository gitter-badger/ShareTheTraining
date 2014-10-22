package models;

import static org.fest.assertions.Assertions.assertThat;
import models.courses.Course;

import org.junit.Test;

import common.BaseTest;


public class CourseTest extends BaseTest{
	
	@Test
	public void testCourseCreation() {
		Course course = Course.create("123", "234", 1, "lala", this.getmEm());
		assertThat(course.getId()).isNotNull();
	}
}