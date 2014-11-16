package controllers.locations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import play.Logger;
import play.db.DB;
import models.locations.Location;

public class LocationHandler {
	public static List<String> stateList;

	private static Map<String, List<String>> cityMap;

	public static void initialize() {
		stateList = getStateList();
		cityMap = getCityMap();
	}

	public static Location getLocationByZipcode(int zipcode) {
		Connection connection = DB.getConnection();
		String selectSQL = "SELECT * FROM zips WHERE state = ? AND city = ?";
		try {
			PreparedStatement preparedStatement = connection
					.prepareStatement(selectSQL);
			preparedStatement.setInt(1, 1001);
			ResultSet rs = preparedStatement.executeQuery(selectSQL);
			if (rs.next()) {
				String state = rs.getString("state");
				String city = rs.getString("city");
				double lat = rs.getDouble("lat");
				double lng = rs.getDouble("lng");
				return new Location(state, city, null, lng, lat);
			}
		} catch (SQLException e) {
			Logger.info(e.toString());
			return null;
		}
		return null;
	}

	public static List<String> getStateList() {
		Connection connection = DB.getConnection();
		String selectSQL = "SELECT DISTINCT state FROM cities";
		List<String> stateList = new ArrayList<String>();
		try {
			PreparedStatement preparedStatement = connection
					.prepareStatement(selectSQL);
			ResultSet rs = preparedStatement.executeQuery(selectSQL);
			while (rs.next()) {
				String state = rs.getString("state");
				stateList.add(state);
			}
		} catch (SQLException e) {
			Logger.info(e.toString());
		}
		return stateList;
	}

	public static Map<String, List<String>> getCityMap() {
		Connection connection = DB.getConnection();
		String selectSQL = "SELECT * FROM cities";
		Map<String, List<String>> cityMap = new HashMap<String, List<String>>();
		try {
			PreparedStatement preparedStatement = connection
					.prepareStatement(selectSQL);
			ResultSet rs = preparedStatement.executeQuery(selectSQL);
			while (rs.next()) {
				String state = rs.getString("state");
				String city = rs.getString("city");
				List<String> cityList = cityMap.get(state);
				if (cityList == null) {
					cityList = new ArrayList<String>();
					cityMap.put(state, cityList);
				}
				cityList.add(city);
			}
		} catch (SQLException e) {
			Logger.info(e.toString());
		}
		return cityMap;
	}
	
	
	public static List<String> getCitiesByState(String state){
		return cityMap.get(state);
	}
}
