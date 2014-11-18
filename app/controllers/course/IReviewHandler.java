package controllers.course;

import models.courses.Review;

public interface IReviewHandler {
	public Review getReviewByCustomerAndCourse(String email,
			String concreteCourseId);
}
