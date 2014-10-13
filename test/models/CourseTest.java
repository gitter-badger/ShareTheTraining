package models;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

import play.db.jpa.JPA;

public class CourseTest extends ModelBaseTest{
	
	@Test
	public void testCourseCreation() {
		Course course = Course.create("123", "234", 1, this.getmEm());
		assertThat(course.getId()).isNotNull();
	}
}