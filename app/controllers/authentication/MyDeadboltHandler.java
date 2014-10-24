package controllers.authentication;

import controllers.user.UserHandler;
import models.users.Customer;
import play.db.jpa.JPA;
import play.libs.F;
import play.mvc.Http;
import play.mvc.Http.Context;
import play.mvc.Result;
import be.objectify.deadbolt.core.models.Subject;
import be.objectify.deadbolt.java.AbstractDeadboltHandler;

public class MyDeadboltHandler extends AbstractDeadboltHandler {

	@Override
	public F.Promise<Result> beforeAuthCheck(Context arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public F.Promise<Subject> getSubject(final Http.Context context) {
		return F.Promise.promise(new F.Function0<Subject>() {
			@Override
			public Subject apply() throws Throwable {
				String email = context.session().get("connected");
            	if(email != null)
            		return new UserHandler().getUserByEmail(email, JPA.em());
				return null;
			}
		});
	}

}
