package models.courses;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;

import models.users.Customer;
import common.BaseModelObject;

@Entity
public class Review extends BaseModelObject {

	public static Review create(Customer author,ConcreteCourse concreteCourse,
			int courseRating, int trainerRating, EntityManager em) {
		Review review = new Review(author, concreteCourse, courseRating, trainerRating);
		em.persist(review);
		author.addReview(review);
		concreteCourse.getCourseInfo().addReview(review);
		review.putSolrDoc();
		return null;
	}

	protected Review(Customer author, ConcreteCourse concreteCourse, int courseRating,
			int trainerRating) {
		this.author = author;
		this.concreteCourse = concreteCourse;
		this.courseRating = courseRating;
		this.trainerRating = trainerRating;
		this.date = new Date();

	}

	@ManyToOne
	private Customer author;

	@ManyToOne
	private ConcreteCourse concreteCourse;

	private int courseRating;

	private int trainerRating;

	private Date date;

	public ConcreteCourse getConcreteCourse() {
		return concreteCourse;
	}

	public void setConcreteCourse(ConcreteCourse concreteCourse) {
		this.concreteCourse = concreteCourse;
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
