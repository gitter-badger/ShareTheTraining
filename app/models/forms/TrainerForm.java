package models.forms;

import java.util.List;

import models.locations.Location;

public class TrainerForm {
	
	private String username;
	
	private String password;
	
	private String email;
	
	private String name;
	
	private Location location;
	
	private String cellPhone;
	
	private String phone;

	private String education;
	
	private String experience;
	
	//certification, image? 
	private List<String>certification;
	
	//how far are you willing to travel to deliver a course without travel reimbursement
	private int howfar;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public String getCellPhone() {
		return cellPhone;
	}

	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
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

	public List getCertification() {
		return certification;
	}

	public void setCertification(List certification) {
		this.certification = certification;
	}

	public int getHowfar() {
		return howfar;
	}

	public void setHowfar(int howfar) {
		this.howfar = howfar;
	}
	
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	public TrainerForm() {
		
	}
	

}
