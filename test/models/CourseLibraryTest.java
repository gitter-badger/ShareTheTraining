package models;

import static org.fest.assertions.Assertions.assertThat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import common.BaseTest;

public class CourseLibraryTest extends BaseTest {
	@Test
	public void testGetCourseById() {

		CourseLibrary courseLibrary = new CourseLibrary(this.getmEm());
		assertThat(initData.course1).isEqualTo(
				courseLibrary.getCourseById(initData.course1.getCourseId()));
	}

	@Test
	public void testGetCourseByCategory() {
		CourseLibrary courseLibrary = new CourseLibrary(this.getmEm());
		assertThat(courseLibrary.getCourseByCategory(1, 1, 10).size())
				.isEqualTo(1);
	}
	
	@Test
	public void testFilterCourseByKeyword() {
		CourseLibrary courseLibrary = new CourseLibrary(this.getmEm());
		CourseFilterBuilder cb = new CourseFilterBuilder();
		cb.setKeyword("la");
		Collection<Course> result = courseLibrary.getCourseByCustomRule(cb);
		assertThat(result.size()).isEqualTo(1);
		
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
		Collection<Course> result = courseLibrary.getCourseByCustomRule(cb);
		assertThat(result.size()).isEqualTo(2);
	}

	@Test
	public void testFilterCourseByLocation() {
		CourseLibrary courseLibrary = new CourseLibrary(this.getmEm());
		CourseFilterBuilder cb = new CourseFilterBuilder();
		List<Location> locations =  new ArrayList<Location>();
		locations.add(new Location(1,-1,""));
		locations.add(new Location(2,1,""));
		cb.setLocations(locations);
		Collection<Course> result = courseLibrary.getCourseByCustomRule(cb);
		assertThat(result.size()).isEqualTo(2);
	}
	
	@Test
	public void testFilterCourseByMultiOption(){
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
		List<Location> locations =  new ArrayList<Location>();
		locations.add(new Location(1,-1,""));
		locations.add(new Location(2,1,""));
		cb.setLocations(locations);
		Collection<Course> result = courseLibrary.getCourseByCustomRule(cb);
		assertThat(result.size()).isEqualTo(2);
	}

}
