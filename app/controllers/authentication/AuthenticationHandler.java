package controllers.authentication;

import javax.persistence.EntityManager;

import controllers.user.UserHandler;
import models.users.Password;
import models.users.User;
import play.mvc.Controller;
import play.mvc.Http.Context;

public class AuthenticationHandler implements IAuthenticationHandler {

	@Override
	public User doLogin(String email, String password, EntityManager em,
			Context context) {
		User u = new UserHandler(em).getUserByEmail(email);
		if (u != null && Password.check(password, u.getPassword())) {
			context.session().put("connected", u.getEmail());
			return u;
		}
		return null;
	}

	@Override
	public boolean doRegister(String email, String password, EntityManager em,
			Context context) {
		context.session().remove("connected");
		return true;
	}

}
