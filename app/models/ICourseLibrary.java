package models;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface ICourseLibrary {
	public Course getCourseById(String courseId);
	
	public Collection<Course> getCourseByCategory(int category, int pageNumber, int pageSize);
	
	public Collection<Course> getCourseByTrainer(String trainerEmail, int pageNumber, int pageSize);
	
	public Collection<Course> getCourseByCustomRule(FilterBuilder cb);
}
