package models;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import common.BaseModelObject;

@Entity
public class ConcreteCourse extends BaseModelObject {

	public static ConcreteCourse create(Course courseInfo, EntityManager em) {
		ConcreteCourse concreteCourse = new ConcreteCourse();
		concreteCourse.setCourseInfo(courseInfo);
		em.persist(concreteCourse);
		return concreteCourse;
	}

	@ManyToOne
	private Course courseInfo;

	@ManyToOne
	private Trainer trainer;

	@ManyToMany(cascade = { CascadeType.ALL })
	private Collection<Customer> selectedCustomers = new ArrayList<Customer>();

	private Date courseDate;

	private Time length;

	private Location location = new Location();

	public Course getCourseInfo() {
		return courseInfo;
	}

	public void setCourseInfo(Course courseInfo) {
		this.courseInfo = courseInfo;
	}

	public Date getCourseDate() {
		return courseDate;
	}

	public void setCourseDate(Date courseDate) {
		this.courseDate = courseDate;
	}

	public Time getLength() {
		return length;
	}

	public void setLength(Time length) {
		this.length = length;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}


}
