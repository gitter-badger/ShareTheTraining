package models.forms;

import java.util.ArrayList;
import java.util.Collection;

import models.courses.ConcreteCourse;
import models.courses.Course;
import models.courses.CourseStatus;
import models.users.Trainer;

public class CourseForm {
	
	private int courseId;

	private String courseName;
	
	private Trainer trainer;

	private int courseCategory;
	
	private double price;

	private String fromCompany;
	
	private String courseDesc;

	private CourseStatus status = CourseStatus.VERIFYING;
	
	private String methods;
	
	private String keyPoints;
	
	
	
	private Collection<ConcreteCourse> courses = new ArrayList<ConcreteCourse>();

	public int getCourseId() {
		return courseId;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public Trainer getTrainer() {
		return trainer;
	}

	public void setTrainer(Trainer trainer) {
		this.trainer = trainer;
	}

	public int getCourseCategory() {
		return courseCategory;
	}

	public void setCourseCategory(int courseCategory) {
		this.courseCategory = courseCategory;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getFromCompany() {
		return fromCompany;
	}

	public void setFromCompany(String fromCompany) {
		this.fromCompany = fromCompany;
	}

	public String getCourseDesc() {
		return courseDesc;
	}

	public void setCourseDesc(String courseDesc) {
		this.courseDesc = courseDesc;
	}

	public CourseStatus getStatus() {
		return status;
	}

	public void setStatus(CourseStatus status) {
		this.status = status;
	}

	public String getMethods() {
		return methods;
	}

	public void setMethods(String methods) {
		this.methods = methods;
	}

	public String getKeyPoints() {
		return keyPoints;
	}

	public void setKeyPoints(String keyPoints) {
		this.keyPoints = keyPoints;
	}

	public Collection<ConcreteCourse> getCourses() {
		return courses;
	}

	public void setCourses(Collection<ConcreteCourse> courses) {
		this.courses = courses;
	}
	
	public boolean bindCourse(Course course){
		if(course == null ||course.getId()!=this.courseId)
			return false;
		//TODO a lot of set here
		return true;
	}
	
	
}
