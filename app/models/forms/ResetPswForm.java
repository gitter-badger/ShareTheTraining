package models.forms;

import play.data.validation.Constraints;

public class ResetPswForm {
	@Constraints.Required
	private String newPassword;

	@Constraints.Required
	private String repass;

	@Constraints.Required
	private String token;

	public String validate() {

		if (isBlank(newPassword))
			return "Password is required";
		if (isBlank(repass))
			return "Reentering Password is required";
		if (!newPassword.equals(newPassword))
			return "The two inputs should be the same";
		return null;
	}

	private boolean isBlank(String input) {
		return input == null || input.isEmpty() || input.trim().isEmpty();
	}

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
