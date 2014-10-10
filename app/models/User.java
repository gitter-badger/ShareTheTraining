package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import play.data.format.Formats;
import play.data.validation.Constraints;
import common.BaseModelObject;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class User extends BaseModelObject {

	//
	@Constraints.Required
	@Formats.NonEmpty
	@Column(unique = true)
	public String email;

	@Constraints.Required
	@Formats.NonEmpty
	public String username;

	@Constraints.Required
	@Formats.NonEmpty
	public String password;

	protected User(String email, String username, String password) {
		this.email = email;
		this.username = username;
		this.password = password;
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
