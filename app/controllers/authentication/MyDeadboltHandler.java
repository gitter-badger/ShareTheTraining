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
					User u = new UserHandler().getUserByEmail(email);
					context.args.put("connected", u);
					context.flash().put("role", Integer.toString(u.getUserRole().ordinal()));
					return u;
				}
				return new Guest();
			}
		});
	}

	
	@Override
	public F.Promise<Result> onAuthFailure(final Http.Context context,
			String content) {
		return F.Promise.promise(new F.Function0<Result>() {
			@Override
			public Result apply() throws Throwable {
				if(context.session().get("connected")!=null)
					return redirect(routes.Application.welcome());
				context.flash().put("message", "go sigining up, you dumbass!");
				context.flash().put("redirect", context.request().path());
				return redirect(routes.Application.login());
			}
		});
	}

}
