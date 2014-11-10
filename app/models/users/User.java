package models.users;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import play.Logger;
import be.objectify.deadbolt.core.models.Permission;
import be.objectify.deadbolt.core.models.Role;
import be.objectify.deadbolt.core.models.Subject;
import common.BaseModelObject;
import common.Password;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public  abstract class User extends BaseModelObject implements Subject{

	@Column(unique = true)
	private String email;

	private String username;
	
	private String password;
	
	private UserRole userRole;
	
	private UserStatus userStatus;

	protected User(String email, String username, String password) {
		this.email = email;
		this.username = username;
		this.setUserStatus(UserStatus.INACTIVE);
		try {
			this.password = Password.getSaltedHash(password);
		} catch (Exception e) {
			Logger.error(e.toString());
		}
	}
	
	public User(){
		
	}
	
	@Override
	public String getIdentifier() {
		return email;
	}

	@Override
	public List<? extends Permission> getPermissions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<? extends Role> getRoles() {
		// TODO Auto-generated method stub
		return null;
	}


	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

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
		try {
			this.password = Password.getSaltedHash(password);
		} catch (Exception e) {
			Logger.error(e.toString());
		};
	}

	public UserRole getUserRole() {
		return userRole;
	}

	public void setUserRole(UserRole userRole) {
		this.userRole = userRole;
	}

	public UserStatus getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(UserStatus userStatus) {
		this.userStatus = userStatus;
	}


}
