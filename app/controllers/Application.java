package controllers;


import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import be.objectify.deadbolt.java.actions.SubjectPresent;
import models.locations.Geolocation;
import models.locations.GeolocationService;
import models.locations.InvalidAddressException;
import models.users.Customer;
import play.*;
import play.data.Form;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.mvc.*;
import play.mvc.Http.Request;
import views.html.*;
import static play.data.Form.form;

public class Application extends Controller {
	public static Form<Customer> signupForm = form(Customer.class);




	@Transactional
	public static Result index() {
		try {
			Geolocation geolocation = GeolocationService
					.getGeolocation(request().remoteAddress());
			if (geolocation == null) { // the service does not responded
				// properly
				Logger.info("no geo");
			}
			else
				Logger.info(geolocation.getCountryCode());
		} catch (InvalidAddressException ex) {

		}
		String message = flash().get("message");
		message= message!=null ? message : "Your new application is ready.";
		Logger.info(index.render(message).toString());
		return ok(index.render(message));
	}
	
	@Transactional
	@Restrict({@Group("CUSTOMER"), @Group("TRAINER")})
	public static Result welcome() {
		return ok(index_new.render());
	}

	public static Result search() {
		return ok(search_results.render());
	}

	public static Result course(boolean confirmed, String orderId) {
		return ok(item_page.render(confirmed, orderId));
	}

}