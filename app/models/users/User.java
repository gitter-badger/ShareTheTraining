package models.users;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import be.objectify.deadbolt.core.models.Permission;
import be.objectify.deadbolt.core.models.Role;
import be.objectify.deadbolt.core.models.Subject;
import play.data.format.Formats;
import play.data.validation.Constraints;
import common.BaseModelObject;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public  abstract class User extends BaseModelObject implements Subject{

	//
	@Constraints.Required
	@Formats.NonEmpty
	@Column(unique = true)
	private String email;

	@Constraints.Required
	@Formats.NonEmpty
	private String username;

	@Constraints.Required
	@Formats.NonEmpty
	private String password;

	protected User(String email, String username, String password) {
		this.email = email;
		this.username = username;
		try {
			this.password = Password.getSaltedHash(password);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
		this.password = password;
	}


}
