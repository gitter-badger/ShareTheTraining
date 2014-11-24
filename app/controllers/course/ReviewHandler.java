package controllers.course;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import controllers.user.UserHandler;
import play.db.jpa.JPA;
import models.courses.ConcreteCourse;
import models.courses.Course;
import models.courses.CourseOrder;
import models.courses.Review;
import models.forms.ReviewForm;
import models.users.Customer;
import models.users.User;

public class ReviewHandler implements IReviewHandler {
	private EntityManager em;

	public ReviewHandler() {
		this.em = JPA.em();
	}

	@Override
	public Review getReviewByCustomerAndCourse(String email,
			String concreteCourseId) {
		String hql = "from Review r where r.author.email= :email and r.concreteCourse.concreteCourseId= :concreteCourseId";
		Query query = em.createQuery(hql).setParameter("email", email)
				.setParameter("concreteCourseId", concreteCourseId);
		Collection result = query.getResultList();
		if (result.size() > 0)
			return (Review) result.iterator().next();
		return null;
	}

	// TODO WRITE REVIEW, REVIEW SEARCH(?)
	@Override
	public Review writeReview(ReviewForm reviewForm, Customer author, IOrderHandler orderHandler) {
		CourseOrder courseOrder =  orderHandler.getCourseOrderByOrderId(reviewForm.getOrderId());
		if(courseOrder != null &&  courseOrder.getCustomer().getEmail().equals(author.getEmail())){
			Review review = Review.create(author, courseOrder.getConcreteCourse(), em);
			reviewForm.bindReview(review);
			return review;
		}
		return null;
	}
}
