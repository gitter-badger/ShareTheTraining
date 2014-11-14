package models.forms;

import java.util.List;

import models.locations.Location;
import models.users.Trainer;
import models.users.User;
import models.users.UserRole;

public class TrainerForm extends UserForm {
	private String name;

	private Location location;

	private String cellPhone;

	private String phone;

	private String education;

	private String experience;

	// certification, image?
	private List<String> certification;

	// how far are you willing to travel to deliver a course without travel
	// reimbursement
	private int howfar;

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

	public List<String> getCertification() {
		return certification;
	}

	public void setCertification(List<String> certification) {
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

	@Override
	public boolean bindUser(User user) {
		if (user == null || user.getUserRole() != UserRole.CUSTOMER
				|| user.getEmail() != this.getEmail())
			return false;
		Trainer trainer = (Trainer) user;
		trainer.setCellPhone(cellPhone);
		trainer.setPhone(phone);
		return true;
		
	}

}
