package controllers;

import static org.fest.assertions.Assertions.assertThat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import models.courses.ConcreteCourse;
import models.courses.Course;
import models.courses.CourseOrder;
import models.courses.CourseStatus;
import models.filters.ConcreteCourseFilterBuilder;
import models.filters.CourseFilterBuilder;
import models.locations.Location;
import models.users.Customer;

import org.junit.Test;

import play.Logger;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;

import common.BaseTest;
import controllers.course.CourseHandler;
import controllers.course.OrderHandler;
import controllers.user.UserHandler;

public class CourseHandlerTest extends BaseTest {
	@Test
	public void testGetCourseById() {

		CourseHandler courseHandler = new CourseHandler();
		assertThat(initData.course1).isEqualTo(
				courseHandler.getCourseById(initData.course1.getId()));
	}

	@Test
	public void testGetCourseByCategory() {
		CourseHandler courseHandler = new CourseHandler();
		assertThat(
				courseHandler.getCourseByCategory(1, 1, 10, null, true).size())
				.isEqualTo(1);
	}

	@Test
	public void testFilterCourseByKeyword() {
		CourseHandler courseHandler = new CourseHandler();
		CourseFilterBuilder cb = new CourseFilterBuilder();
		cb.setCourseStatus(CourseStatus.OPEN.ordinal());
		cb.setKeyword("xingbuxin");
		Collection<Course> result = courseHandler.getCourseByCustomRule(cb,
				null, true, 1, 10);
		assertThat(result.size()).isEqualTo(1);

	}

	@Test
	public void testFilterCourseByStartDate() {
		CourseHandler courseHandler = new CourseHandler();
		CourseFilterBuilder cb = new CourseFilterBuilder();
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
		String dateInString = "10-01-1949";
		Date date = new Date();
		try {
			date = sdf.parse(dateInString);
		} catch (ParseException e) {
			Logger.error(e.toString());
		}
		cb.setStartDate(date);
		Collection<Course> result = courseHandler.getCourseByCustomRule(cb,
				null, true, 1, 10);
		assertThat(result.size()).isEqualTo(2);
	}

	@Test
	public void testFilterCourseByCourseRating() {
		CourseHandler courseHandler = new CourseHandler();
		CourseFilterBuilder cb = new CourseFilterBuilder();
		cb.setCourseRating(3);
		Collection<Course> result = courseHandler.getCourseByCustomRule(cb,
				null, true, 1, 10);
		assertThat(result.size()).isEqualTo(1);

	}

	@Test
	public void testFilterCourseByTrainerRating() {
		CourseHandler courseHandler = new CourseHandler();
		CourseFilterBuilder cb = new CourseFilterBuilder();
		cb.setTrainerRating(3);
		Collection<Course> result = courseHandler.getCourseByCustomRule(cb,
				null, true, 1, 10);
		assertThat(result.size()).isEqualTo(2);
		cb.setTrainerRating(4);
		result = courseHandler.getCourseByCustomRule(cb, null, true, 1, 10);
		assertThat(result.size()).isEqualTo(0);

	}

	@Test
	public void testFilterCourseByLocation() {
		CourseHandler courseHandler = new CourseHandler();
		CourseFilterBuilder cb = new CourseFilterBuilder();
		List<Location> locations = new ArrayList<Location>();
		locations.add(new Location("qunimabi", null, "", -118.14, 34.03));
		locations.add(new Location("nimabi", "gun", "", -118.14, 34.03));
		cb.setLocations(locations);
		Collection<Course> result = courseHandler.getCourseByCustomRule(cb,
				null, true, 1, 10);
		assertThat(result.size()).isEqualTo(2);
	}

	@Test
	public void testFilterCourseByMultiOption() {
		CourseHandler courseLibrary = new CourseHandler();
		CourseFilterBuilder cb = new CourseFilterBuilder();
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
		String dateInString = "10-01-1949";
		Date date = new Date();
		try {
			date = sdf.parse(dateInString);
		} catch (ParseException e) {
			Logger.error(e.toString());
		}
		cb.setStartDate(date);
		List<Location> locations = new ArrayList<Location>();
		locations.add(new Location("qunimabi", null, "", -118.14, 34.03));
		locations.add(new Location("nimabi", "gun", "", -118.14, 34.03));
		cb.setLocations(locations);
		Collection<Course> result = courseLibrary.getCourseByCustomRule(cb,
				null, true, 1, 10);
		assertThat(result.size()).isEqualTo(2);
	}

	@Test
	public void testGetAllCourse() {
		CourseHandler courseHandler = new CourseHandler();
		CourseFilterBuilder cb = new CourseFilterBuilder();
		Collection<Course> result = courseHandler.getCourseByCustomRule(cb,
				null, true, 1, 10);
		assertThat(result.size()).isEqualTo(2);
	}

	@Test
	public void testSearchNearbyCourse() {
		GeoApiContext context = new GeoApiContext()
				.setApiKey("AIzaSyA8Msu-j7RYqgcmHNzbdtSG4G5m12CFJ_o");
		GeocodingResult[] results;
		try {
			results = GeocodingApi.geocode(context,
					"1600 Amphitheatre Parkway Mountain View, CA 94043")
					.await();
			System.out.println(results[0].formattedAddress);
			System.out.println("heheh");
			System.out.println(results[0].geometry.location.lat);
			System.out.println(results[0].geometry.location.lng);
		} catch (Exception e) {
			Logger.error(e.toString());
		}

		CourseHandler courseHandler = new CourseHandler();
		CourseFilterBuilder cb = new CourseFilterBuilder();
		cb.getLocations().add(new Location("nearby", null, ""));
		cb.setCurentLocation(new Location("qunimaba", null, "", -118.495,
				34.030));
		Collection<Course> result = courseHandler.getCourseByCustomRule(cb,
				null, true, 1, 10);
		assertThat(result.size()).isEqualTo(1);
	}

	@Test
	public void testGetConcreteCourseMap(){
		CourseHandler courseHandler = new CourseHandler();
		CourseFilterBuilder cb = new CourseFilterBuilder();
		cb.setKeyword("xingbuxin");
		Collection<Course> result = courseHandler.getCourseByCustomRule(cb,
				null, true, 1, 10);
		assertThat(result.size()).isEqualTo(1);
		ConcreteCourseFilterBuilder ccfb = new ConcreteCourseFilterBuilder();
		ccfb.setCfb(cb);
		ccfb.setCourseList(result);
		Map<Integer,List<ConcreteCourse>> map = courseHandler.getConcreteCourseMap(ccfb, null, true, -1, -1);
		int count = 0;
		Iterator<Entry<Integer, List<ConcreteCourse>>> it = map.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pairs = (Map.Entry)it.next();
	        count+=((List<ConcreteCourse>)pairs.getValue()).size();
	    }
	    assertThat(count).isEqualTo(2);
	}
	
	@Test
	public void testCourseRegisteration() {
		CourseOrder courseOrder = new CourseHandler().registerCourse(
				this.initData.customer1, this.initData.concreteCourse1, "hehe", new OrderHandler());
		Customer customer = new UserHandler()
				.getCustomerByEmail(this.initData.customer1.getEmail());
		ConcreteCourse concreteCourse = new CourseHandler()
				.getCourseByConcreteCourseId(this.initData.concreteCourse1
						.getConcreteCourseId());
		assertThat(concreteCourse.getSelectedCustomers().contains(customer))
				.isTrue();
		assertThat(customer.getSelectedCourses().contains(concreteCourse))
				.isTrue();
		assertThat(courseOrder.getOrderId()).isEqualTo("hehe");
		this.getmEm().remove(courseOrder);
	}
}
