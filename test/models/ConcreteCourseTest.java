package models;

import static org.junit.Assert.assertNotNull;
import models.courses.ConcreteCourse;
import models.courses.Course;

import org.junit.Test;

import common.BaseTest;

public class ConcreteCourseTest extends BaseTest{
	@Test
	public void testConcreteCourseCreation() {
		Course course=Course.create("hehehe", 1, "lala", this.getmEm());
		ConcreteCourse concreteCourse = ConcreteCourse.create(course, this.getmEm());
		this.getmEm().getTransaction().begin();
		this.getmEm().persist(course);
		this.getmEm().persist(concreteCourse);
		this.getmEm().getTransaction().commit();
		assertNotNull(concreteCourse.getId());
		this.getmEm().getTransaction().begin();
		this.getmEm().remove(concreteCourse);
		this.getmEm().remove(course);
		this.getmEm().getTransaction().commit();
	}
}
