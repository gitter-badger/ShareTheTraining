package models.forms;

import java.util.List;

import play.Logger;
import models.courses.Course;
import models.courses.CourseStatus;
import models.courses.Review;

public class ReviewForm {

	private String comment;

	private String orderId;

	private List<Integer> courseRatings;

	private List<Integer> trainerRatings;

	private List<String> courseQuestions;

	private List<String> trainerQuestions;
	
	private int reviewId;
	
	private int courseId;
	
	private String courseName;
	
	private String trainerName;
	
	private String userName;
	
	private double courseRating;
	
	private double trainerRating;
	
	

	public int getReviewId() {
		return reviewId;
	}

	public void setReviewId(int reviewId) {
		this.reviewId = reviewId;
	}

	public int getCourseId() {
		return courseId;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getTrainerName() {
		return trainerName;
	}

	public void setTrainerName(String trainerName) {
		this.trainerName = trainerName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public List<Integer> getCourseRatings() {
		return courseRatings;
	}

	public void setCourseRatings(List<Integer> courseRatings) {
		this.courseRatings = courseRatings;
	}

	public List<Integer> getTrainerRatings() {
		return trainerRatings;
	}

	public void setTrainerRatings(List<Integer> trainerRatings) {
		this.trainerRatings = trainerRatings;
	}

	public List<String> getCourseQuestions() {
		return courseQuestions;
	}

	public void setCourseQuestions(List<String> courseQuestions) {
		this.courseQuestions = courseQuestions;
	}

	public List<String> getTrainerQuestions() {
		return trainerQuestions;
	}

	public void setTrainerQuestions(List<String> trainerQuestions) {
		this.trainerQuestions = trainerQuestions;
	}

	

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
	public boolean bindReview(Review review) {
		try {
			review.setCourseQuestions(courseQuestions);
			review.setTrainerQuestions(trainerQuestions);
			return review.updateCourseRatings(courseRatings)
					&& review.updateTrainerRatings(trainerRatings);

		} catch (Exception e) {
			Logger.error(e.toString());
		}
		return false;
	}
	
	public static ReviewForm bindReviewForm(
			Review review) {
		if (review == null)
			return null;
		ReviewForm reviewForm = new ReviewForm();
		reviewForm.setComment(review.getComment());
		reviewForm.setCourseId(review.getConcreteCourse().getCourseInfo().getId());
		reviewForm.setCourseName(review.getConcreteCourse().getCourseInfo().getCourseName());
		reviewForm.setCourseQuestions(review.getCourseQuestions());
		reviewForm.setCourseRatings(review.getCourseRatings());
		reviewForm.setCourseRating(review.getCourseRating());
		reviewForm.setTrainerRatings(review.getTrainerRatings());
		reviewForm.setTrainerQuestions(review.getTrainerQuestions());
		reviewForm.setTrainerRating(review.getTrainerRating());
		reviewForm.setTrainerName(review.getConcreteCourse().getCourseInfo().getTrainer().getName());
		reviewForm.setReviewId(review.getId());
		reviewForm.setUserName(review.getAuthor().getUsername());
		
		
		return reviewForm;

	}
	
}
