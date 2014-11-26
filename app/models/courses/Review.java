package models.courses;

import java.util.Date;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;

import models.users.Customer;
import common.BaseModelObject;

@Entity
public class Review extends BaseModelObject {

	public static Review create(Customer author, ConcreteCourse concreteCourse,
			EntityManager em) {
		Review review = new Review(author, concreteCourse);
		em.persist(review);
		author.addReview(review);
		concreteCourse.getCourseInfo().addReview(review);
		review.putSolrDoc();
		return null;
	}

	protected Review(Customer author, ConcreteCourse concreteCourse) {
		this.author = author;
		this.concreteCourse = concreteCourse;
		this.createDate = new Date();

	}

	public boolean updateTrainerRatings(List<Integer> trainerRatings) {
		if (trainerRatings == null)
			return false;
		this.setTrainerRatings(trainerRatings);
		double trainerSum = 0.0;
		for (Integer trainerRate : trainerRatings)
			trainerSum += trainerRate;
		this.trainerRating = trainerSum / ((double) trainerRatings.size());
		return false;

	}

	public boolean updateCourseRatings(List<Integer> courseRatings) {
		if (courseRatings == null)
			return false;
		this.setCourseRatings(courseRatings);
		double courseSum = 0.0;
		for (Integer courseRate : courseRatings)
			courseSum += courseRate;
		this.courseRating = courseSum / ((double) courseRatings.size());
		return false;
	}

	@ManyToOne
	private Customer author;

	@ManyToOne
	private ConcreteCourse concreteCourse;

	private double courseRating;

	private double trainerRating;

	@ElementCollection
	private List<Integer> trainerRatings;

	@ElementCollection
	private List<Integer> courseRatings;

	@ElementCollection
	private List<String> trainerQuestions;

	@ElementCollection
	private List<String> courseQuestions;

	private Date createDate;
	
	private String comment;
	

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public ConcreteCourse getConcreteCourse() {
		return concreteCourse;
	}

	public void setConcreteCourse(ConcreteCourse concreteCourse) {
		this.concreteCourse = concreteCourse;
	}

	public Customer getAuthor() {
		return author;
	}

	public void setAuthor(Customer author) {
		this.author = author;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public List<Integer> getTrainerRatings() {
		return trainerRatings;
	}

	public void setTrainerRatings(List<Integer> trainerRatings) {
		this.trainerRatings = trainerRatings;
	}

	public List<Integer> getCourseRatings() {
		return courseRatings;
	}

	public void setCourseRatings(List<Integer> courseRatings) {
		this.courseRatings = courseRatings;
	}

	public List<String> getTrainerQuestions() {
		return trainerQuestions;
	}

	public void setTrainerQuestions(List<String> trainerQuestions) {
		this.trainerQuestions = trainerQuestions;
	}

	public List<String> getCourseQuestions() {
		return courseQuestions;
	}

	public void setCourseQuestions(List<String> courseQuestions) {
		this.courseQuestions = courseQuestions;
	}

	public double getCourseRating() {
		return courseRating;
	}

	public void setCourseRating(double courseRating) {
		this.courseRating = courseRating;
	}

	public double getTrainerRating() {
		return trainerRating;
	}

	public void setTrainerRating(double trainerRating) {
		this.trainerRating = trainerRating;
	}

	public Review() {
		
	}
	
	

}
