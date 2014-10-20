package models;

import java.util.Date;
import java.util.List;

public interface ICourseLibrary {
	public Course getCourseById(String courseID);
	
	public Course getCourseByCategory(int category, int pageNumber, int pageSize);
	
	public Course getCourseByTrainer(String trainerId, int pageNumber, int pageSize);
	
	public List<Course> getCourseByCustomRule(FilterBuilder cb);
}
