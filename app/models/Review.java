package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import common.BaseModelObject;

@Entity
public class Review extends BaseModelObject {

	@ManyToOne
	private Customer author;

	@ManyToOne
	private ConcreteCourse course;

	private int courseRating;

	private int tainingRating;

	public Customer getAuhtor() {
		return author;
	}

	public void setAuhtor(Customer author) {
		this.author = author;
	}

	public ConcreteCourse getCourse() {
		return course;
	}

	public void setCourse(ConcreteCourse course) {
		this.course = course;
	}

	public int getCourseRating() {
		return courseRating;
	}

	public void setCourseRating(int courseRating) {
		this.courseRating = courseRating;
	}

	public int getTainingRating() {
		return tainingRating;
	}

	public void setTainingRating(int tainingRating) {
		this.tainingRating = tainingRating;
	}

}
