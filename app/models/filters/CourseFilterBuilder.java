package models.filters;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import org.hibernate.jpa.criteria.CriteriaBuilderImpl;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.util.GeometricShapeFactory;

import models.courses.ConcreteCourse;
import models.courses.Course;
import models.courses.CourseStatus;
import models.courses.Review;
import models.locations.Location;
import models.spellchecker.SolrSuggestions;

public class CourseFilterBuilder implements FilterBuilder {
	private String keyword;
	private List<Location> locations = new ArrayList<Location>();
	private int courseRating = -1;
	private int trainerRating = -1;
	private Date startDate;
	private Date endDate;
	private double lowPrice = -1;
	private double highPrice = -1;
	private int category = -1;
	private Location curentLocation;

	@Override
	// can't order by date right now, I hope tomorrow morning when I wake up an
	// elf has fixed this.
	public CriteriaQuery<Tuple> buildeQuery(CriteriaBuilder cb,
			String orderByColumn, boolean ascending) {
		CriteriaQuery<Tuple> criteria = cb.createTupleQuery();
		Root<ConcreteCourse> entityRoot = criteria.from(ConcreteCourse.class);
		Path<ConcreteCourse> courseInfoRoot = entityRoot.get("courseInfo");
		Path<Location> locationRoot = entityRoot.get("location");
		Path<Review> reviews = entityRoot.get("reviews");
		List<Selection> selections = Course.getSelections(courseInfoRoot);
		selections.add(0, entityRoot.get("courseInfo"));
		criteria.multiselect(selections.toArray(new Selection[0])).distinct(
				true);
		List<Predicate> predicates = new ArrayList<Predicate>();
		predicates.add(cb.equal(courseInfoRoot.<CourseStatus> get("status"),
				CourseStatus.approved));
		if (keyword != null) {
			// keyword = SolrSuggestions.getSuggestions(keyword);
			keyword = keyword.replaceAll("\\s+", "%");
			predicates.add(cb.or(
					cb.like(courseInfoRoot.<String> get("courseName"), "%"
							+ keyword + "%"),
					cb.like(courseInfoRoot.<String> get("courseDesc"), "%"
							+ keyword + "%")));
		}
		if (category != -1) {
			predicates.add(cb.equal(
					courseInfoRoot.<Integer> get("courseCategory"), category));
		}
		if (courseRating != -1)
			predicates.add(cb.greaterThanOrEqualTo(
					reviews.<Integer> get("courseRating"), courseRating));
		if (trainerRating != -1)
			predicates.add(cb.greaterThanOrEqualTo(
					reviews.<Integer> get("trainerRating"), trainerRating));
		if (startDate != null)
			predicates.add(cb.greaterThanOrEqualTo(
					entityRoot.<Date> get("courseDate"), startDate));
		if (endDate != null)
			predicates.add(cb.lessThanOrEqualTo(
					entityRoot.<Date> get("courseDate"), endDate));
		if (lowPrice != -1)
			predicates.add(cb.greaterThanOrEqualTo(
					courseInfoRoot.<Double> get("price"), lowPrice));
		if (highPrice != -1)
			predicates.add(cb.lessThanOrEqualTo(
					courseInfoRoot.<Double> get("price"), highPrice));
		if (locations.size() > 0) {
			List<Predicate> locationQueries = new ArrayList<Predicate>();
			for (Location location : locations) {
				if (location.getCity() == -1) {
					locationQueries.add(cb.equal(
							locationRoot.<Integer> get("county"),
							location.getCounty()));
				} else {
					Predicate q1 = cb.equal(
							locationRoot.<Integer> get("county"),
							location.getCounty());
					Predicate q2 = cb.equal(locationRoot.<Integer> get("city"),
							location.getCity());
					locationQueries.add(cb.and(q1, q2));
				}
			}
			predicates.add(cb.or(locationQueries.toArray(new Predicate[] {})));
		}
		if (curentLocation != null) {
			predicates.add(new WithinPredicate((CriteriaBuilderImpl) cb,
					locationRoot.<Point> get("point"), createCircle(
							curentLocation.getPoint(), 0.36)));
		}
		// TODO add more filter here
		javax.persistence.criteria.Order order = ascending ? cb
				.asc(courseInfoRoot.get(orderByColumn)) : cb.desc(entityRoot
				.get(orderByColumn));
		criteria.orderBy(order);
		criteria.groupBy(entityRoot.get("courseInfo"));
		criteria.where(predicates.toArray(new Predicate[] {}));
		return criteria;
	}

	private static Geometry createCircle(Point point, final double RADIUS) {
		Coordinate[] coordinates = new Coordinate[5];
		double x = 0.3 + Math.abs(point.getX()/180 *0.5);
		coordinates[0] = new Coordinate(point.getX()+x, point.getY()+0.3);
		coordinates[1] = new Coordinate(point.getX()+x, point.getY()-0.3);
		coordinates[2] = new Coordinate(point.getX()-x, point.getY()-0.3);
		coordinates[3] = new Coordinate(point.getX()-x, point.getY()+0.3);
		coordinates[4] = new Coordinate(point.getX()+x, point.getY()+0.3);
		GeometryFactory fact = new GeometryFactory();
		LinearRing linear = new GeometryFactory().createLinearRing(coordinates);
		Polygon poly = new Polygon(linear, null, fact);
		/*
		GeometricShapeFactory shapeFactory = new GeometricShapeFactory();
		shapeFactory.setNumPoints(64);
		shapeFactory.setCentre(new Coordinate(x, y));
		shapeFactory.setSize(RADIUS * 2);
		return shapeFactory.createCircle();
		*/
		return poly;
	}

	public List<Location> getLocations() {
		return locations;
	}

	public void setLocations(List<Location> locations) {
		this.locations = locations;
	}

	public int getCourseRating() {
		return courseRating;
	}

	public void setCourseRating(int courseRating) {
		this.courseRating = courseRating;
	}

	public int getTrainerRating() {
		return trainerRating;
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

}
