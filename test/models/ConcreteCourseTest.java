package models;

import static org.junit.Assert.assertNotNull;
import models.courses.ConcreteCourse;
import models.courses.Course;

import org.junit.Test;

import common.BaseTest;

public class ConcreteCourseTest extends BaseTest{
	@Test
	public void testConcreteCourseCreation() {
		Course course=Course.create("hehehe", "hehehe", 1, "lala", this.getmEm());
		ConcreteCourse concreteCourse = ConcreteCourse.create(course, this.getmEm());
		assertNotNull(concreteCourse.getId());
		this.getmEm().remove(concreteCourse);
		this.getmEm().remove(course);
	}
}
