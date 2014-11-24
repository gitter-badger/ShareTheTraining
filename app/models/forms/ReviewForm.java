package models.forms;

import java.util.List;

import play.Logger;
import models.courses.Review;

public class ReviewForm {

	private String comment;

	private String email;

	private String concreteCourseId;

	private List<Integer> courseRatings;

	private List<Integer> trainerRatings;

	private List<String> courseQuestions;

	private List<String> trainerQuestions;

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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getConcreteCourseId() {
		return concreteCourseId;
	}

	public void setConcreteCourseId(String concreteCourseId) {
		this.concreteCourseId = concreteCourseId;
	}

	public boolean bindReview(Review review) {
		try {
			if (review.getAuthor().getEmail().equals(this.email)
					&& review.getConcreteCourse().getConcreteCourseId()
							.equals(this.concreteCourseId)) {
				review.setCourseQuestions(courseQuestions);
				review.setTrainerQuestions(trainerQuestions);
				return review.updateCourseRatings(courseRatings)
						&& review.updateTrainerRatings(trainerRatings);
			}
		} catch (Exception e) {
			Logger.error(e.toString());
		}
		return false;
	}
}
