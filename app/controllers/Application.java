package controllers;

import geolocation.*;

import java.util.Collection;

import models.ConcreteCourse;
import models.Course;
import models.CourseFilterBuilder;
import models.CourseLibrary;
import models.Customer;
import models.User;
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

	public static Result blank() {
		return ok(signup.render(signupForm));
	}

	@Transactional
	public static Result submit() {
		Form<Customer> filledForm = signupForm.bindFromRequest();

		// instantiate User entity
		Customer created = filledForm.get();

		Customer newcus = Customer.create(created.getEmail(),
				created.getUsername(), created.getPassword());

		session("email", newcus.getEmail());

		return redirect(
		// return to home page
		routes.Application.welcome());
	}

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
		return ok(index.render("Your new application is ready."));
	}

	public static Result welcome() {
		return ok(index_new.render());
	}

	public static Result search() {
		return ok(search_results.render());
	}

	public static Result course(boolean confirmed, String orderId) {
		Logger.info(Boolean.toString(confirmed));
		return ok(item_page.render(confirmed, orderId));
	}

}