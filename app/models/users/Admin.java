package models.users;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityManager;

import models.locations.Location;
import be.objectify.deadbolt.core.models.Role;

@Entity
public class Admin extends User {
	
	private String name;

	private String cellPhone;

	private String phone;

	private Location location= new Location(null, null, "", 0, 0);
	
	private boolean isSuper;
	
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

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public static Admin create(String email, String username, String password, EntityManager em) {
		Admin admin = new Admin(email, username, password);
		em.persist(admin);
		admin.putSolrDoc();
		return admin;

	}

	protected Admin(String email, String username, String password) {
		super(email, username, password);
		this.setUserRole(UserRole.ADMIN);
	}
	
	public Admin(){}
	
	@Override
	public List<? extends Role> getRoles() {
		List<UserRole> list = new ArrayList<UserRole>();
		list.add(this.getUserRole());
		return list;
	}

	public boolean isSuper() {
		return isSuper;
	}

	public void setSuper(boolean isSuper) {
		this.isSuper = isSuper;
	}

}
