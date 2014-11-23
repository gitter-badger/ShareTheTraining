package controllers.locations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.sql.DataSource;

import play.Logger;
import play.db.DB;
import models.courses.ConcreteCourse;
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
		String selectSQL = "SELECT * FROM zips WHERE zip = ?";
		try {
			PreparedStatement preparedStatement = connection
					.prepareStatement(selectSQL);
			preparedStatement.setInt(1, zipcode);
			ResultSet rs = preparedStatement.executeQuery(selectSQL);
			if (rs.next()) {
				String state = rs.getString("state");
				String city = rs.getString("city");
				double lat = rs.getDouble("lat");
				double lng = rs.getDouble("lng");
				Location location = new Location(state, city, null, lng, lat);
				location.setZipCode(zipcode);
				return location;
			}
		} catch (SQLException e) {
			Logger.info(e.toString());
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
	
	public static Collection<String> getAvailableState(EntityManager em){
		String hql = " select distinct c.location.region from ConcreteCourse c";
		Query query = em.createQuery(hql);
		Collection result = query.getResultList();
		return result;
	}
	
	public static Collection<String> getAvailableCity(String state, EntityManager em){
		String hql = " select distinct c.location.city from ConcreteCourse c where c.location.region= :state";
		Query query = em.createQuery(hql).setParameter("state",
				state);
		Collection result = query.getResultList();
		return result;
	}
	
}
