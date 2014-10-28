package models;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import models.courses.ConcreteCourse;
import models.courses.Course;
import models.locations.Location;

import org.junit.Test;

import common.BaseTest;

public class ConcreteCourseTest extends BaseTest {
	@Test
	public void testConcreteCourseCreation() {
		Course course = Course.create("hehehe", 1, "lala", this.getmEm());
		List<Date> dates = new ArrayList<Date>();
		ConcreteCourse concreteCourse = ConcreteCourse.create(course, "123",
				new Location(1, 1, "", -118.14, 34.03), dates, 2, 1,
				this.getmEm());
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
