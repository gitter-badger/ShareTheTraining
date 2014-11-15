package controllers;

import static org.fest.assertions.Assertions.assertThat;

import java.util.List;

import common.BaseTest;

import org.junit.*;

import controllers.locations.LocationHandler;

public class LocationHnaderTest extends BaseTest {
	@Test
	public void testGetStateList() {
		List<String> stateList = LocationHandler.getStateList();
		assertThat(stateList.size()).isEqualTo(51);
	}
}
