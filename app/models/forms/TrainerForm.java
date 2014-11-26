package models.forms;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import play.Logger;
import models.courses.Course;
import models.locations.Location;
import models.users.Trainer;
import models.users.User;
import models.users.UserRole;
import models.users.Veteran;

public class TrainerForm extends UserForm {
	private int id;
	
	private String name;

	private Location location;

	private String cellPhone;

	private String phone;

	private String education;

	private String experience;

	// certification, image?
	private String certification;

	// how far are you willing to travel to deliver a course without travel
	// reimbursement
	private String howfar;
	
	private String company;
	
	private String email;
	
	private String companyInfo;
	
	private String userName;
	
	
	private boolean isVeteran;

	@JsonFormat(shape= JsonFormat.Shape.NUMBER_INT)
	private Veteran veteranRole = Veteran.NONE;
	
	
	
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public boolean isVeteran() {
		return isVeteran;
	}

	public void setVeteran(boolean isVeteran) {
		this.isVeteran = isVeteran;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
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


	public String getCertification() {
		return certification;
	}

	public void setCertification(String certification) {
		this.certification = certification;
	}

	public String getHowfar() {
		return howfar;
	}

	public void setHowfar(String howfar) {
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
	

	public Veteran getVeteranRole() {
		return veteranRole;
	}

	public void setVeteranRole(Veteran veteranRole) {
		this.veteranRole = veteranRole;
	}

	
	public String getCompanyInfo() {
		return companyInfo;
	}

	public void setCompanyInfo(String companyInfo) {
		this.companyInfo = companyInfo;
	}
	


	

	@Override
	public boolean bindUser(User user) {
		if (user == null || user.getUserRole() != UserRole.TRAINER
				|| !user.getEmail().equals(this.getEmail()))
			return false;
		Trainer trainer = (Trainer) user;
		trainer.setCellPhone(cellPhone);
		trainer.setPhone(phone);
		trainer.setEducation(education);
		trainer.setCompany(company);
		trainer.setExperience(experience);
		trainer.setHowFar(howfar);
		trainer.setCertification(certification);
		trainer.setLocation(location);
		trainer.setName(name);
		trainer.setUsername(userName);
		trainer.setCompanyInfo(companyInfo);
		
	
	
		return true;
		
	}
	
	public static TrainerForm bindTraienrForm(
			Trainer trainer) {
		if (trainer == null)
			return null;
		
		TrainerForm trainerForm = new TrainerForm();
		trainerForm.setCellPhone(trainer.getCellPhone());
		trainerForm.setCertification(trainer.getCertification());
		trainerForm.setCompany(trainer.getCompany());
		trainerForm.setEducation(trainer.getEducation());
		trainerForm.setExperience(trainer.getExperience());
		trainerForm.setHowfar(trainer.getHowFar());
		trainerForm.setCompanyInfo(trainer.getCompanyInfo());
		trainerForm.setEmail(trainer.getEmail());
		trainerForm.setName(trainer.getName());
		trainerForm.setId(trainer.getId());
		trainerForm.setImage(trainer.getImage());
		trainerForm.getLocation().setRegion(trainer.getLocation().getRegion());
		trainerForm.getLocation().setCity(trainer.getLocation().getCity());
		trainerForm.setUserName(trainer.getUsername());
		trainerForm.setUserStatus(trainer.getUserStatus());
		trainerForm.setVeteranRole(trainer.getVeteranRole());
		
		return trainerForm;

	}
	

}
