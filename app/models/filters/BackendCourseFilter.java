package models.filters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
import models.courses.ConcreteCourseStatus;
import models.courses.Course;
import models.courses.CourseStatus;
import models.locations.Location;
import models.users.Trainer;

import org.hibernate.jpa.criteria.CriteriaBuilderImpl;

import com.vividsolutions.jts.geom.Point;

public class BackendCourseFilter implements FilterBuilder {
	private String trainerEmail;
	private double courseRating = -1;
	private double trainerRating = -1;
	private double lowPrice = -1;
	private double highPrice = -1;
	private int category = -1;
	private int courseStatus = -1;
	private boolean isVeteran = false;
	private static Set<String> orderBySet = new HashSet<String>(Arrays.asList(
			"courseCategory", "price", "popularity", "status", "rating"));

	@Override
	public CriteriaQuery<Tuple> buildeQuery(CriteriaBuilder cb,
			String orderByColumn, boolean ascending) {
		CriteriaQuery<Tuple> criteria = cb.createTupleQuery();
		Root<Course> courseInfoRoot = criteria.from(Course.class);
		Path<Trainer> trainerRoot = courseInfoRoot.get("trainer");
		List<Selection> selections = new ArrayList<Selection>();
		selections.add(0, courseInfoRoot);
		criteria.multiselect(selections.toArray(new Selection[0])).distinct(
				true);
		List<Predicate> predicates = new ArrayList<Predicate>();
		if (courseStatus != -1)
			predicates.add(cb.equal(
					courseInfoRoot.<CourseStatus> get("status"),
					CourseStatus.fromInteger(courseStatus)));
		
		if (trainerEmail != null) {
			predicates.add(cb.equal(trainerRoot.<String> get("email"),
					trainerEmail));
		}
		if (isVeteran == true) {
			predicates.add(cb.equal(trainerRoot.<Boolean> get("isVeteran"),
					isVeteran));
		}
		if (category != -1) {
			predicates.add(cb.equal(
					courseInfoRoot.<Integer> get("courseCategory"), category));
		}
		if (courseRating != -1)
			predicates.add(cb.greaterThanOrEqualTo(
					courseInfoRoot.<Double> get("rating"), courseRating));
		if (trainerRating != -1)
			predicates.add(cb.greaterThanOrEqualTo(
					trainerRoot.<Double> get("rating"), trainerRating));
		if (lowPrice != -1)
			predicates.add(cb.greaterThanOrEqualTo(
					courseInfoRoot.<Double> get("price"), lowPrice));
		if (highPrice != -1)
			predicates.add(cb.lessThanOrEqualTo(
					courseInfoRoot.<Double> get("price"), highPrice));
		
		// TODO add more filter here
		if (orderByColumn != null && orderBySet.contains(orderByColumn)) {
			javax.persistence.criteria.Order order = ascending ? cb
					.asc(courseInfoRoot.get(orderByColumn)) : cb
					.desc(courseInfoRoot.get(orderByColumn));
			criteria.orderBy(order);
		}
		criteria.where(predicates.toArray(new Predicate[] {}));
		return criteria;
	}

	public String getTrainerEmail() {
		return trainerEmail;
	}

	public void setTrainerEmail(String trainerEmail) {
		this.trainerEmail = trainerEmail;
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

	public double getLowPrice() {
		return lowPrice;
	}

	public void setLowPrice(double lowPrice) {
		this.lowPrice = lowPrice;
	}

	public double getHighPrice() {
		return highPrice;
	}

	public void setHighPrice(double highPrice) {
		this.highPrice = highPrice;
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public int getCourseStatus() {
		return courseStatus;
	}

	public void setCourseStatus(int courseStatus) {
		this.courseStatus = courseStatus;
	}

	public boolean isVeteran() {
		return isVeteran;
	}

	public void setVeteran(boolean isVeteran) {
		this.isVeteran = isVeteran;
	}

	public static Set<String> getOrderBySet() {
		return orderBySet;
	}

	public static void setOrderBySet(Set<String> orderBySet) {
		BackendCourseFilter.orderBySet = orderBySet;
	}

}
