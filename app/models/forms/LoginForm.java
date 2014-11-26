package models.forms;

import play.data.validation.Constraints;

public class LoginForm {
	
	
	private String username;
	
	@Constraints.Required
	private String email;
	
	@Constraints.Required
	private String password;
	
	private String redirect;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public LoginForm() {
		
	}

	public String getRedirect() {
		return redirect;
	}

	public void setRedirect(String redirect) {
		this.redirect = redirect;
	}
	 
	
	
}
