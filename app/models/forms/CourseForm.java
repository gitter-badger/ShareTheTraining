package models.forms;

import models.courses.ConcreteCourseStatus;
import models.locations.Location;

public class CourseForm {
	
	private String courseName;
	
	//id?emial?username?
	private String trainerEmail;
	
	private Location locations;
	
	private String keyPoints;
	
	private String courseDesc;
	
	private String startDate;
	
	private String endDate;
	
	private double price;
	
	private int maxnum;
	
	private int minmum;
	
	//room set up
	private String roomInfo;
	
	private String fromCompany;
	
	private int courseRating;
	
	private int trainerRating;
	
	private String methods;
	
	private String address;
	
	private ConcreteCourseStatus status;

	
	
	public ConcreteCourseStatus getStatus() {
		return status;
	}

	public void setStatus(ConcreteCourseStatus status) {
		this.status = status;
	}

	public String getMethods() {
		return methods;
	}

	public void setMethods(String methods) {
		this.methods = methods;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}



	public String getFromCompany() {
		return fromCompany;
	}

	public void setFromCompany(String fromCompany) {
		this.fromCompany = fromCompany;
	}


	public Location getLocations() {
		return locations;
	}

	public void setLocations(Location locations) {
		this.locations = locations;
	}

	public String getKeyPoints() {
		return keyPoints;
	}

	public void setKeyPoints(String keyPoints) {
		this.keyPoints = keyPoints;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getMaxnum() {
		return maxnum;
	}

	public void setMaxnum(int maxnum) {
		this.maxnum = maxnum;
	}

	public int getMinmum() {
		return minmum;
	}

	public void setMinnum(int minnum) {
		this.minmum = minmum;
	}

	public String getRoomInfo() {
		return roomInfo;
	}

	public void setRoomInfo(String roomInfo) {
		this.roomInfo = roomInfo;
	}
	
	public CourseForm(){
		
	}

	public String getTrainerEmail() {
		return trainerEmail;
	}

	public void setTrainerEmail(String trainerEmail) {
		this.trainerEmail = trainerEmail;
	}

	public String getCourseDesc() {
		return courseDesc;
	}

	public void setCourseDesc(String courseDesc) {
		this.courseDesc = courseDesc;
	}
	
	
	
}
