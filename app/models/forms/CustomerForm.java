package models.forms;

import play.Logger;
import models.locations.Location;
import models.users.Customer;
import models.users.Trainer;
import models.users.User;
import models.users.UserRole;

public class CustomerForm extends UserForm {

	private String name;

	private String cellPhone;

	private String phone;

	private String userRole;

	private Location location;

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

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

	public CustomerForm() {

	}

	@Override
	public boolean bindUser(User user) {
		if (user == null || user.getUserRole() != UserRole.CUSTOMER
				|| !user.getEmail().equals(this.getEmail()))
			return false;
		Customer customer = (Customer) user;
		customer.setCellPhone(cellPhone);
		customer.setPhone(phone);
		customer.setLocation(location);
		customer.setImage(image);
		customer.setUsername(username);
		customer.setName(name);
		if (userStatus != null)
			customer.setUserStatus(userStatus);

		return true;
	}

	// TODO
	public static CustomerForm bindCustomerForm(Customer customer) {
		if (customer == null)
			return null;
		CustomerForm customerForm = new CustomerForm();
		customerForm.setCellPhone(customer.getCellPhone());
		customerForm.setImage(customer.getImage());
		customerForm.setName(customer.getName());
		customerForm.getLocation()
				.setRegion(customer.getLocation().getRegion());
		customerForm.getLocation().setCity(customer.getLocation().getCity());
		customerForm.setUsername(customer.getUsername());

		return customerForm;

	}
}
