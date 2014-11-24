package models.forms;

import java.util.ArrayList;
import java.util.Collection;

import models.courses.ConcreteCourse;
import models.courses.Course;
import models.courses.CourseStatus;
import models.users.Trainer;

public class CourseForm {
	
	
	
	private Integer courseId;

	private String courseName;
	
	private String trainerId;

	private String trainerName;
	
	private String trainerEmail;
	
	private int courseCategory;
	
	private Double price;

	private String fromCompany;
	
	private String courseDesc;

	private CourseStatus status = CourseStatus.VERIFYING;
	
	private String methods;
	
	private String keyPoints;
	
	private String companyInfo;
	
	private int minimum;

	private int maximum;
	
	
	
	private Collection<ConcreteCourse> courses = new ArrayList<ConcreteCourse>();
	
	

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

	public Integer getCourseId() {
		return courseId;
	}

	public void setCourseId(Integer courseId) {
		this.courseId = courseId;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}


	public String getTrainerId() {
		return trainerId;
	}

	public void setTrainerId(String trainerId) {
		this.trainerId = trainerId;
	}

	public int getCourseCategory() {
		return courseCategory;
	}

	public void setCourseCategory(int courseCategory) {
		this.courseCategory = courseCategory;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
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
	
	
	public String getCompanyInfo() {
		return companyInfo;
	}

	public void setCompanyInfo(String companyInfo) {
		this.companyInfo = companyInfo;
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

	public boolean bindCourse(Course course) {
		if (course == null
				|| course.getId() != this.courseId)
			return false;
		//TODO popular
		course.setCourseName(courseName);
		course.setCourseCategory(courseCategory);
		course.setPrice(price);
		course.setCourseDesc(courseDesc);
		course.setStatus(status);
		course.setMaximum(maximum);
		course.setMinimum(minimum);
		
		
		return true;
	}
	
	public static CourseForm bindCourseForm(
			Course course) {
		if (course == null)
			return null;
		CourseForm courseForm = new CourseForm();
		courseForm.setCourseCategory(course.getCourseCategory());
		courseForm.setCourseDesc(course.getCourseDesc());
		courseForm.setCourseName(course.getCourseName());
		courseForm.setKeyPoints(course.getKeyPoints());
		courseForm.setMethods(course.getMethods());
		courseForm.setPrice(course.getPrice());
		courseForm.setStatus(course.getStatus());
		courseForm.setFromCompany(course.getTrainer().getCompany());
		courseForm.setTrainerEmail(course.getTrainer().getEmail());
		courseForm.setTrainerName(course.getTrainer().getName());
		courseForm.setCompanyInfo(course.getTrainer().getCompanyInfo());
		courseForm.setMaximum(course.getMaximum());
		courseForm.setMinimum(course.getMinimum());
		courseForm.setCourseId(course.getId());
		
		
		return courseForm;

	}
	
}
