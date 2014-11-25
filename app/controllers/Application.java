package controllers;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.text.*;

import common.Password;
import common.Utility;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import controllers.authentication.AuthenticationHandler;
import controllers.course.CourseHandler;
import controllers.course.OrderHandler;
import controllers.course.ReviewHandler;
import controllers.image.ImageHandler;
import controllers.locations.GeolocationService;
import controllers.locations.LocationHandler;
import controllers.user.IMailHandler;
import controllers.user.IUserHandler;
import controllers.user.MailHandler;
import controllers.user.UserHandler;
import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import models.courses.ConcreteCourse;
import models.courses.Course;
import models.courses.CourseOrder;
import models.courses.CourseStatus;
import models.courses.OrderStatus;
import models.courses.Review;
import models.filters.CourseFilterBuilder;
import models.filters.FilterBuilder;
import models.filters.OrderFilterBuilder;
import models.filters.UserFilterBuilder;
import models.forms.ConcreteCourseForm;
import models.forms.CourseFilterForm;
import models.forms.CourseForm;
import models.forms.CustomerForm;
import models.forms.LoginForm;
import models.forms.NewPswForm;
import models.forms.ResetPswForm;
import models.forms.ReviewForm;
import models.forms.TrainerForm;
import models.forms.UserForm;
import models.locations.Geolocation;
import models.locations.Location;
import models.users.Customer;
import models.users.Trainer;
import models.users.User;
import models.users.UserAction;
import models.users.UserRole;
import play.*;
import play.data.Form;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.libs.F.Function;
import play.libs.F.Function0;
import play.libs.F.Promise;
import play.libs.Json;
import play.libs.ws.WS;
import play.mvc.*;
import play.mvc.Http.Context;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Http.Request;
import play.mvc.Http.Response;
import views.html.*;
import views.html.searchview.*;
import views.html.signup.*;
import views.html.customerprofile.*;
import views.html.login.*;
import views.html.itempage.*;
import views.html.trainerprofile.*;
import views.html.dashboard.*;
import static play.data.Form.form;

public class Application extends Controller {
	public static Form<Customer> signupForm = form(Customer.class);

	@Transactional
	public static Promise<Result> test() {
		Promise<Geolocation> promiseOfGeolocation = Promise
				.promise(new Function0<Geolocation>() {
					public Geolocation apply() {
						return GeolocationService
								.getGeolocation("68.181.54.30");
					}
				});
		return promiseOfGeolocation.map(new Function<Geolocation, Result>() {
			public Result apply(Geolocation geolocation) {
				if (geolocation == null) { // the service does not responded
					// properly
					Logger.info("no geo");
				} else
					Logger.info(geolocation.getRegionName());
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
				true, 2, 2);
		Logger.info("course" + course.size());
		LocationHandler lh = new LocationHandler();
		Collection<String> states = LocationHandler.getAvailableState(JPA.em());

		return ok(home.render(course, states));

	}

	@Transactional
	public static Result welcome() {
		CourseHandler ch = new CourseHandler();
		CourseFilterBuilder cfb = new CourseFilterBuilder();
		Collection<Course> course = ch.getCourseByCustomRule(cfb, null, true,
				1, 10);
		Logger.info("course" + course.size());
		LocationHandler lh = new LocationHandler();
		Collection<String> states = LocationHandler.getAvailableState(JPA.em());

		return ok(home.render(course, states));

	}

	public static Result login() {
		return ok(login.render());
	}

	@Transactional
	public static Result loginAuthen() {
		Form<LoginForm> loginForm = form(LoginForm.class).bindFromRequest();
		AuthenticationHandler ah = new AuthenticationHandler();
		IUserHandler userHandler = new UserHandler();
		User u = ah.doLogin(loginForm.get().getEmail(), loginForm.get()
				.getPassword(), Context.current(), userHandler);
		if (u != null) {

			Logger.info(u.getEmail());
			return redirect(routes.Application.welcome());
		}
		flash("error", "Username or Password is incorrect");

		return ok(login.render());
	}

	@Transactional
	public static Result activate(String token) {
		AuthenticationHandler ah = new AuthenticationHandler();
		IUserHandler uh = new UserHandler();
		ah.activateUser(token, uh);
		return ok(activatesuccess.render());
	}

	@Transactional
	public static Result resetpsw() {
		Form<ResetPswForm> resetPswForm = form(ResetPswForm.class)
				.bindFromRequest();
		AuthenticationHandler ah = new AuthenticationHandler();
		IUserHandler uh = new UserHandler();
		ah.doResetPassword(resetPswForm.get().getToken(), resetPswForm.get()
				.getNewPassword(), uh);
		return ok(resetpswconfirm.render());
	}

	// TODO show password form here
	@Transactional
	public static Result authorizeResetPassword(String token) {
		return ok(auth_password.render(token));
	}

	@Transactional
	public static Result forgetpsw() {
		return ok(forgetpsw.render());
	}

	@Transactional
	public static Result forgetpswsubmit() {
		Form<LoginForm> loginForm = form(LoginForm.class).bindFromRequest();
		UserHandler uh = new UserHandler();
		User u = uh.getUserByEmail(loginForm.get().getEmail());
		if (u != null) {
			AuthenticationHandler ah = new AuthenticationHandler();
			IMailHandler mh = new MailHandler();
			ah.authorizeResetPassword(loginForm.get().getUsername(), loginForm
					.get().getEmail(), mh);
			return ok(forgetpswconfirm.render());
		}
		flash("error", "email doesn't exist!!!");
		return ok(forgetpsw.render());

	}

	@Transactional
	public static Result logout() {
		AuthenticationHandler ah = new AuthenticationHandler();
		ah.doLogout(Context.current());
		return redirect(routes.Application.welcome());
	}

	@Transactional
	public static Result search(Integer pageNumber) {
		Logger.info(Utility.getQueryString(request().uri()));
		CourseFilterForm filterForm = form(CourseFilterForm.class)
				.bindFromRequest().get();
		CourseHandler ch = new CourseHandler();
		filterForm.transferChoiceToRange();
		Collection<Course> course = ch.getCourseByCustomRule(filterForm, null,
				true, pageNumber < 1 ? 1 : pageNumber, Play.application()
						.configuration().getInt("page.size.search"));
		Logger.info("course" + course.size());
		LocationHandler lh = new LocationHandler();
		// TODO cache
		Collection<String> states = LocationHandler.getAvailableState(JPA.em());
		Logger.info(pageNumber + "hahah");
		return ok(searchindex.render(course, states, pageNumber + 1,
				Utility.getQueryString(request().uri())));
	}

	@Transactional
	public static Result searchall() {

		CourseHandler ch = new CourseHandler();
		Collection<Course> course = ch.getCourseByCustomRule(
				new CourseFilterBuilder(), null, true, 1, 10);
		Logger.info("course" + course.size());

		LocationHandler lh = new LocationHandler();
		Collection<String> states = LocationHandler.getAvailableState(JPA.em());

		return ok(searchindex.render(course, states, 1, ""));
	}

	@Transactional
	// TODO test
	public static Result searchloc(String city, String region) {
		CourseFilterBuilder filter = new CourseFilterBuilder();
		Location loc = new Location();
		loc.setRegion(region);
		loc.setCity(city);
		ArrayList<Location> locationList = new ArrayList<Location>();
		locationList.add(loc);
		filter.setLocations(locationList);
		CourseHandler ch = new CourseHandler();
		Collection<Course> course = ch.getCourseByCustomRule(filter, null,
				true, 1, 10);
		LocationHandler lh = new LocationHandler();
		Collection<String> states = LocationHandler.getAvailableState(JPA.em());

		return ok(searchindex.render(course, states, 1, ""));
	}

	@Transactional
	public static Result signupcussubmit() {
		Form<CustomerForm> cusForm = form(CustomerForm.class).bindFromRequest();
		AuthenticationHandler ah = new AuthenticationHandler();
		IUserHandler uh = new UserHandler();
		IMailHandler mh = new MailHandler();
		ah.doRegister(cusForm.get().getEmail(), cusForm.get().getName(),
				cusForm.get().getPassword(), UserRole.values()[1],
				cusForm.get(), uh, mh);

		return ok(signupemail.render());
	}

	@Transactional
	@Restrict({ @Group("!CUSTOMER"), @Group("!TRAINER") })
	public static Result signup(int userRole) {
		Logger.info(session().get("connected"));
		List<String> stateList = LocationHandler.getStateList();
		switch (UserRole.fromInteger(userRole)) {
		case CUSTOMER:
			return ok(customersignup.render(stateList));
		case TRAINER:
			return ok(trainersignup.render(stateList));
		default:
			break;
		}
		return notFound();
	}

	@Transactional
	public static Result signupSubmit(int userRole) {
		UserForm userForm = null;
		switch (UserRole.fromInteger(userRole)) {
		case CUSTOMER:
			userForm = form(CustomerForm.class).bindFromRequest().get();
			break;
		case TRAINER:
			userForm = form(TrainerForm.class).bindFromRequest().get();
			break;
		default:
			return notFound();
		}
		AuthenticationHandler ah = new AuthenticationHandler();
		IUserHandler uh = new UserHandler();
		IMailHandler mh = new MailHandler();
		ah.doRegister(userForm.getEmail(), userForm.getUsername(),
				userForm.getPassword(), UserRole.fromInteger(userRole),
				userForm, uh, mh);
		return ok(signupemail.render());
	}

	@Transactional
	@Restrict({ @Group("CUSTOMER"), @Group("TRAINER") })
	public static Result profile() {
		UserHandler uh = new UserHandler();
		User user = uh.getUserByEmail(session().get("connected"));
		if (user.getUserRole().ordinal() == 1) {
			OrderHandler oh = new OrderHandler();
			Collection<CourseOrder> order = oh
					.getCourseOrderByCustomer(session().get("connected"));
			System.out.print(order.size());
			return ok(cuscoursehistory.render(order));
		}
		if (user.getUserRole().ordinal() == 2) {
			CourseHandler ch = new CourseHandler();
			Collection<Course> course = ch.getCourseByTrainer(
					session().get("connected"), 1, 10, null, true);
			System.out.print(course.size());
			return ok(trainercoursehistory.render(course));
		}
		return internalServerError();
	}

	@Transactional
	@Restrict({ @Group("CUSTOMER") })
	public static Result createOrder(String orderId, String eventbriteId) {
		Logger.info(orderId);
		Logger.info(eventbriteId);
		if (session().get("connected") == null) {
			flash("error", "your order is failed because you log out");
		}
		CourseHandler ch = new CourseHandler();
		ch.registerCourse(new UserHandler().getCustomerByEmail(session().get(
				"connected")), ch.getCourseByEventbriteId(eventbriteId),
				orderId, new OrderHandler());
		return redirect(routes.Application.profile());
	}

	@Transactional
	@Restrict({ @Group("TRAINER") })
	public static Result trainerschedule() {
		return ok(trainerschedule.render());
	}

	@Transactional
	@Restrict({ @Group("CUSTOMER") })
	public static Result customerCourseHistory(int status) {
		OrderStatus orderStatus = OrderStatus.fromInteger(status);
		if (orderStatus != null) {
			OrderHandler oh = new OrderHandler();
			OrderFilterBuilder fb = new OrderFilterBuilder();
			fb.setOrderStatus(orderStatus.ordinal());
			fb.setUserEmail(session().get("connected"));
			Collection<CourseOrder> order = oh.getCourseOrderByCustomRule(fb,
					null, 1, 10);
			return ok(cuscoursehistory.render(order));
		}
		return notFound();
	}

	// TODO together
	@Transactional
	@Restrict({ @Group("CUSTOMER") })
	public static Result basicInfo() {
		UserHandler uh = new UserHandler();
		User user = uh.getUserByEmail(session().get("connected"));
		switch (user.getUserRole()) {
		case CUSTOMER:
			return ok(cusinfo.render((Customer) user));
		case TRAINER:
			return ok(trainerbasicinfo.render((Trainer) user));
		}
		return internalServerError();
	}

	@Transactional
	@Restrict({ @Group("TRAINER") })
	public static Result trainerInfo() {
		UserHandler uh = new UserHandler();
		Trainer trainer = (Trainer) uh.getUserByEmail(session()
				.get("connected"));

		return ok(trainerinfo.render(trainer));
	}

	@Transactional
	@Restrict({ @Group("CUSTOMER"), @Group("TRAINER") })
	public static Result editBasicInfo() {
		UserHandler uh = new UserHandler();
		User user = uh.getUserByEmail(session().get("connected"));
		LocationHandler lh = new LocationHandler();
		List<String> stateList = LocationHandler.getStateList();
		switch (user.getUserRole()) {
		case CUSTOMER:
			return ok(cusinfoedit.render((Customer) user, stateList));
		case TRAINER:
			return ok(trainerbasicinfoedit.render((Trainer) user, stateList));
		}
		return internalServerError();

	}

	@Transactional
	@Restrict({ @Group("CUSTOMER"), @Group("TRAINER") })
	public static Result basicInfoEditSubmit() throws IOException {
		Form<CustomerForm> cusForm = form(CustomerForm.class).bindFromRequest();
		IUserHandler uh = new UserHandler();
		uh.updateProfile(session().get("connected"), cusForm.get());

		// image processing
		FilePart picture = request().body().asMultipartFormData()
				.getFile("picture");
		String oldpath = uh.getUserByEmail(session().get("connected"))
				.getImage();
		ImageHandler ih = new ImageHandler();
		if (ih.processImage(
				request().body().asMultipartFormData().getFile("picture"),
				oldpath) != null) {
			String imagePath = ih.processImage(picture, oldpath);
			uh.getUserByEmail(session().get("connected")).setImage(imagePath);
		}
		flash("error", "Missing file");
		return redirect(routes.Application.editBasicInfo());

	}

	@Transactional
	@Restrict({ @Group("CUSTOMER"), @Group("TRAINER") })
	public static Result changepsw() {
		UserHandler uh = new UserHandler();
		User user = uh.getUserByEmail(session().get("connected"));
		switch (user.getUserRole()) {
		case CUSTOMER:
			return ok(cuschangepsw.render());
		case TRAINER:
			return ok(trainerchangepsw.render());
		default:
			return internalServerError();
		}
	}

	@Transactional
	@Restrict({ @Group("CUSTOMER"), @Group("TRAINER") })
	public static Result changepswsubmit() throws Exception {
		Form<NewPswForm> npf = form(NewPswForm.class).bindFromRequest();
		UserHandler uh = new UserHandler();
		User user = uh.getUserByEmail(session().get("connected"));
		String password = user.getPassword();

		if (Password.check(npf.get().getOldpsw(), password) == false) {
			flash("error", "original password is incorrect");
			return redirect(routes.Application.changepsw());
		}
		user.setPassword(npf.get().getNewpsw());

		return redirect(routes.Application.profile());
	}

	@Transactional
	public static Result itempage(Integer id) throws ParseException {
		CourseHandler ch = new CourseHandler();
		Course course = ch.getCourseById(id);
		Collection<Course> similarcourse = ch.getCourseByCategory(
				course.getCourseCategory(), 1, 3, null, true);
		LocationHandler lh = new LocationHandler();
		Collection<String> states = LocationHandler.getAvailableState(JPA.em());
		return ok(itempage.render(course, states));
	}

	@Transactional
	public static Result viewdetaillocation(Integer id) {
		return TODO;
	}

	@Transactional
	public static Result showSiderbarCity() {
		String stateName = form().bindFromRequest().get("name");
		if (stateName != null) {
			Collection<String> cityList = LocationHandler.getAvailableCity(
					stateName, JPA.em());

			System.out.println(cityList.size() + "hahaha");
			return ok(Json.toJson(cityList).toString());

		}
		return null;
	}

	@Transactional
	public static Result showCity() {

		String stateName = form().bindFromRequest().get("name");
		if (stateName != null) {
			List<String> cityList = LocationHandler.getCitiesByState(stateName);

			return ok(Json.toJson(cityList).toString());

		}
		return null;
	}

	@Transactional
	@Restrict({ @Group("TRAINER") })
	public static Result addschedule() throws ParseException, SQLException {
		String start = form().bindFromRequest().get("start");
		Logger.info(start);
		Date date = new SimpleDateFormat("yyyy-MM-dd").parse(start);
		UserHandler uh = new UserHandler();
		Trainer trainer = (Trainer) uh.getUserByEmail(session()
				.get("connected"));
		trainer.getAvailableDates().add(date);
		return ok("lala");
	}

	@Transactional
	@Restrict({ @Group("TRAINER") })
	public static Result deleteschedule() throws ParseException {
		String start = form().bindFromRequest().get("start");
		Date date = new SimpleDateFormat("yyyy-MM-dd").parse(start);
		UserHandler uh = new UserHandler();
		Trainer trainer = (Trainer) uh.getUserByEmail(session()
				.get("connected"));
		uh.removeAvailableDate(date, trainer);

		return ok();
	}

	@Transactional
	@Restrict({ @Group("TRAINER") })
	public static Result getschedule() {
		UserHandler uh = new UserHandler();
		Trainer trainer = uh.getTrainerByEmail(session().get("connected"));
		Set<Date> dates = (Set<Date>) trainer.getAvailableDates();

		List<String> states = new ArrayList();
		for (Iterator<Date> iter = dates.iterator(); iter.hasNext();) {
			String sdate = new SimpleDateFormat("yyyy-MM-dd").format(iter
					.next());
			states.add(sdate);
		}

		String json = jsonexchange(states);

		Logger.info(Json.toJson(json).toString());
		return ok(json);
	}

	public static String jsonexchange(List<String> states) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		String json = "";
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();

		for (int i = 0; i < states.size(); i++) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("title", "available day");
			map.put("start", states.get(i));

			result.add(map);
		}
		try {
			json = mapper.writeValueAsString(result);

		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return json;

	}

	// TODO writetogether
	@Transactional
	@Restrict({ @Group("TRAINER") })
	public static Result trainerCourseHistory(int status) {
		if (CourseStatus.fromInteger(status) != null) {
			CourseHandler ch = new CourseHandler();
			CourseFilterBuilder fb = new CourseFilterBuilder();
			fb.setCourseStatus(status);
			fb.setTrainerEmail(session().get("connected"));
			Collection<Course> course = ch.getCourseByCustomRule(fb, null,
					true, 1, 10);

			return ok(trainercoursehistory.render(course));
		}
		return notFound();
	}

	@Transactional
	@Restrict({ @Group("TRAINER") })
	public static Result trainerInfoEdit() {
		UserHandler uh = new UserHandler();
		Trainer trainer = (Trainer) uh.getUserByEmail(session()
				.get("connected"));
		return ok(trainerinfoedit.render(trainer));
	}

	@Transactional
	@Restrict({ @Group("TRAINER") })
	public static Result trainerInfoEditSubmit() {
		Form<TrainerForm> trainerForm = form(TrainerForm.class)
				.bindFromRequest();
		Logger.info(trainerForm.get().getName());
		IUserHandler uh = new UserHandler();
		uh.updateProfile(session().get("connected"), trainerForm.get());
		return redirect(routes.Application.trainerInfo());
	}

	@Transactional
	@Restrict({ @Group("TRAINER") })
	public static Result traineraddcourse() {
		return ok(traineraddcourse.render());
	}

	@Transactional
	@Restrict({ @Group("TRAINER") })
	public static Result traineraddcoursesubmit() {
		Form<CourseForm> courseForm = form(CourseForm.class).bindFromRequest();
		courseForm.get();
		CourseHandler ch = new CourseHandler();
		ch.addNewCourse(session().get("connected"), courseForm.get(),
				new UserHandler());

		return redirect(routes.Application
				.trainerCourseHistory(CourseStatus.VERIFYING.ordinal()));
	}

	@Transactional
	@Restrict({ @Group("CUSTOMER") })
	public static Result review(String orderId) {
		CourseHandler ch = new CourseHandler();
		OrderHandler oh = new OrderHandler();
		CourseOrder courseOrder = oh.getCourseOrderByOrderId(orderId);
		ReviewHandler rh = new ReviewHandler();
		Review r = rh.getReviewByCustomerAndCourse(session().get("connected"),
				courseOrder.getConcreteCourse().getId().toString());
		if (r != null) {
			flash("error", "you have already reviewed this course");
			return redirect(routes.Application
					.customerCourseHistory(OrderStatus.DONE.ordinal()));
		}
		return ok(review.render(courseOrder));
	}

	@Transactional
	public static Result reviewSubmit() {
		Form<ReviewForm> reviewForm = form(ReviewForm.class).bindFromRequest();
		Logger.info(reviewForm.get().getComment());
		Logger.info(reviewForm.get().getCourseRatings().iterator().next()
				.toString());
		UserHandler uh = new UserHandler();
		Customer author = uh.getCustomerByEmail(session().get("connected"));
		ReviewHandler rh = new ReviewHandler();
		Review re = rh
				.writeReview(reviewForm.get(), author, new OrderHandler());
		if (re == null) {
			flash("error",
					"you are not the right customer to write this comment");
		}
		return redirect(routes.Application
				.customerCourseHistory(OrderStatus.DONE.ordinal()));

	}

	@Transactional
	public static Result concreteCourseDisplay() {
		return ok(Course_list.render());
	}

	@Transactional
	public static Result dashConcreteCourse() {
		CourseHandler ch = new CourseHandler();
		// TODO get all the concreteCourse
		Collection<ConcreteCourse> concreteCourse = ch.getAllConcreteCourse();
		Collection<ConcreteCourseForm> concreteCourseForms = new ArrayList<ConcreteCourseForm>();
		for (ConcreteCourse cc : concreteCourse) {
			Logger.info(cc.getCourseInfo().getCourseName());
			ConcreteCourseForm ccf = ConcreteCourseForm
					.bindConcreteCourseForm(cc);
			concreteCourseForms.add(ccf);
		}
		System.out.print(Json.toJson(concreteCourseForms));
		return ok(Json.toJson(concreteCourseForms));
	}

	// how to pass several dates and location
	@Transactional
	public static Result dashConcreteCourseDetail(String concreteCourseId) {
		CourseHandler ch = new CourseHandler();
		ConcreteCourse concreteCourse = ch
				.getCourseByConcreteCourseId(concreteCourseId);
		ConcreteCourseForm ccf = ConcreteCourseForm
				.bindConcreteCourseForm(concreteCourse);
		System.out.print(Json.toJson(ccf));
		return ok(Json.toJson(ccf));
	}

	@Transactional
	public static Result dashConcreteCourseDelete() {
		Form<ConcreteCourseForm> concreteCourseForm = form(
				ConcreteCourseForm.class).bindFromRequest();
		CourseHandler ch = new CourseHandler();
		boolean flag = ch.deleteConcreteCourse(concreteCourseForm.get()
				.getConcreteCourseId());
		ObjectNode result = Json.newObject();
		if (flag == true) {
			result.put("result", "true");
			return ok(result);
		}
		result.put("result", "false");
		return ok(result);
	}

	@Transactional
	public static Result dashConcreteCourseUpdate() {
		Form<ConcreteCourseForm> concreteCourseForm = form(
				ConcreteCourseForm.class).bindFromRequest();
		CourseHandler ch = new CourseHandler();
		ConcreteCourse concreteCourse = ch
				.getCourseByConcreteCourseId(concreteCourseForm.get()
						.getConcreteCourseId());
		boolean flag = concreteCourseForm.get().bindConcreteCourse(
				concreteCourse);
		ObjectNode result = Json.newObject();
		if (flag == true) {
			result.put("result", "true");
			return ok(result);
		}
		result.put("result", "false");
		return ok(result);
	}

	@Transactional
	public static Result courseDisplay() {
		return ok(Course_request.render());
	}

	@Transactional
	public static Result dashCourse() {
		CourseHandler ch = new CourseHandler();
		FilterBuilder fb = new CourseFilterBuilder();

		Collection<Course> course = ch.getCourseByCustomRule(fb, null, true,
				-1, -1);
		Collection<CourseForm> courseForm = new ArrayList<CourseForm>();
		for (Course c : course) {
			Logger.info(c.getCourseName());
			CourseForm cf = CourseForm.bindCourseForm(c);
			courseForm.add(cf);
		}
		System.out.print(Json.toJson(courseForm));
		return ok(Json.toJson(courseForm));
	}

	@Transactional
	public static Result dashCourseDetail(String courseId) {
		CourseHandler ch = new CourseHandler();
		Course course = ch.getCourseById(Integer.parseInt(courseId));
		CourseForm cf = CourseForm.bindCourseForm(course);
		System.out.print(Json.toJson(cf));
		return ok(Json.toJson(cf));
	}

	@Transactional
	public static Result dashCourseDelete() {
		Form<CourseForm> courseForm = form(CourseForm.class).bindFromRequest();
		CourseHandler ch = new CourseHandler();
		boolean flag = ch.deleteCourse(courseForm.get().getCourseId());
		ObjectNode result = Json.newObject();
		if (flag == true) {
			result.put("result", "true");
			return ok(result);
		}
		result.put("result", "false");
		return ok(result);
	}

	@Transactional
	public static Result dashCourseUpdate() {
		Form<CourseForm> courseForm = form(CourseForm.class).bindFromRequest();
		CourseHandler ch = new CourseHandler();
		Course course = ch.getCourseById(courseForm.get().getCourseId());
		boolean flag = courseForm.get().bindCourse(course);
		ObjectNode result = Json.newObject();
		if (flag == true) {
			result.put("result", "true");
			return ok(result);
		}
		result.put("result", "false");
		return ok(result);
	}

	@Transactional
	public static Result dashConcreteCourseAdd() throws JsonParseException, JsonMappingException, IOException {
		ConcreteCourseForm concreteCourseForm = form(
				ConcreteCourseForm.class).bindFromRequest().get();
		if (concreteCourseForm.getCourseDates() != null) {
			Logger.info(concreteCourseForm.getCourseDates().size()+"jhhhe");
			
		}
		CourseHandler ch = new CourseHandler();
		ConcreteCourse concreteCourse = ch.addNewConcreteCourse(
				concreteCourseForm.getTrainerEmail(),
				concreteCourseForm);
		ObjectNode result = Json.newObject();
		if (concreteCourse != null) {
			result.put("result", "true");
			return ok(result);
		}
		result.put("result", "false");
		return ok(result);
	}

	@Transactional
	public static Result dashDashboard() {
		return ok(Dashboard.render());

	}

	@Transactional
	public static Result trainerDisplay() {
		return TODO;
	}

	@Transactional
	public static Result dashTrainer() {
		UserHandler uh = new UserHandler();
		UserFilterBuilder fb = new UserFilterBuilder();
		fb.setUserRole(UserRole.TRAINER);
		Collection<User> user = uh.getUserByCustomeRule(fb, null, true, -1, -1);

		Collection<TrainerForm> trainerForm = new ArrayList<TrainerForm>();
		for (User u : user) {
			Trainer t = (Trainer) u;
			TrainerForm tf = TrainerForm.bindTraienrForm(t);
			trainerForm.add(tf);
		}
		System.out.print(Json.toJson(trainerForm));
		return ok(Json.toJson(trainerForm));
	}

	//
	// @Transactional
	// public static Result dashTrainer(){
	// return ok(User_trainer.render());
	// }

	public static Result javascriptRoutes() {
		response().setContentType("text/javascript");
		return ok(Routes.javascriptRouter("jsRoutes",
				routes.javascript.Application.dashConcreteCourse(),
				routes.javascript.Application.dashConcreteCourseDetail(),
				routes.javascript.Application.dashConcreteCourseDelete(),
				routes.javascript.Application.dashConcreteCourseUpdate(),
				routes.javascript.Application.dashConcreteCourseAdd(),
				routes.javascript.Application.dashCourse(),
				routes.javascript.Application.dashCourseDetail(),
				routes.javascript.Application.dashCourseDelete(),
				routes.javascript.Application.dashCourseUpdate()));
	}

}