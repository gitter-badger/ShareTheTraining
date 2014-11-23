import play.mvc.*;
import play.mvc.Http.*;
import play.db.jpa.JPA;
import play.libs.F.*;
import static play.mvc.Results.*;
import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.mvc.Action;
import play.mvc.Http.Request;

import java.lang.reflect.Method;

import controllers.routes;
import controllers.locations.LocationHandler;

public class Global extends GlobalSettings {

	public void onStart(Application app) {
		// Logger.info("Three tomatoes are walking down the street- a poppa tomato, a momma tomato, and a little baby tomato. ");
		Logger.info("Two elderly women are at a Catskill mountain resort, and one of 'em says, \"Boy, the food at this place is really terrible.\"");
		LocationHandler.initialize();
	}

	public void onStop(Application app) {
		// Logger.info("Baby tomato starts lagging behind. Poppa tomato gets angry, goes over to the baby tomato, and smooshes him... and says, Catch up.");
		Logger.info("The other one says, \"Yeah, I know; and such small portions.\"");
	}

	/* 500, need modification */
	public Promise<Result> onError(RequestHeader request, Throwable t) {
		return Promise.<Result> pure(internalServerError(views.html.test
				.render(t.toString())));
	}

	/* 404, need modification later */
	public Promise<Result> onHandlerNotFound(RequestHeader request) {
		return Promise.<Result> pure(notFound(views.html.test.render(request
				.uri())));
	}

	/* 400, need modification later */
	public Promise<Result> onBadRequest(RequestHeader request, String error) {
		return Promise.<Result> pure(badRequest("Don't try to hack the URI!"));
	}

	/* intercept request, need modification later */
	public Action onRequest(Request request, Method actionMethod) {
		Logger.info("before each request..." + request.toString());
		//Logger.info("Naw man. I'm pretty fuckin' far from okay.");
		Logger.info("You met me at a very strange time in my life.");
		return super.onRequest(request, actionMethod);
	}

}
