import play.mvc.*;
import play.mvc.Http.*;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.libs.F;
import play.libs.F.*;
import static play.mvc.Results.*;
import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.mvc.Action;
import play.mvc.Http.Request;
import play.mvc.Result;

import java.lang.reflect.Method;

import models.locations.Geolocation;
import models.users.Trainer;
import models.users.UserStatus;
import controllers.routes;
import controllers.locations.GeolocationService;
import controllers.locations.LocationHandler;
@Transactional
public class Global extends GlobalSettings {

	public void onStart(Application app) {
		// Logger.info("Three tomatoes are walking down the street- a poppa tomato, a momma tomato, and a little baby tomato. ");
		// Logger.info("Two elderly women are at a Catskill mountain resort, and one of 'em says, \"Boy, the food at this place is really terrible.\"");
		Logger.info("Play it, Sam. Play \"As Time Goes By.\"");
		LocationHandler.initialize();
		
	}
	

	
	public void onStop(Application app) {
		// Logger.info("Baby tomato starts lagging behind. Poppa tomato gets angry, goes over to the baby tomato, and smooshes him... and says, Catch up.");
		// Logger.info("The other one says, \"Yeah, I know; and such small portions.\"");
		Logger.info("Play it once, Sam. For old times' sake.");
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

		return new Action.Simple() {
			public Promise<Result> call(Context ctx) throws Throwable {

				/* your code here */
				Logger.info("before each request..." + ctx.request().toString());
				// Logger.info("Naw man. I'm pretty fuckin' far from okay.");
				Logger.info("You met me at a very strange time in my life.");
				if (ctx.session().get("geo") == null) {
					Geolocation geolocation = GeolocationService
							.getGeolocation(ctx.request().remoteAddress());
					if (geolocation != null
							&& !geolocation.getRegionName().equals("")
							&& !geolocation.getCity().equals("")
							&& !(geolocation.getLatitude() == 0 && geolocation
									.getLongitude() == 0)) {
						ctx.session().put("geo", "true");
						ctx.session().put("state", geolocation.getRegionName());
						ctx.session().put("city", geolocation.getCity());
						ctx.session().put("lat",
								Double.toString(geolocation.getLatitude()));
						ctx.session().put("lng",
								Double.toString(geolocation.getLongitude()));
					} else {
						ctx.session().put("geo", "false");
					}
				}
				return delegate.call(ctx);
			}
		};
	}

}
