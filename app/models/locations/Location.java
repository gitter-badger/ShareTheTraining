package models.locations;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.hibernate.annotations.Type;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;

@Embeddable
public class Location {
	private int county = 1;
	private int city = 1;
	private String detailedLoc = "";

	@Type(type = "org.hibernate.spatial.GeometryType")
	private Point point;

	public Location() {
	}

	public Location(int county, int city, String detailedLoc, double longitude,
			double latitude) {
		this.county = county;
		this.city = city;
		this.detailedLoc = detailedLoc;
		GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
		this.point = geometryFactory.createPoint(new Coordinate(longitude,
				latitude));
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

	public Point getPoint() {
		return point;
	}

	public void setPoint(Point point) {
		this.point = point;
	}

}
