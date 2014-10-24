package controllers.authentication;

import javax.persistence.EntityManager;

import play.mvc.Http.Context;
import models.users.User;

public interface IAuthenticationHandler {
	public User doLogin(String email, String password, EntityManager em, Context context);
	
	public boolean doRegister(String email, String password, EntityManager em, Context context);
}
