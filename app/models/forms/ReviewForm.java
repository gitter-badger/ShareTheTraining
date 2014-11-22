package models.forms;

import java.util.List;

public class ReviewForm {

	private String comment;

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

}
