package controllers.course;

import models.courses.Review;
import models.forms.ReviewForm;
import models.users.Customer;

public interface IReviewHandler {
	public Review getReviewByCustomerAndCourse(String email,
			String concreteCourseId);

	public Review writeReview(ReviewForm reviewForm, Customer author,
			IOrderHandler orderHandler);
}
