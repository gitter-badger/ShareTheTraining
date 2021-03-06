package controllers.authentication;

import controllers.user.IMailHandler;
import controllers.user.IUserHandler;
import play.mvc.Http.Context;
import models.forms.UserForm;
import models.users.User;
import models.users.UserAction;
import models.users.UserRole;

public interface IAuthenticationHandler {
	public User doLogin(String userEmail, String password, Context context,
			IUserHandler userHandler);

	public String doRegister(String userEmail, String userName,
			String password, UserRole userRole,UserForm userform, IUserHandler userHandler,
			IMailHandler mailHandler);

	public boolean doLogout(Context context);

	boolean authorizeResetPassword(String userName, String userEmail,
			IMailHandler mailHandler);

	boolean doResetPassword(String token, String newPassword,
			IUserHandler userHandler);

	boolean activateUser(String token, IUserHandler userHandler);
}
