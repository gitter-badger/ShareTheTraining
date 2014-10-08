package models;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import play.db.jpa.Transactional;

public class CourseTest extends ModelBaseTest{
	
	@Test
	@Transactional
	public void testCourse() {
		Course course = Course.create("123", "234", 1, this.getmEm());
		assertNotNull(course.getId());
		
	}
}