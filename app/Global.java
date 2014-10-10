
import play.mvc.*;
import play.mvc.Http.*;
import play.libs.F.*;
import static play.mvc.Results.*;
import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.mvc.Action;
import play.mvc.Http.Request;
import java.lang.reflect.Method;

public class Global extends GlobalSettings {

	public void onStart(Application app) {
		Logger.info("Three tomatoes are walking down the street- a poppa tomato, a momma tomato, and a little baby tomato. ");
	}

	public void onStop(Application app) {
		Logger.info("Baby tomato starts lagging behind. Poppa tomato gets angry, goes over to the baby tomato, and smooshes him... and says, Catch up.");
	}

	/* 500, need modification */
	public Promise<Result> onError(RequestHeader request, Throwable t) {
		return Promise.<Result> pure(internalServerError(views.html.index
				.render(t.toString())));
	}

	/* 404, need modification later */
	public Promise<Result> onHandlerNotFound(RequestHeader request) {
		return Promise.<Result> pure(notFound(views.html.index.render(request
				.uri())));
	}

	/* 400, need modification later */
	public Promise<Result> onBadRequest(RequestHeader request, String error) {
		return Promise.<Result> pure(badRequest("Don't try to hack the URI!"));
	}

	/* intercept request, need modification later */
	public Action onRequest(Request request, Method actionMethod) {
		System.out.println("before each request..." + request.toString());
		return super.onRequest(request, actionMethod);
	}

}
