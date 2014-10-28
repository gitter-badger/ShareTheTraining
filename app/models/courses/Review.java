package models.courses;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;

import models.users.Customer;
import common.BaseModelObject;

@Entity
public class Review extends BaseModelObject {

	public static Review create(Customer author, Course course,
			int courseRating, int trainerRating, EntityManager em) {
		Review review = new Review(author, course, courseRating, trainerRating);
		em.persist(review);
		author.addReview(review);
		course.addReview(review);
		review.putSolrDoc();
		return null;
	}
	
	protected Review(Customer author, Course course,
			int courseRating, int trainerRating) {
		this.author = author;
		this.course = course;
		this.courseRating = courseRating;
		this.trainerRating = trainerRating;
		this.date = new Date();
		
	}

	@ManyToOne
	private Customer author;

	@ManyToOne
	private Course course;

	private int courseRating;

	private int trainerRating;
	
	private Date date;

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public int getCourseRating() {
		return courseRating;
	}

	public void setCourseRating(int courseRating) {
		this.courseRating = courseRating;
	}

	public Customer getAuthor() {
		return author;
	}

	public void setAuthor(Customer author) {
		this.author = author;
	}

	public int getTrainerRating() {
		return trainerRating;
	}

	public void setTrainerRating(int trainerRating) {
		this.trainerRating = trainerRating;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}


}
