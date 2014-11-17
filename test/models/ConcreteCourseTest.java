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
		Course course = Course.create("hehehe", null, this.getmEm());
		List<Date> dates = new ArrayList<Date>();
		ConcreteCourse concreteCourse = ConcreteCourse.create(course,this.getmEm());
		assertNotNull(concreteCourse.getId());
		this.getmEm().remove(concreteCourse);
		this.getmEm().remove(course);
	}
}
