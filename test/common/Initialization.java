package common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.EntityManager;

import models.ConcreteCourse;
import models.Course;

public class Initialization {
	public Initialization(EntityManager entityManager){
		this.em = entityManager;
		course1 = Course.create("ziyou", "minzhu", 1, em);
		concreteCourse1 = ConcreteCourse.create(course1, em);
		concreteCourse1.setCourseDate(new Date());
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
		em.getTransaction().begin();
		em.persist(course1);
		em.persist(concreteCourse1);
		em.getTransaction().commit();
	}
	
	private EntityManager em;
	
	public Course course1;
	
	public ConcreteCourse concreteCourse1;
	
	public ConcreteCourse concreteCourse2;
}
