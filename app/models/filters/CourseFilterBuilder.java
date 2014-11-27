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

import org.hibernate.jpa.criteria.CriteriaBuilderImpl;

import play.Logger;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

import models.courses.ConcreteCourse;
import models.courses.ConcreteCourseStatus;
import models.courses.Course;
import models.courses.CourseStatus;
import models.courses.Review;
import models.locations.Location;
import models.users.Trainer;

public class CourseFilterBuilder implements FilterBuilder {

	public CourseFilterBuilder() {

	}

	private String keyword;
	private String trainerEmail;
	private List<Location> locations = new ArrayList<Location>();
	private double courseRating = -1;
	private double trainerRating = -1;
	private Date startDate;
	private Date endDate;
	private double lowPrice = -1;
	private double highPrice = -1;
	private int category = -1;
	private Location curentLocation;
	private int courseStatus = -1;
	private int concreteCourseStatus = -1;
	private boolean isVeteran = false;
	private boolean isNearBy = false;
	private static Set<String> orderBySet = new HashSet<String>(Arrays.asList(
			"courseCategory", "price", "popularity", "status", "rating"));

	@Override
	// can't order by date right now, I hope tomorrow morning when I wake up an
	// elf has fixed this.(fixed it in a dumb way, stupid me)
	public CriteriaQuery<Tuple> buildeQuery(CriteriaBuilder cb,
			String orderByColumn, boolean ascending) {
		CriteriaQuery<Tuple> criteria = cb.createTupleQuery();
		Root<ConcreteCourse> entityRoot = criteria.from(ConcreteCourse.class);
		Path<Course> courseInfoRoot = entityRoot.get("courseInfo");
		Path<Trainer> trainerRoot = courseInfoRoot.get("trainer");
		Path<Location> locationRoot = entityRoot.get("location");
		List<Selection> selections = Course.getSelections(courseInfoRoot);
		selections.add(0, entityRoot.get("courseInfo"));
		criteria.multiselect(selections.toArray(new Selection[0])).distinct(
				true);
		List<Predicate> predicates = new ArrayList<Predicate>();
		if (courseStatus != -1)
			predicates.add(cb.equal(
					courseInfoRoot.<CourseStatus> get("status"),
					CourseStatus.fromInteger(courseStatus)));
		if (concreteCourseStatus != -1)
			predicates.add(cb.equal(
					entityRoot.<ConcreteCourseStatus> get("status"),
					ConcreteCourseStatus.fromInteger(courseStatus)));
		if (keyword != null && !keyword.equals("")) {
			// keyword = SolrSuggestions.getSuggestions(keyword);
			keyword = keyword.replaceAll("\\s+", "%");
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
		if (startDate != null)
			predicates.add(cb.greaterThanOrEqualTo(
					entityRoot.<Date> get("courseStartDate"), startDate));
		if (endDate != null)
			predicates.add(cb.lessThanOrEqualTo(
					entityRoot.<Date> get("courseStartDate"), endDate));
		if (lowPrice != -1)
			predicates.add(cb.greaterThanOrEqualTo(
					courseInfoRoot.<Double> get("price"), lowPrice));
		if (highPrice != -1)
			predicates.add(cb.lessThanOrEqualTo(
					courseInfoRoot.<Double> get("price"), highPrice));
		if (locations.size() > 0) {
			List<Predicate> locationQueries = new ArrayList<Predicate>();
			for (Location location : locations) {
				if (location.getRegion() != null
						&& !location.getRegion().equals("")
						&& curentLocation != null) {
					if (location.getRegion().equals("nearby")) {
						locationQueries.add(new WithinPredicate(
								(CriteriaBuilderImpl) cb, locationRoot
										.<Point> get("point"), createTriangle(
										curentLocation.getPoint(), 0.36)));
						continue;
					}
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
		// TODO add more filter here
		if (orderByColumn != null && orderBySet.contains(orderByColumn)) {
			javax.persistence.criteria.Order order = ascending ? cb
					.asc(entityRoot.get(orderByColumn)) : cb
					.desc(entityRoot.get(orderByColumn));
			criteria.orderBy(order);
		}
		criteria.groupBy(entityRoot.get("courseInfo"));
		criteria.where(predicates.toArray(new Predicate[] {}));
		return criteria;
	}

	public static Geometry createTriangle(Point point, final double RADIUS) {
		Coordinate[] coordinates = new Coordinate[5];
		double x = 0.3 + Math.abs(point.getX() / 180 * 0.5);
		coordinates[0] = new Coordinate(point.getX() + x, point.getY() + 0.3);
		coordinates[1] = new Coordinate(point.getX() + x, point.getY() - 0.3);
		coordinates[2] = new Coordinate(point.getX() - x, point.getY() - 0.3);
		coordinates[3] = new Coordinate(point.getX() - x, point.getY() + 0.3);
		coordinates[4] = new Coordinate(point.getX() + x, point.getY() + 0.3);
		GeometryFactory fact = new GeometryFactory();
		LinearRing linear = new GeometryFactory().createLinearRing(coordinates);
		Polygon poly = new Polygon(linear, null, fact);
		/*
		 * GeometricShapeFactory shapeFactory = new GeometricShapeFactory();
		 * shapeFactory.setNumPoints(64); shapeFactory.setCentre(new
		 * Coordinate(x, y)); shapeFactory.setSize(RADIUS * 2); return
		 * shapeFactory.createCircle();
		 */
		return poly;
	}

	public List<Location> getLocations() {
		return locations;
	}

	public void setLocations(List<Location> locations) {
		this.locations = locations;
	}

	public void setTrainerRating(int trainerRating) {
		this.trainerRating = trainerRating;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
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

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Location getCurentLocation() {
		return curentLocation;
	}

	public void setCurentLocation(Location curentLocation) {
		this.curentLocation = curentLocation;
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

	public int getConcreteCourseStatus() {
		return concreteCourseStatus;
	}

	public void setConcreteCourseStatus(int concreteCourseStatus) {
		this.concreteCourseStatus = concreteCourseStatus;
	}

	public Boolean getIsVeteran() {
		return isVeteran;
	}

	public String getTrainerEmail() {
		return trainerEmail;
	}

	public void setTrainerEmail(String trainerEmail) {
		this.trainerEmail = trainerEmail;
	}

	public void setVeteran(boolean isVeteran) {
		this.isVeteran = isVeteran;
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

	public boolean isNearBy() {
		return isNearBy;
	}

	public void setNearBy(boolean isNearBy) {
		this.isNearBy = isNearBy;
	}

}
