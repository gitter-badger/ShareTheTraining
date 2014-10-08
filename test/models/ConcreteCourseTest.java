package models;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import play.db.jpa.Transactional;

public class ConcreteCourseTest extends ModelBaseTest{
	@Test
	@Transactional
	public void testConcreteCourse() {
		Course course=Course.create("123", "234", 1, this.getmEm());
		ConcreteCourse concreteCourse = ConcreteCourse.create(course, this.getmEm());
		assertNotNull(concreteCourse.getId());
		
	}
}
