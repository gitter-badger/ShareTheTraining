package models;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.OneToMany;

import play.db.jpa.Transactional;
import common.BaseModelObject;

@Entity
public class Course extends BaseModelObject{
	@Transactional
	public static Course create(String courseID,String courseName, EntityManager em) {
		Course course = new Course();
		course.setCourseID(courseID);
		course.setCourseName(courseName);
		em.persist(course);
		return course;
	}
	
	private String courseID;
	
	private String courseName;

	@OneToMany(mappedBy = "courseInfo", cascade = { CascadeType.ALL })
	private Collection<ConcreteCourse> courses = new ArrayList<ConcreteCourse>();
	
	public String getCourseID() {
		return courseID;
	}

	public void setCourseID(String courseID) {
		this.courseID = courseID;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
}
