package controllers.user;

import controllers.authentication.IAuthenticationHandler;
import models.users.UserAction;

public interface IMailHandler {
	public boolean sendMailWithToken(String userName, String email, String token,
			UserAction action, IAuthenticationHandler authenticationHandler);
}
