package models.forms;

public class ResetPswForm {
	
	private String newPassword;
	
	private String repass;
	
	private String token;

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getRepass() {
		return repass;
	}

	public void setRepass(String repass) {
		this.repass = repass;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public ResetPswForm() {
		super();
	}
	
	
	
}
