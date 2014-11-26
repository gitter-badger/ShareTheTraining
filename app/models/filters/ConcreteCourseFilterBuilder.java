package models.filters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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

public class ConcreteCourseFilterBuilder implements FilterBuilder {
	private CourseFilterBuilder cfb;

	private Collection<Course> courseList;

	private int courseId = -1;

	private static Set<String> orderBySet = new HashSet<String>(Arrays.asList(
			"status", "courseDate"));

	private static Set<String> courseOrderBySet = new HashSet<String>(
			Arrays.asList("id", "courseCategory", "price", "popularity",
					"rating"));

	@Override
	public CriteriaQuery<Tuple> buildeQuery(CriteriaBuilder cb,
			String orderByColumn, boolean ascending) {
		CriteriaQuery<Tuple> criteria = cb.createTupleQuery();
		Root<ConcreteCourse> entityRoot = criteria.from(ConcreteCourse.class);
		Path<Course> courseInfoRoot = entityRoot.get("courseInfo");
		Path<Trainer> trainerRoot = courseInfoRoot.get("trainer");
		Path<Location> locationRoot = entityRoot.get("location");
		List<Selection> selections = new ArrayList<Selection>();
		selections.add(0, entityRoot);
		criteria.multiselect(selections.toArray(new Selection[0]));
		List<Predicate> predicates = new ArrayList<Predicate>();
		if (courseList != null) {
			predicates.add(courseInfoRoot.in(courseList));
		}
		if (courseId != -1) {
			predicates.add(cb.equal(courseInfoRoot.<Integer> get("id"),
					courseId));
		}
		if (cfb.getCourseStatus() != -1)
			predicates.add(cb.equal(
					courseInfoRoot.<CourseStatus> get("status"),
					CourseStatus.fromInteger(cfb.getCourseStatus())));
		if (cfb.getConcreteCourseStatus() != -1)
			predicates.add(cb.equal(
					entityRoot.<ConcreteCourseStatus> get("status"),
					ConcreteCourseStatus.fromInteger(cfb.getCourseStatus())));
		if (cfb.getKeyword() != null && !cfb.getKeyword().equals("")) {
			// keyword = SolrSuggestions.getSuggestions(keyword);
			String keyword = cfb.getKeyword().replaceAll("\\s+", "%");
			Predicate keyWordConditions = cb.disjunction();
			keyWordConditions.getExpressions().add(
					cb.like(courseInfoRoot.<String> get("courseName"), "%"
							+ keyword + "%"));
			keyWordConditions.getExpressions().add(
					cb.like(courseInfoRoot.<String> get("keyPoints"), "%"
							+ keyword + "%"));
			keyWordConditions.getExpressions().add(
					cb.like(trainerRoot.<String> get("username"), "%" + keyword
							+ "%"));
			predicates.add(keyWordConditions);
		}
		if (cfb.getTrainerEmail() != null) {
			predicates.add(cb.equal(trainerRoot.<String> get("email"),
					cfb.getTrainerEmail()));
		}
		if (cfb.getIsVeteran() == true) {
			predicates.add(cb.equal(trainerRoot.<Boolean> get("isVeteran"),
					cfb.getIsVeteran()));
		}
		if (cfb.getCategory() != -1) {
			predicates.add(cb.equal(
					courseInfoRoot.<Integer> get("courseCategory"),
					cfb.getCategory()));
		}
		if (cfb.getCourseRating() != -1)
			predicates.add(cb.greaterThanOrEqualTo(
					courseInfoRoot.<Double> get("rating"),
					cfb.getCourseRating()));
		if (cfb.getTrainerRating() != -1)
			predicates
					.add(cb.greaterThanOrEqualTo(
							trainerRoot.<Double> get("rating"),
							cfb.getTrainerRating()));
		if (cfb.getStartDate() != null)
			predicates.add(cb.greaterThanOrEqualTo(
					entityRoot.<Date> get("courseDate"), cfb.getStartDate()));
		if (cfb.getEndDate() != null)
			predicates.add(cb.lessThanOrEqualTo(
					entityRoot.<Date> get("courseDate"), cfb.getEndDate()));
		if (cfb.getLowPrice() != -1)
			predicates.add(cb.greaterThanOrEqualTo(
					courseInfoRoot.<Double> get("price"), cfb.getLowPrice()));
		if (cfb.getHighPrice() != -1)
			predicates.add(cb.lessThanOrEqualTo(
					courseInfoRoot.<Double> get("price"), cfb.getHighPrice()));
		if (cfb.getLocations().size() > 0) {
			List<Predicate> locationQueries = new ArrayList<Predicate>();
			for (Location location : cfb.getLocations()) {
				if (location.getRegion() != null
						&& !location.getRegion().equals("")) {
					if (location.getCity() == null
							|| location.getCity().equals("")) {
						locationQueries.add(cb.equal(
								locationRoot.<String> get("region"),
								location.getRegion()));
					} else {
						Predicate q1 = cb.equal(
								locationRoot.<String> get("region"),
								location.getRegion());
						Predicate q2 = cb.equal(
								locationRoot.<String> get("city"),
								location.getCity());
						locationQueries.add(cb.and(q1, q2));
					}
				}
			}
			if (locationQueries.size() > 0)
				predicates.add(cb.or(locationQueries
						.toArray(new Predicate[] {})));
		}
		if (cfb.getCurentLocation() != null) {
			predicates.add(new WithinPredicate((CriteriaBuilderImpl) cb,
					locationRoot.<Point> get("point"), CourseFilterBuilder
							.createTriangle(cfb.getCurentLocation().getPoint(),
									0.36)));
		}
		// TODO add more filter here
		if (orderByColumn != null && orderBySet.contains(orderByColumn)) {
			javax.persistence.criteria.Order order = ascending ? cb
					.asc(entityRoot.get(orderByColumn)) : cb
					.desc(entityRoot.get(orderByColumn));
			criteria.orderBy(order);
		} else if (orderByColumn != null
				&& courseOrderBySet.contains(orderByColumn)) {
			javax.persistence.criteria.Order order = ascending ? cb
					.asc(courseInfoRoot.get(orderByColumn)) : cb
					.desc(courseInfoRoot.get(orderByColumn));
			criteria.orderBy(order);
		}
		criteria.where(predicates.toArray(new Predicate[] {}));
		return criteria;
	}

	public CourseFilterBuilder getCfb() {
		return cfb;
	}

	public void setCfb(CourseFilterBuilder cfb) {
		this.cfb = cfb;
	}

	public Collection<Course> getCourseList() {
		return courseList;
	}

	public void setCourseList(Collection<Course> courseList) {
		this.courseList = courseList;
	}

	public int getCourseId() {
		return courseId;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}
}
