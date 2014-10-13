package models;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

public class CourseLibraryTest extends ModelBaseTest {
	@Test
	public void testCourseLibrary() {
		Course course = Course.create("345", "678", 1, this.getmEm());
		this.getmEm().getTransaction().begin();
		this.getmEm().persist(course);
		this.getmEm().getTransaction().commit();
		CourseLibrary courseLibrary = new CourseLibrary(this.getmEm());
		assertThat(course).isEqualTo(courseLibrary.getCourseById(course.getCourseID()));
	}
}
