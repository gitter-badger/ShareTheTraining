package controllers;

import java.util.Collection;
import java.util.List;

import com.typesafe.plugin.MailerAPI;
import com.typesafe.plugin.MailerPlugin;

import controllers.course.CourseHandler;
import controllers.user.UserHandler;
import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import be.objectify.deadbolt.java.actions.SubjectPresent;
import models.courses.Course;
import models.filters.CourseFilterBuilder;
import models.filters.FilterBuilder;
import models.forms.CourseFilterForm;
import models.forms.CustomerForm;
import models.locations.Geolocation;
import models.locations.GeolocationService;
import models.locations.InvalidAddressException;
import models.users.Customer;
import play.*;
import play.api.mvc.Cookie;
import play.data.Form;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.libs.F.Function;
import play.libs.F.Function0;
import play.libs.F.Promise;
import play.mvc.*;
import play.mvc.Http.Request;
import play.mvc.Http.Response;
import views.html.*;
import views.html.searchview.*;
import views.html.signup.*;
import views.html.customerprofile.*;
import views.html.login.*;
import static play.data.Form.form;

public class Application extends Controller {
	public static Form<Customer> signupForm = form(Customer.class);

	@Transactional
	public static Promise<Result> test() {
		Promise<Geolocation> promiseOfGeolocation = Promise
				.promise(new Function0<Geolocation>() {
					public Geolocation apply() {
						return GeolocationService
								.getGeolocation("68.191.236.135");
					}
				});
		return promiseOfGeolocation.map(new Function<Geolocation, Result>() {
			public Result apply(Geolocation geolocation) {
				if (geolocation == null) { // the service does not responded
					// properly
					Logger.info("no geo");
				} else
					Logger.info(geolocation.getCity());
				String message = flash().get("message");
				message = message != null ? message
						: "Your new application is ready.";
				return ok(test.render(message));
			}
		});
	}

	@Transactional
	@Restrict({ @Group("CUSTOMER"), @Group("TRAINER") })
	public static Result testAuth() {
		CourseHandler ch = new CourseHandler();
		CourseFilterBuilder cfb = new CourseFilterBuilder();
		Collection<Course> course = ch.getCourseByCustomRule(cfb, "popularity",
				2, 2);
		Logger.info("course" + course.size());
		return ok(home.render(course));

	}
	
	@Transactional
	public static Result welcome() {
		CourseHandler ch = new CourseHandler();
		CourseFilterBuilder cfb = new CourseFilterBuilder();
		Collection<Course> course = ch.getCourseByCustomRule(cfb, "popularity",
				2, 2);
		Logger.info("course" + course.size());
		return ok(home.render(course));

	}

	@Transactional
	public static Result search() {
		Form<CourseFilterForm> filterForm = form(CourseFilterForm.class)
				.bindFromRequest();
		Logger.info("category" + filterForm.get().getCfb().getCategory());
		CourseHandler ch = new CourseHandler();
		Collection<Course> course = ch.getCourseByCustomRule(filterForm.get()
				.getCfb(), null, filterForm.get().getPageNumber(), filterForm
				.get().getPageSize());
		Logger.info("course" + course.size());
		return ok(searchindex.render(course));
	}

	public static Result signupcus() {
		return ok(customersignup.render());
	}

	public static Result signuptrainer() {
		return ok(trainersignup.render());
	}

	public static Result cusprofile() {
		return ok(cuscoursehistory.render());
	}

	public static Result cuscourseconfirmed() {
		return ok(cuscoursehistory.render());
	}

	public static Result cuscourseordered() {
		return ok(cuscoursehistory.render());
	}

	public static Result cuscoursedone() {
		return ok(cuscoursehistory.render());
	}

	public static Result cuscoursecanceled() {
		return ok(cuscoursehistory.render());
	}

	public static Result cusinfo() {
		return ok(cusinfo.render());
	}

	public static Result cusinfoedit() {
		return ok(cusinfoedit.render());
	}

	public static Result cuschangepsw() {
		return ok(cuschangepsw.render());
	}

	public static Result forgetpsw() {
		return ok(forgetpsw.render());
	}

	public static Result forgetpswsubmit() {
		return ok(forgetpswconfirm.render());
	}

	public static Result signupcussubmit() {
		Form<CustomerForm> cusForm = form(CustomerForm.class).bindFromRequest();
		UserHandler uh = new UserHandler();
		// uh.createNewUser(userEmail, userName, password, userRole);

		return TODO;
	}

}