package models.users;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.OneToMany;

import be.objectify.deadbolt.core.models.Role;
import models.courses.Course;
import models.locations.Location;

@Entity
public class Trainer extends User {

	public static Trainer create(String email, String username,
			String password, EntityManager em) {
		Trainer trainer = new Trainer(email, username, password);
		em.persist(trainer);
		trainer.putSolrDoc();
		return trainer;

	}

	protected Trainer(String email, String username, String password) {
		super(email, username, password);
		this.setUserRole(UserRole.TRAINER);
	}

	public Trainer() {
	}

	private String cellPhone;

	private String phone;

	private String education;

	private String experience;

	private String certification;

	private double rating;

	private int rateCount;

	private boolean isVeteran;
	
	private Veteran veteranRole = Veteran.NONE;

	private Location location = new Location(null,null, "", 0, 0);

	@Override
	public List<? extends Role> getRoles() {
		List<UserRole> list = new ArrayList<UserRole>();
		list.add(this.getUserRole());
		return list;
	}

	@OneToMany(mappedBy = "trainer", cascade = { CascadeType.ALL })
	private Collection<Course> courses = new ArrayList<Course>();

	@ElementCollection
	private Set<Date> availableDates = new HashSet<Date>();

	public Course createNewCourse(String courseName, EntityManager em){
		return Course.create(courseName, this, em);
	}
	
	public void updateRating(double rating) {
		this.rateCount += 1;
		this.rating = (this.rating * this.rateCount) / this.rateCount;
	}

	public Collection<Course> getCourses() {
		return courses;
	}

	public void setCourses(Collection<Course> courses) {
		this.courses = courses;
	}

	public Set<Date> getAvailableDates() {
		return availableDates;
	}

	public void setAvailableDates(Set<Date> availableDates) {
		this.availableDates = availableDates;
	}

	public String getCellPhone() {
		return cellPhone;
	}

	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public String getExperience() {
		return experience;
	}

	public void setExperience(String experience) {
		this.experience = experience;
	}

	public String getCertification() {
		return certification;
	}

	public void setCertification(String certification) {
		this.certification = certification;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public int getRateCount() {
		return rateCount;
	}

	public void setRateCount(int rateCount) {
		this.rateCount = rateCount;
	}

	public boolean isVeteran() {
		return isVeteran;
	}

	public void setVeteran(boolean isVeteran) {
		this.isVeteran = isVeteran;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
}
