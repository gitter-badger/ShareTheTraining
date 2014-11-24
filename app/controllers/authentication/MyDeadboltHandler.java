package controllers.authentication;

import controllers.routes;
import controllers.user.UserHandler;
import models.users.User;
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
				//TODO move this part to login method
				context.flash().put(
						"url",
						"GET".equals(context.request().method()) ? context
								.request().uri() : routes.Application.welcome().url());
				String email = context.session().get("connected");
				if (email != null) {
					User u = new UserHandler().getUserByEmail(email);
					return u;
				}
				return null;
			}
		});
	}

	@Override
	public F.Promise<Result> onAuthFailure(final Http.Context context,
			String content) {
		// you can return any result from here - forbidden, etc
		return F.Promise.promise(new F.Function0<Result>() {
			@Override
			public Result apply() throws Throwable {
				context.flash().put("message", "go sigining up, you dumbass!");
				return redirect(routes.Application.test());
			}
		});
	}

}
