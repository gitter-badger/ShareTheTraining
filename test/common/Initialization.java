package common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.EntityManager;

import models.courses.ConcreteCourse;
import models.courses.Course;
import models.locations.Location;
import models.users.Trainer;

public class Initialization {
	public Initialization(EntityManager entityManager){
		this.em = entityManager;
		course1 = Course.create("shishikan", "xingbuxing", 1,"xixihaha", em);
		course2 = Course.create("fengjian", "fubai", 2, "xixilala", em);
		concreteCourse1 = ConcreteCourse.create(course1, em);
		concreteCourse1.setCourseDate(new Date());
		concreteCourse1.setLocation(new Location(1,1,""));
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
		String dateInString = "06-04-1989";
		Date date = new Date();
		try {
			date = sdf.parse(dateInString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		concreteCourse2 = ConcreteCourse.create(course1, em);
		concreteCourse2.setCourseDate(date);
		concreteCourse1.setLocation(new Location(1,2,""));
		concreteCourse3 = ConcreteCourse.create(course2, em);
		concreteCourse3.setCourseDate(new Date());
		concreteCourse3.setLocation(new Location(2,1,""));
		em.getTransaction().begin();
		em.persist(course1);
		em.persist(course2);
		em.persist(concreteCourse1);
		em.getTransaction().commit();
	}
	
	private EntityManager em;
	
	public Trainer trainer1;
	
	public Course course1;
	
	public Course course2;
	
	public ConcreteCourse concreteCourse1;
	
	public ConcreteCourse concreteCourse2;
	
	public ConcreteCourse concreteCourse3;
}
