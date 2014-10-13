package models;

import java.util.Date;

public interface ICourseLibrary {
	public Course getCourseById(String courseID);
	
	public Course getCourseByCategory(int category, int pageNumber, int pageSize);
	
	public Course getCourseByTrainer(int location, int pageNumber, int pageSize);
	
	public Course getCourseByCustomRule();
}
