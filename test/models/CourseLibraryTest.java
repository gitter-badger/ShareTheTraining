package models;

import static org.fest.assertions.Assertions.assertThat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import common.BaseTest;

public class CourseLibraryTest extends BaseTest {
	@Test
	public void testGetCourseById() {

		CourseLibrary courseLibrary = new CourseLibrary(this.getmEm());
		assertThat(initData.course1).isEqualTo(courseLibrary.getCourseById(initData.course1.getCourseID()));
		
		//List<String> result = courseLibrary.getCourseByCustomRule();
		//assertThat(result.size()).isEqualTo(1);
		//System.out.println(result.get(0));
	}
	
	@Test
	public void testFilterCourseByStartDate() {
		CourseLibrary courseLibrary = new CourseLibrary(this.getmEm());
		CourseFilterBuilder cb = new CourseFilterBuilder();
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
		String dateInString = "10-01-1949";
		Date date = new Date();
		try {
			date = sdf.parse(dateInString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cb.setStartDate(date);
		List<Course> result = courseLibrary.getCourseByCustomRule(cb);
		assertThat(result.size()).isEqualTo(1);
		System.out.println(result.get(0).getCourseID()+"lala");
	}
	
}
