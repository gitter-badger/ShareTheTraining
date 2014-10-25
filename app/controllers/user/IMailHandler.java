package controllers.user;

import models.users.UserAction;

public interface IMailHandler {
	public boolean sendMailWithToken(String email, String token, UserAction action);
}
