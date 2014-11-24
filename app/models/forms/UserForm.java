package models.forms;

import models.users.User;
import models.users.UserStatus;

public abstract class UserForm {
	protected String username;

	protected String password;

	protected String email;
	
	protected UserStatus userStatus;
	
	protected String image;
	
	

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public UserStatus getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(UserStatus userStatus) {
		this.userStatus = userStatus;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	abstract public boolean bindUser(User user) ;

}
