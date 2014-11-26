package models.forms;

import models.locations.Location;
import models.users.Admin;
import models.users.Customer;
import models.users.User;
import models.users.UserRole;

public class AdminForm extends UserForm{
	
	private String name;

	private String cellPhone;

	private String phone;

	private String userRole;

	private Location location;
	
	

	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
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



	public String getUserRole() {
		return userRole;
	}



	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}



	public Location getLocation() {
		return location;
	}



	public void setLocation(Location location) {
		this.location = location;
	}



	@Override
	public boolean bindUser(User user) {
		if (user == null || user.getUserRole() != UserRole.CUSTOMER
				|| !user.getEmail().equals(this.getEmail()))
			return false;
		Admin admin = (Admin) user;
		admin.setCellPhone(cellPhone);
		admin.setPhone(phone);
		admin.setLocation(location);
		admin.setImage(image);
		admin.setUsername(username);
		admin.setName(name);
		if (userStatus != null)
			admin.setUserStatus(userStatus);

		return true;
	}
	
	public static AdminForm bindAdminForm(Admin admin) {
		if (admin == null)
			return null;
		AdminForm adminForm = new AdminForm();
		adminForm.setCellPhone(admin.getCellPhone());
		adminForm.setImage(admin.getImage());
		adminForm.setName(admin.getName());
		adminForm.getLocation()
				.setRegion(admin.getLocation().getRegion());
		adminForm.getLocation().setCity(admin.getLocation().getCity());
		adminForm.setUsername(admin.getUsername());
		adminForm.setEmail(admin.getEmail());
		adminForm.setPhone(admin.getPhone());

		return adminForm;

	}
	
}
