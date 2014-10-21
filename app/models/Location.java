package models;

import javax.persistence.Embeddable;

@Embeddable
public class Location {
	private int county = 1;
	private int city = 1;
	private String detailedLoc = "";

	public Location() {
	}
	
	public Location(int county, int city, String detailedLoc) {
		this.county = county;
		this.city = city;
		this.detailedLoc = detailedLoc;
	}

	public int getCounty() {
		return county;
	}

	public void setCounty(int county) {
		this.county = county;
	}

	public int getCity() {
		return city;
	}

	public void setCity(int city) {
		this.city = city;
	}

	public String getDetailedLoc() {
		return detailedLoc;
	}

	public void setDetailedLoc(String detailedLoc) {
		this.detailedLoc = detailedLoc;
	}

}
