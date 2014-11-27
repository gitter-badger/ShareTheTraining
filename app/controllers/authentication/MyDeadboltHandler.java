package controllers.authentication;

import controllers.routes;
import controllers.user.UserHandler;
import models.users.Guest;
import models.users.Trainer;
import models.users.User;
import models.users.UserRole;
import models.users.UserStatus;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.libs.F;
import play.mvc.Http;
import play.mvc.Http.Context;
import play.mvc.Result;
import be.objectify.deadbolt.core.models.Subject;
import be.objectify.deadbolt.java.AbstractDeadboltHandler;

public class MyDeadboltHandler extends AbstractDeadboltHandler {
	
	@Override
	public F.Promise<Result> beforeAuthCheck(Context arg0) {
		return F.Promise.pure(null);
	}

	@Override
	public F.Promise<Subject> getSubject(final Http.Context context) {
		return F.Promise.promise(new F.Function0<Subject>() {
			@Override
			public Subject apply() throws Throwable {
				String email = context.session().get("connected");
				if (email != null) {
					User u = retrieveUser(email);
					context.args.put("connected", u);
					return u;
				}
				return new Guest();
			}
		});
	}

	@Transactional
	public static User retrieveUser(final String userEmail){
		final User u = null;
		try {
			return JPA.withTransaction(new F.Function0<User>() {
			    @Override
			    public User apply() throws Throwable {
					return new UserHandler().getUserByEmail(userEmail);
			    }
			});
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return u;
	}
	
	@Override
	public F.Promise<Result> onAuthFailure(final Http.Context context,
			String content) {
		// you can return any result from here - forbidden, etc
		return F.Promise.promise(new F.Function0<Result>() {
			@Override
			public Result apply() throws Throwable {
				if(context.args.get("connected")!=null){
					User u = (User) context.args.get("connected");
					if(u.getUserRole() == UserRole.ADMIN)
						return redirect(routes.Application.dashDashboard());
					return redirect(routes.Application.welcome());
				}
				context.flash().put("message", "go sigining up, you dumbass!");
				context.flash().put("redirect", context.request().path());
				return redirect(routes.Application.login());
			}
		});
	}

}
