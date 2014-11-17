package models.filters;

import java.util.ArrayList;
import java.util.List;

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
import models.users.Customer;

public class OrderFilterBuilder implements FilterBuilder {
	int courseId = -1;
	String concreteCourseId;
	String userEmail;
	int orderStatus = -1;

	@Override
	public CriteriaQuery<Tuple> buildeQuery(CriteriaBuilder cb,
			String orderByColumn, boolean ascending) {
		CriteriaQuery<Tuple> criteria = cb.createTupleQuery();
		Root<CourseOrder> entityRoot = criteria.from(CourseOrder.class);
		Path<ConcreteCourse> concreteCourseRoot = entityRoot
				.get("concreteCourse");
		Path<Customer> customerRoot = entityRoot.get("customer");
		Path<Course> courseRoot = concreteCourseRoot.get("courseInfo");
		List<Selection> selections = new ArrayList<Selection>();
		selections.add(0, entityRoot);
		criteria.multiselect(selections.toArray(new Selection[0])).distinct(
				true);
		List<Predicate> predicates = new ArrayList<Predicate>();
		if (orderStatus != -1) {
			predicates.add(cb.equal(
					entityRoot.<OrderStatus> get("orderStatus"),
					OrderStatus.fromInteger(orderStatus)));
		}
		if (courseId != -1) {
			predicates.add(cb.equal(courseRoot.<Integer> get("id"), courseId));
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
		if (orderByColumn != null) {
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

	public int getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(int orderStatus) {
		this.orderStatus = orderStatus;
	}


}
