package models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Selection;

import common.BaseModelObject;

@Entity
public class Course extends BaseModelObject {

	public static Course create(String courseId, String courseName,
			int courseCategory, EntityManager em) {
		Course course = new Course();
		course.setCourseId(courseId);
		course.setCourseName(courseName);
		course.setCourseCategory(courseCategory);
		em.persist(course);
		return course;
	}

	private String courseId;

	private String courseName;
	
	@ManyToOne
	private Trainer trainer;

	private int courseCategory;

	//TODO should this be in ConcreteCourse?
	private double price;
	
	@OneToMany(mappedBy = "courseInfo", cascade = { CascadeType.ALL })
	private Collection<ConcreteCourse> courses = new ArrayList<ConcreteCourse>();
	
	
	public static List<Selection> getSelections(Path path){
		List<Selection> selections = new ArrayList<Selection>();
		selections.add(path.get("price"));
		selections.add(path.get("courseCategory"));
		return selections;
	}
	
	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	
	public Trainer getTrainer() {
		return trainer;
	}

	public void setTrainer(Trainer trainer) {
		this.trainer = trainer;
	}


	public int getCourseCategory() {
		return courseCategory;
	}

	public void setCourseCategory(int courseCategory) {
		this.courseCategory = courseCategory;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
}
