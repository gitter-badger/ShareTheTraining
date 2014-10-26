package models;

import static org.fest.assertions.Assertions.assertThat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import models.courses.Course;
import models.courses.CourseLibrary;
import models.filters.CourseFilterBuilder;
import models.locations.Location;

import org.junit.Test;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;

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
		cb.setKeyword("xingbuxin");
		Collection<Course> result = courseLibrary.getCourseByCustomRule(cb, 1, 10);
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
		Collection<Course> result = courseLibrary.getCourseByCustomRule(cb, 1, 10);
		assertThat(result.size()).isEqualTo(2);
	}

	@Test
	public void testFilterCourseByLocation() {
		CourseLibrary courseLibrary = new CourseLibrary(this.getmEm());
		CourseFilterBuilder cb = new CourseFilterBuilder();
		List<Location> locations =  new ArrayList<Location>();
		locations.add(new Location(1,-1,"", -118.14 , 34.03));
		locations.add(new Location(2,1,"", -118.14 , 34.03));
		cb.setLocations(locations);
		Collection<Course> result = courseLibrary.getCourseByCustomRule(cb, 1, 10);
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
		locations.add(new Location(1,-1,"",-118.14 , 34.03));
		locations.add(new Location(2,1,"",-118.14 , 34.03));
		cb.setLocations(locations);
		Collection<Course> result = courseLibrary.getCourseByCustomRule(cb, 1, 10);
		assertThat(result.size()).isEqualTo(2);
	}
	
	@Test
	public void testSearchNearbyCourse(){
		GeoApiContext context = new GeoApiContext().setApiKey("AIzaSyA8Msu-j7RYqgcmHNzbdtSG4G5m12CFJ_o");
		GeocodingResult[] results;
		try {
			results = GeocodingApi.geocode(context,
			    "1600 Amphitheatre Parkway Mountain View, CA 94043").await();
			System.out.println(results[0].formattedAddress);
			System.out.println("heheh");
			System.out.println(results[0].geometry.location.lat);
			System.out.println(results[0].geometry.location.lng);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		CourseLibrary courseLibrary = new CourseLibrary(this.getmEm());
		CourseFilterBuilder cb = new CourseFilterBuilder();
		List<Location> locations =  new ArrayList<Location>();
		cb.setCurentLocation(new Location(1,-1,"", -118.495 , 34.030));
		Collection<Course> result = courseLibrary.getCourseByCustomRule(cb, 1, 10);
		assertThat(result.size()).isEqualTo(1);
	}
	
}
