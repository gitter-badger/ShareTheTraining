package common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import play.Logger;
import models.courses.ConcreteCourse;
import models.courses.ConcreteCourseStatus;
import models.courses.Course;
import models.courses.CourseStatus;
import models.locations.Location;
import models.users.Customer;
import models.users.Trainer;

public class Initialization {
	public Initialization(EntityManager entityManager) {
		this.em = entityManager;
		trainer1 = Trainer.create("sda", "dasda", "dasd", em);
		trainer1.setRating(3);
		course1 = Course.create("xingbuxing", trainer1, em);
		course1.setCourseDesc("xixihaha");
		course1.setCourseCategory(1);
		course1.setStatus(CourseStatus.OPEN);
		course2 = Course.create("fubai", trainer1, em);
		course2.setCourseDesc("xixilala");
		course2.setCourseCategory(2);
		course2.setStatus(CourseStatus.OPEN);
		course2.setRating(5);
		List<Date> dates = new ArrayList<Date>();
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
		String dateInString = "06-04-1989";
		Date date = new Date();
		try {
			date = sdf.parse(dateInString);
		} catch (ParseException e) {
			Logger.error(e.toString());
		}
		dates.add(new Date());
		dates.add(date);
		concreteCourse1 = ConcreteCourse.create(course1, em);
		concreteCourse1.setEventbriteId("123");
		concreteCourse1.setLocation(new Location("qunimaba",
				"gun", "", -118.14, 34.03));
		concreteCourse1.setCourseDates(dates);
		concreteCourse1.setMinimum(1);
		concreteCourse1.setMaximum(2);
		concreteCourse1.setStatus(ConcreteCourseStatus.APPROVED);
		dates = new ArrayList<Date>();
		dates.add(new Date());
		concreteCourse2 = ConcreteCourse.create(course1, em);
		concreteCourse2.setEventbriteId("234");
		concreteCourse2.setLocation(new Location("qunimaba",
				"gun","", -118.14, 34.03));
		concreteCourse2.setCourseDates(dates);
		concreteCourse2.setMinimum(1);
		concreteCourse2.setMaximum(2);
		dates = new ArrayList<Date>();
		dates.add(new Date());
		concreteCourse3 = ConcreteCourse.create(course2, em);
		concreteCourse3.setEventbriteId("234");
		concreteCourse3.setLocation(new Location("nimabi",
				"gun", "", 121.28, 31.10));
		concreteCourse3.setCourseDates(dates);
		concreteCourse3.setMinimum(1);
		concreteCourse3.setMaximum(2);
		customer1 = Customer.create("qnmb", "xixihaha", "lala", em);
		
	}

	private EntityManager em;

	public Trainer trainer1;

	public Customer customer1;
	
	public Course course1;

	public Course course2;

	public ConcreteCourse concreteCourse1;

	public ConcreteCourse concreteCourse2;

	public ConcreteCourse concreteCourse3;

	public void dispose() {
		
		em.remove(concreteCourse1);
		em.remove(concreteCourse2);
		em.remove(concreteCourse3);
		em.remove(course1);
		em.remove(course2);
		em.remove(trainer1);
		em.remove(customer1);
	}
}
