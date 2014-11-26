package models.forms;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatTypes;

import models.courses.ConcreteCourse;
import models.courses.ConcreteCourseStatus;
import models.courses.Review;
import models.locations.Location;
import models.users.Customer;
import models.users.Trainer;
import models.users.User;
import models.users.UserRole;

public class ConcreteCourseForm {
	

	private String courseName;

	private String trainerName;

	private String trainerEmail;

	private int soldSeat;

	private int courseInfoId;

	private String concreteCourseId;

	private Collection<Customer> selectedCustomers = new ArrayList<Customer>();

	private Collection<Review> reviews = new ArrayList<Review>();

	private Date courseDate;

	private String length;
	
	private String courseLength;

	private Location location = new Location();

	private int minimum;

	private int maximum;

	private String eventbriteId;

	@JsonFormat(shape= JsonFormat.Shape.NUMBER_INT)
	private ConcreteCourseStatus status = ConcreteCourseStatus.VERIFYING;

	private String roomset;
	
	private List<Date> courseDates;

	public String getRoomset() {
		return roomset;
	}

	public void setRoomset(String roomset) {
		this.roomset = roomset;
	}

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

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getTrainerName() {
		return trainerName;
	}

	public void setTrainerName(String trainerName) {
		this.trainerName = trainerName;
	}

	public String getTrainerEmail() {
		return trainerEmail;
	}

	public void setTrainerEmail(String trainerEmail) {
		this.trainerEmail = trainerEmail;
	}

	public int getSoldSeat() {
		return soldSeat;
	}

	public void setSoldSeat(int soldSeat) {
		this.soldSeat = soldSeat;
	}
	

	public String getCourseLength() {
		return courseLength;
	}

	public void setCourseLength(String courseLength) {
		this.courseLength = courseLength;
	}

	public ConcreteCourseForm() {

	}

	public boolean bindConcreteCourse(ConcreteCourse concreteCourse) {
		if (concreteCourse == null
				|| concreteCourse.getConcreteCourseId() != this.concreteCourseId)
			return false;
		// TODO a lot of set here
		concreteCourse.setConcreteCourseId(concreteCourseId);
		concreteCourse.setCourseDates(courseDates);
		concreteCourse.setEventbriteId(eventbriteId);
		concreteCourse.setLocation(location);
		concreteCourse.setStatus(status);
		concreteCourse.setLength(courseLength);
		
		
		
		return true;
	}

	public static ConcreteCourseForm bindConcreteCourseForm(
			ConcreteCourse concreteCourse) {
		if (concreteCourse == null)
			return null;
		ConcreteCourseForm concreteCourseForm = new ConcreteCourseForm();
		concreteCourseForm.setCourseName(concreteCourse.getCourseInfo()
				.getCourseName());
		concreteCourseForm.setTrainerName(concreteCourse.getCourseInfo()
				.getTrainer().getName());
		concreteCourseForm.setTrainerEmail(concreteCourse.getCourseInfo()
				.getTrainer().getEmail());
		concreteCourseForm.getLocation().setRegion(
				concreteCourse.getLocation().getRegion());
		concreteCourseForm.getLocation().setCity(
				concreteCourse.getLocation().getCity());
		concreteCourseForm.setCourseDate(concreteCourse.getCourseDate());
		concreteCourseForm.setConcreteCourseId(concreteCourse.getConcreteCourseId());
		concreteCourseForm.setTrainerEmail(concreteCourse.getCourseInfo().getTrainer().getEmail());
		concreteCourseForm.setCourseDate(concreteCourse.getCourseDate());
		concreteCourseForm.setSoldSeat(concreteCourse.getSelectedCustomers().size());
		concreteCourseForm.setCourseLength(concreteCourse.getLength());
		//concreteCourseForm.setCourseDates(concreteCourse.getCourseDates());
		
		return concreteCourseForm;

	}

	public List<Date> getCourseDates() {
		return courseDates;
	}

	public void setCourseDates(List<Date> courseDates) {
		this.courseDates = courseDates;
	}
}
