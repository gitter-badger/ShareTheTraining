package controllers;

import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.contentAsString;

import java.util.List;
import java.util.Map;

import common.BaseTest;
import models.locations.Geolocation;

import org.junit.*;

import controllers.locations.GeolocationService;
import controllers.locations.LocationHandler;

public class LocationHandlerTest extends BaseTest {
	@Test
	public void testGetStateList() {
		List<String> stateList = LocationHandler.getStateList();
		assertThat(stateList.size()).isEqualTo(51);
	}

	@Test
	public void testGeolocationService() {
		Geolocation geolocation = GeolocationService
				.getGeolocation("68.181.54.30");
		assertThat(geolocation.getCity()).isEqualTo("Los Angeles");
	}
	
	@Test
	public void testGetCityMap() {
		Map<String, List<String>> cityMap = LocationHandler.getCityMap();
		int count = 0;
		for(String key : cityMap.keySet()){
			count+=cityMap.get(key).size();
		}
		assertThat(count).isEqualTo(25818);
	}
}
