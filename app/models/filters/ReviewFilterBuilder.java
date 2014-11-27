package models.filters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import models.courses.ConcreteCourse;
import models.courses.Course;
import models.courses.CourseOrder;
import models.courses.OrderStatus;
import models.courses.Review;
import models.users.Customer;

public class ReviewFilterBuilder implements FilterBuilder {
	int reviewId = -1;
	int courseId = -1;
	String concreteCourseId;
	String userEmail;
	double highCourseRate = -1;
	double lowCourseRate = -1;
	double highTrainerRate;
	double lowTrainerRate;
	private static Set<String> orderBySet = new HashSet<String>(Arrays.asList(
			"courseRatings", "trainerRatings"));
	private static Set<String> concreteCourseOrderBySet = new HashSet<String>(
			Arrays.asList("id", "courseCategory", "price", "popularity",
					"rating"));

	private static Set<String> customerOrderBySet = new HashSet<String>(
			Arrays.asList("id", "courseCategory", "price", "popularity",
					"rating"));

	
	@Override
	public CriteriaQuery<Tuple> buildeQuery(CriteriaBuilder cb,
			String orderByColumn, boolean ascending) {
		CriteriaQuery<Tuple> criteria = cb.createTupleQuery();
		Root<Review> entityRoot = criteria.from(Review.class);
		Path<ConcreteCourse> concreteCourseRoot = entityRoot
				.get("concreteCourse");
		Path<Customer> customerRoot = entityRoot.get("author");
		Path<Course> courseRoot = concreteCourseRoot.get("courseInfo");
		List<Selection> selections = new ArrayList<Selection>();
		selections.add(0, entityRoot);
		criteria.multiselect(selections.toArray(new Selection[0])).distinct(
				true);
		List<Predicate> predicates = new ArrayList<Predicate>();
		if (courseId != -1) {
			predicates.add(cb.equal(courseRoot.<Integer> get("id"), courseId));
		}
		if(reviewId != -1){
			predicates.add(cb.equal(entityRoot.<Integer> get("id"), reviewId));
		}
		if (concreteCourseId != null) {
			predicates.add(cb.equal(
					concreteCourseRoot.<String> get("concreteCourseId"),
					concreteCourseId));
		}
		if (userEmail != null) {
			predicates.add(cb.equal(customerRoot.<String> get("email"),
					userEmail));
		}
		if(highCourseRate != -1){
			predicates.add(cb.lessThanOrEqualTo(entityRoot.<Double> get("courseRatings"),
					highCourseRate));
		}
		if(lowCourseRate != -1){
			predicates.add(cb.lessThanOrEqualTo(entityRoot.<Double> get("courseRatings"),
					lowCourseRate));
		}
		
		if(highTrainerRate != -1){
			predicates.add(cb.lessThanOrEqualTo(entityRoot.<Double> get("trainerRatings"),
					highTrainerRate));
		}
		if(lowTrainerRate != -1){
			predicates.add(cb.lessThanOrEqualTo(entityRoot.<Double> get("trainerRatings"),
					lowTrainerRate));
		}
		if (orderByColumn != null  && orderBySet.contains(orderByColumn)) {
			javax.persistence.criteria.Order order = ascending ? cb
					.asc(entityRoot.get(orderByColumn)) : cb
					.desc(entityRoot.get(orderByColumn));
			criteria.orderBy(order);
		} else if (orderByColumn != null  && concreteCourseOrderBySet.contains(orderByColumn)) {
			javax.persistence.criteria.Order order = ascending ? cb
					.asc(concreteCourseRoot.get(orderByColumn)) : cb
					.desc(concreteCourseRoot.get(orderByColumn));
			criteria.orderBy(order);
		} else if (orderByColumn != null  && customerOrderBySet.contains(orderByColumn)) {
			javax.persistence.criteria.Order order = ascending ? cb
					.asc(entityRoot.get(orderByColumn)) : cb
					.desc(entityRoot.get(orderByColumn));
			criteria.orderBy(order);
		}
		criteria.where(predicates.toArray(new Predicate[] {}));
		return criteria;
	}

	public int getCourseId() {
		return courseId;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	public String getConcreteCourseId() {
		return concreteCourseId;
	}

	public void setConcreteCourseId(String concreteCourseId) {
		this.concreteCourseId = concreteCourseId;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public double getHighCourseRate() {
		return highCourseRate;
	}

	public void setHighCourseRate(double highCourseRate) {
		this.highCourseRate = highCourseRate;
	}

	public double getLowCourseRate() {
		return lowCourseRate;
	}

	public void setLowCourseRate(double lowCourseRate) {
		this.lowCourseRate = lowCourseRate;
	}

	public double getHighTrainerRate() {
		return highTrainerRate;
	}

	public void setHighTrainerRate(double highTrainerRate) {
		this.highTrainerRate = highTrainerRate;
	}

	public double getLowTrainerRate() {
		return lowTrainerRate;
	}

	public void setLowTrainerRate(double lowTrainerRate) {
		this.lowTrainerRate = lowTrainerRate;
	}

	public int getReviewId() {
		return reviewId;
	}

	public void setReviewId(int reviewId) {
		this.reviewId = reviewId;
	}

	public static Set<String> getOrderBySet() {
		return orderBySet;
	}

	public static void setOrderBySet(Set<String> orderBySet) {
		ReviewFilterBuilder.orderBySet = orderBySet;
	}

	public static Set<String> getConcreteCourseOrderBySet() {
		return concreteCourseOrderBySet;
	}

	public static void setConcreteCourseOrderBySet(
			Set<String> concreteCourseOrderBySet) {
		ReviewFilterBuilder.concreteCourseOrderBySet = concreteCourseOrderBySet;
	}

	public static Set<String> getCustomerOrderBySet() {
		return customerOrderBySet;
	}

	public static void setCustomerOrderBySet(Set<String> customerOrderBySet) {
		ReviewFilterBuilder.customerOrderBySet = customerOrderBySet;
	}



}
