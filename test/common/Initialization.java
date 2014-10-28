package common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.EntityManager;

import models.courses.ConcreteCourse;
import models.courses.Course;
import models.courses.CourseStatus;
import models.locations.Location;
import models.users.Trainer;

public class Initialization {
	public Initialization(EntityManager entityManager){
		this.em = entityManager;
		trainer1 = Trainer.create("sda", "dasda", "dasd", em);
		course1 = Course.create("xingbuxing", 1,"xixihaha", em);
		course1.setStatus(CourseStatus.approved);
		course1.setTrainer(trainer1);
		course2 = Course.create("fubai", 2, "xixilala", em);
		course2.setStatus(CourseStatus.approved);
		course2.setTrainer(trainer1);
		concreteCourse1 = ConcreteCourse.create(course1, em);
		concreteCourse1.setCourseDate(new Date());
		concreteCourse1.setLocation(new Location(1,1,"",-118.14 , 34.03));
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
		concreteCourse1.setLocation(new Location(1,2,"",-118.14 , 34.03));
		concreteCourse3 = ConcreteCourse.create(course2, em);
		concreteCourse3.setCourseDate(new Date());
		concreteCourse3.setLocation(new Location(2,1,"",121.28,31.10));
		em.getTransaction().begin();
		em.persist(trainer1);
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
	
	public void dispose(){
		em.getTransaction().begin();
		em.remove(concreteCourse1);
		em.remove(concreteCourse2);
		em.remove(concreteCourse3);
		em.remove(course1);
		em.remove(course2);
		em.remove(trainer1);
		em.getTransaction().commit();
	}
}
