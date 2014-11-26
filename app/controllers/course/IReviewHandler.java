package controllers.course;

import java.util.Collection;

import models.courses.Review;
import models.filters.FilterBuilder;
import models.forms.ReviewForm;
import models.users.Customer;

public interface IReviewHandler {
	public Review getReviewByCustomerAndCourse(String email,
			String concreteCourseId);

	public Review writeReview(ReviewForm reviewForm, Customer author,
			IOrderHandler orderHandler);

	public Collection getReviewByCustomerRule(FilterBuilder fb, String orderByColumn,
			int pageNumber, int pageSize);
}
