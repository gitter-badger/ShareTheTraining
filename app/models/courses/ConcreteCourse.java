package models.courses;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;


import com.fasterxml.jackson.annotation.JsonIgnore;

import models.locations.Location;
import models.spellchecker.SolrDao;
import models.users.Customer;
import common.BaseModelObject;

@Entity
public class ConcreteCourse extends BaseModelObject {

	public static ConcreteCourse create(Course courseInfo, EntityManager em) {
		ConcreteCourse concreteCourse = new ConcreteCourse();
		concreteCourse.setConcreteCourseId(courseInfo.getId() + "-"
				+ Integer.toString(courseInfo.getConcreteCourseCount()));
		courseInfo
				.setConcreteCourseCount(courseInfo.getConcreteCourseCount() + 1);
		concreteCourse.setCourseInfo(courseInfo);
		courseInfo.addConcreteCourse(concreteCourse);
		em.persist(concreteCourse);
		concreteCourse.putSolrDoc();
		return concreteCourse;
	}

	@JsonIgnore
	@ManyToOne
	private Course courseInfo;

	private String concreteCourseId;

	@ManyToMany(cascade = { CascadeType.ALL })
	private Collection<Customer> selectedCustomers = new ArrayList<Customer>();

	@OneToMany(mappedBy = "concreteCourse", cascade = { CascadeType.ALL })
	private Collection<WaitListRecord> waitListRecords = new ArrayList<WaitListRecord>();

	private Date courseStartDate;
	
	private Date courseEndDate;

	@ElementCollection
	private List<Date> courseDates = new ArrayList<Date>();

	private String length;

	private Location location = new Location("", "", "", 0, 0);

	private String eventbriteId;

	private ConcreteCourseStatus status = ConcreteCourseStatus.VERIFYING;
	
	private int minimum = -1;

	private int maximum = -1;

	public void enrollCustomer(Customer customer) {
		this.selectedCustomers.add(customer);
		this.courseInfo.setPopularity(this.courseInfo.getPopularity() + 1);
	}

	public void removeCustomer(Customer customer) {
		this.selectedCustomers.remove(customer);
	}

	public Course getCourseInfo() {
		return courseInfo;
	}

	public void setCourseInfo(Course courseInfo) {
		this.courseInfo = courseInfo;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Collection<Customer> getSelectedCustomers() {
		return selectedCustomers;
	}

	public void setSelectedCustomers(Collection<Customer> selectedCustomers) {
		this.selectedCustomers = selectedCustomers;
	}

	public Collection<WaitListRecord> getWaitListRecords() {
		return waitListRecords;
	}

	public void setWaitListRecords(Collection<WaitListRecord> waitListRecords) {
		this.waitListRecords = waitListRecords;
	}

	public ConcreteCourseStatus getStatus() {
		return status;
	}

	public void setStatus(ConcreteCourseStatus status) {
		this.status = status;
	}

	public String getEventbriteId() {
		return eventbriteId;
	}

	public void setEventbriteId(String eventbriteId) {
		this.eventbriteId = eventbriteId;
	}

	public String getEventbriteUrl() {
		return "http://www.eventbrite.com/e/software-engineering-tickets-"
				+ eventbriteId;
	}

	public Collection<Date> getCourseDates() {
		return courseDates;
	}

	public void setCourseDates(List<Date> courseDates) {
		this.courseDates = courseDates;
		if (courseDates.size() > 0) {
			Collections.sort(courseDates);
			this.setCourseStartDate(courseDates.get(0));
			this.setCourseEndDate(courseDates.get(courseDates.size()-1));
		}
	}

	public String getConcreteCourseId() {
		return concreteCourseId;
	}

	public void setConcreteCourseId(String concreteCourseId) {
		this.concreteCourseId = concreteCourseId;
	}

	public int getMinimum() {
		return minimum;
	}

	public void setMinimum(int minimum) {
		this.minimum = minimum;
	}

	public int getMaximum() {
		return maximum;
	}

	public void setMaximum(int maximum) {
		this.maximum = maximum;
	}

	public Date getCourseStartDate() {
		return courseStartDate;
	}

	public void setCourseStartDate(Date courseStartDate) {
		this.courseStartDate = courseStartDate;
	}

	public Date getCourseEndDate() {
		return courseEndDate;
	}

	public void setCourseEndDate(Date courseEndDate) {
		this.courseEndDate = courseEndDate;
	}
	
	

}
