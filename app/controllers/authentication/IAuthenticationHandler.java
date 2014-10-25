package controllers.authentication;

import controllers.user.IMailHandler;
import controllers.user.IUserHandler;
import play.mvc.Http.Context;
import models.users.User;
import models.users.UserRole;

public interface IAuthenticationHandler {
	public User doLogin(String userEmail, String password, UserRole userRole,
			Context context, IUserHandler userHandler);

	public boolean doRegister(String userEmail, String userName, String password,
			UserRole userRole, IUserHandler userHandler, Context context,
			IMailHandler mailHandler);

	public boolean doLogout(Context context);

	boolean authorizeResetPassword(String userEmail, IMailHandler mailHandler);

	boolean doResetPassword(String token, String newPassword,
			IUserHandler userHandler);

	boolean activeUser(String token, IUserHandler userHandler);
}
