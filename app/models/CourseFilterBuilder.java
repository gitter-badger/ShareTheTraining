package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

public class CourseFilterBuilder implements FilterBuilder{
	private int location = -1;
	private int courseRate = -1;
	private int trainerRate = -1;
	private Date startDate;
	private Date endDate;
	private double lowPrice = -1;
	private double highPrice = -1;
	private String keyword;
	
	@Override
	//can't order by date right now, I hope tomorrow morning when I wake up an elf has fixed this.
	public CriteriaQuery<Tuple> buildeQuery(CriteriaBuilder cb,
			String orderByColumn, boolean ascending) {
		CriteriaQuery<Tuple> criteria = cb.createTupleQuery();
		Root<ConcreteCourse> entityRoot = criteria.from(ConcreteCourse.class);
		Path<ConcreteCourse> courseInfo = entityRoot.get("courseInfo");
		Path<Location> location = entityRoot.get("location");
		List<Selection> selections = Course.getSelections(courseInfo);
		selections.add(0, entityRoot.get("courseInfo"));
		criteria.multiselect( selections.toArray(new Selection[0]) ).distinct(true);
		List<Predicate> predicates = new ArrayList<Predicate>();
		if(keyword != null)
			predicates.add(cb.like(entityRoot.<String>get("name"),"%"+keyword+"%"));
		if (startDate != null)
			predicates.add(cb.greaterThanOrEqualTo(entityRoot.<Date>get("courseDate"), startDate));
		if (endDate != null)
			predicates.add(cb.lessThanOrEqualTo(entityRoot.<Date>get("courseDate"), endDate));
		// TODO add more filter here
		javax.persistence.criteria.Order order = ascending ? cb.asc(courseInfo.get(orderByColumn))
		        : cb.desc(entityRoot.get(orderByColumn));
		criteria.orderBy(order);
		criteria.groupBy(entityRoot.get("courseInfo"));
		criteria.where(predicates.toArray(new Predicate[]{}));
		return criteria;
	}

	public int getLocation() {
		return location;
	}

	public void setLocation(int location) {
		this.location = location;
	}

	public int getCourseRate() {
		return courseRate;
	}

	public void setCourseRate(int courseRate) {
		this.courseRate = courseRate;
	}

	public int getTrainerRate() {
		return trainerRate;
	}

	public void setTrainerRate(int trainerRate) {
		this.trainerRate = trainerRate;
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
	
	
}
