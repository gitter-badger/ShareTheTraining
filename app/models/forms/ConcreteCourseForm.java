package models.forms;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import models.courses.ConcreteCourse;
import models.courses.ConcreteCourseStatus;
import models.courses.Review;
import models.locations.Location;
import models.users.Customer;

public class ConcreteCourseForm {

	private int courseInfoId;

	private String concreteCourseId;

	private Collection<Customer> selectedCustomers = new ArrayList<Customer>();

	private Collection<Review> reviews = new ArrayList<Review>();

	private Date courseDate;

	private Time length;

	private Location location = new Location();

	private int minimum;

	private int maximum;

	private String eventbriteId;

	private ConcreteCourseStatus status = ConcreteCourseStatus.VERIFYING;

	public Collection<Customer> getSelectedCustomers() {
		return selectedCustomers;
	}

	public void setSelectedCustomers(Collection<Customer> selectedCustomers) {
		this.selectedCustomers = selectedCustomers;
	}

	public Collection<Review> getReviews() {
		return reviews;
	}

	public void setReviews(Collection<Review> reviews) {
		this.reviews = reviews;
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

	public String getEventbriteId() {
		return eventbriteId;
	}

	public void setEventbriteId(String eventbriteId) {
		this.eventbriteId = eventbriteId;
	}

	public ConcreteCourseStatus getStatus() {
		return status;
	}

	public void setStatus(ConcreteCourseStatus status) {
		this.status = status;
	}

	public int getCourseInfoId() {
		return courseInfoId;
	}

	public void setCourseInfoId(int courseInfoId) {
		this.courseInfoId = courseInfoId;
	}
	
	public String getConcreteCourseId() {
		return concreteCourseId;
	}

	public void setConcreteCourseId(String concreteCourseId) {
		this.concreteCourseId = concreteCourseId;
	}

	public boolean bindConcreteCourse(ConcreteCourse concreteCourse){
		if(concreteCourse == null ||concreteCourse.getConcreteCourseId()!=this.concreteCourseId)
			return false;
		//TODO a lot of set here
		return true;
	}
}
