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
import java.net.URISyntaxException;
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

import org.apache.http.client.utils.URIBuilder;

import controllers.authentication.AuthenticationHandler;
import controllers.course.CourseHandler;
import controllers.course.EventbriteHandler;
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
import models.filters.BackendCourseFilter;
import models.filters.ConcreteCourseFilterBuilder;
import models.filters.CourseFilterBuilder;
import models.filters.FilterBuilder;
import models.filters.OrderFilterBuilder;
import models.filters.ReviewFilterBuilder;
import models.filters.UserFilterBuilder;
import models.forms.AdminForm;
import models.forms.ConcreteCourseForm;
import models.forms.CourseFilterForm;
import models.forms.CourseForm;
import models.forms.CourseOrderForm;
import models.forms.CustomerForm;
import models.forms.LoginForm;
import models.forms.NewPswForm;
import models.forms.ResetPswForm;
import models.forms.ReviewForm;
import models.forms.TrainerForm;
import models.forms.UserForm;
import models.locations.Geolocation;
import models.locations.Location;
import models.users.Admin;
import models.spellchecker.SolrSuggestions;
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
import play.libs.ws.WSResponse;
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
	public static Result welcome() throws URISyntaxException {
		CourseHandler ch = new CourseHandler();
		CourseFilterBuilder cfb = new CourseFilterBuilder();
		Collection<Course> course = ch.getCourseByCustomRule(cfb, null, true,
				1, 10);
		Logger.info("course" + course.size());
		LocationHandler lh = new LocationHandler();
		Collection<String> states = LocationHandler.getAvailableState(JPA.em());

		return ok(home.render(course, states));

	}

	@Transactional
	@Restrict({ @Group("GUEST") })
	public static Result login() {
		String redirect = flash().get("redirect") == null ? routes.Application
				.welcome().url() : flash().get("redirect");
		return ok(login.render(redirect));
	}

	@Transactional
	@Restrict({ @Group("GUEST") })
	public static Result loginAuthen() {
		Form<LoginForm> form = form(LoginForm.class).bindFromRequest();
		if (form.hasErrors()) {
			flash("error", "Username or Password is incorrect");
			return badRequest(login.render(routes.Application.welcome().url()));
		}
		LoginForm loginForm = form.get();
		Logger.info(loginForm.getEmail());
		AuthenticationHandler ah = new AuthenticationHandler();
		IUserHandler userHandler = new UserHandler();
		User u = ah.doLogin(loginForm.getEmail(), loginForm.getPassword(),
				Context.current(), userHandler);
		if (u != null) {
			return redirect(routes.Application.welcome());
		}
		flash("error", "Username or Password is incorrect");
		return badRequest(login.render(loginForm.getRedirect()));
	}

	@Transactional
	public static Result activate(String token) {
		AuthenticationHandler ah = new AuthenticationHandler();
		IUserHandler uh = new UserHandler();
		ah.activateUser(token, uh);
		return ok(activatesuccess.render());
	}

	@Transactional
	@Restrict({ @Group("GUEST") })
	public static Result resetpsw() {
		Form<ResetPswForm> resetPswForm = form(ResetPswForm.class)
				.bindFromRequest();
		if (resetPswForm.hasErrors()) {
			flash("error", "= =");
			// TODO wo bu zhi dao zhuan na li
			return notFound();
		}
		AuthenticationHandler ah = new AuthenticationHandler();
		IUserHandler uh = new UserHandler();
		if (ah.doResetPassword(resetPswForm.get().getToken(), resetPswForm
				.get().getNewPassword(), uh)) {
			return ok(resetpswconfirm.render());
		}
		// TODO wo bu zhi dao zhuan na li
		return notFound();
	}

	// TODO show password form here
	@Transactional
	@Restrict({ @Group("GUEST") })
	public static Result authorizeResetPassword(String token) {
		return ok(auth_password.render(token));
	}

	@Transactional
	@Restrict({ @Group("GUEST") })
	public static Result forgetpsw() {
		return ok(forgetpsw.render());
	}

	@Transactional
	@Restrict({ @Group("GUEST") })
	public static Result forgetpswsubmit() {
		String email = form().bindFromRequest().get("email");
		if (email != null) {
			UserHandler uh = new UserHandler();
			User u = uh.getUserByEmail(email);
			if (u != null) {
				AuthenticationHandler ah = new AuthenticationHandler();
				IMailHandler mh = new MailHandler();
				ah.authorizeResetPassword(u.getUsername(), u.getEmail(), mh);
				return ok(forgetpswconfirm.render());
			}
		}
		flash("error", "email doesn't exist!!!");
		return ok(forgetpsw.render());
	}

	@Transactional
	@Restrict({ @Group("!GUEST") })
	public static Result logout() {
		AuthenticationHandler ah = new AuthenticationHandler();
		ah.doLogout(Context.current());
		return redirect(routes.Application.welcome());
	}

	@Transactional
	public static Result search(String orderBy, Integer pageNumber) {
		orderBy = "default".equals(orderBy) ? null : orderBy;
		CourseFilterForm filterForm = form(CourseFilterForm.class)
				.bindFromRequest().get();
		CourseHandler ch = new CourseHandler();
		filterForm.transferChoiceToRange();
		filterForm.setCurentLocation(LocationHandler
				.getLocationFromSession(session()));
		Collection<Course> course = ch.getCourseByCustomRule(filterForm,
				orderBy, true, pageNumber < 1 ? 1 : pageNumber,
				Play.application().configuration().getInt("page.size.search"));
		Logger.info("course" + course.size());
		LocationHandler lh = new LocationHandler();
		// TODO cache
		Collection<String> states = LocationHandler.getAvailableState(JPA.em());
		ConcreteCourseFilterBuilder ccfb = new ConcreteCourseFilterBuilder();
		ccfb.setCfb(filterForm);
		Map<Integer, List<ConcreteCourse>> courseMap = ch.getConcreteCourseMap(
				ccfb, null, true, -1, -1);
		String newKeyword = null, newKeywordQuery = null;
		if (filterForm.getKeyword() != null) {
			newKeyword = SolrSuggestions
					.getSuggestions(filterForm.getKeyword());
			if (newKeyword != null) {
				newKeywordQuery = Utility.replaceKeyword(
						Utility.getQueryString(request().uri()),
						filterForm.getKeyword(), newKeyword);
			}

		}
		return ok(searchindex.render(course, states, pageNumber,
				Utility.getQueryString(request().uri()), newKeyword,
				newKeywordQuery));
	}

	@Transactional
	@Restrict({ @Group("GUEST") })
	public static Result signup(int userRole) {
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
	@Restrict({ @Group("GUEST") })
	public static Result signupSubmit(int userRole) {
		UserForm userForm = null;
		Form form;
		switch (UserRole.fromInteger(userRole)) {
		case CUSTOMER:
			form = form(CustomerForm.class).bindFromRequest();
			if (!form.hasErrors()) {
				userForm = (UserForm) form.get();
				break;
			}
		case TRAINER:
			form = form(TrainerForm.class).bindFromRequest();
			if (!form.hasErrors()) {
				userForm = (UserForm) form.get();
				break;
			}
		default:
			return badRequest(signupemail.render());
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
		User user = (User) Context.current().args.get("connected");
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
	public static Promise<Result> createOrder(String orderId,
			String eventbriteId) {
		Logger.info(orderId);
		Logger.info(eventbriteId);
		if (session().get("connected") == null) {
			flash("error", "your order is failed because you log out");
			return Promise.promise(new Function0<Result>() {
				public Result apply() {
					return redirect(routes.Application.profile());
				}
			});
		}
		CourseHandler ch = new CourseHandler();
		Promise<CourseOrder> orderPromise = ch.registerCourse(new UserHandler().getCustomerByEmail(session().get(
				"connected")), ch.getCourseByEventbriteId(eventbriteId),
				orderId, new OrderHandler());
		return orderPromise.map(new Function<CourseOrder, Result>() {
			public Result apply(CourseOrder courseOrder) {
				if(courseOrder == null)
					flash("error", "your order is failed because you log out");
				return redirect(routes.Application.profile());
			}
		});
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
		User user = (User) Context.current().args.get("connected");
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
		Trainer trainer = (Trainer) Context.current().args.get("connected");
		return ok(trainerinfo.render(trainer));
	}

	@Transactional
	@Restrict({ @Group("CUSTOMER"), @Group("TRAINER") })
	public static Result editBasicInfo() {
		UserHandler uh = new UserHandler();
		User user = (User) Context.current().args.get("connected");
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
	public static Result basicInfoEditSubmit() {
		IUserHandler uh = new UserHandler();
		User user = (User) Context.current().args.get("connected");
		Form form = null;
		UserForm userForm = null;
		switch (user.getUserRole()) {
		case CUSTOMER:
			form = form(CustomerForm.class).bindFromRequest();
			if (form.hasErrors()) {
				List<String> stateList = LocationHandler.getStateList();
				return ok(cusinfoedit.render((Customer) user, stateList));
			}
			userForm = (UserForm) form.get();
			break;
		case TRAINER:
			form = form(TrainerForm.class).bindFromRequest();
			if (form.hasErrors()) {
				List<String> stateList = LocationHandler.getStateList();
				return ok(trainerbasicinfoedit
						.render((Trainer) user, stateList));
			}
			userForm = (UserForm) form.get();
			break;
		}
		uh.updateProfile(session().get("connected"), userForm);
		if (request().body().asMultipartFormData() != null) {
			try {
				FilePart picture = request().body().asMultipartFormData()
						.getFile("picture");
				String oldpath = user.getImage();
				ImageHandler ih = new ImageHandler();
				if (ih.processImage(request().body().asMultipartFormData()
						.getFile("picture"), oldpath) != null) {
					String imagePath = ih.processImage(picture, oldpath);
					user.setImage(imagePath);
				}
			} catch (Exception e) {
				Logger.error(e.toString());
			}
		}
		flash("error", "Missing file");
		return redirect(routes.Application.editBasicInfo());

	}

	@Transactional
	@Restrict({ @Group("CUSTOMER"), @Group("TRAINER") })
	public static Result changepsw() {
		User user = (User) Context.current().args.get("connected");
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
		User user = (User) Context.current().args.get("connected");
		if (npf.hasErrors()) {
			switch (user.getUserRole()) {
			case CUSTOMER:
				return badRequest(cuschangepsw.render());
			case TRAINER:
				return badRequest(trainerchangepsw.render());
			default:
				return internalServerError();
			}
		}
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
		CourseFilterForm filterForm = form(CourseFilterForm.class)
				.bindFromRequest().get();
		CourseHandler ch = new CourseHandler();
		Course course = ch.getCourseById(id);
		Collection<Course> similarcourse = ch.getCourseByCategory(
				course.getCourseCategory(), 1, 3, null, true);
		LocationHandler lh = new LocationHandler();
		Collection<String> states = LocationHandler.getAvailableState(JPA.em());
		Collection<ConcreteCourse> concreteCourseList = ch
				.getDisplayedConcreteCourse(filterForm, id);
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
		Trainer trainer = (Trainer) Context.current().args.get("connected");
		trainer.getAvailableDates().add(date);
		return ok("lala");
	}

	@Transactional
	@Restrict({ @Group("TRAINER") })
	public static Result deleteschedule() throws ParseException {
		String start = form().bindFromRequest().get("start");
		Date date = new SimpleDateFormat("yyyy-MM-dd").parse(start);
		UserHandler uh = new UserHandler();
		Trainer trainer = (Trainer) Context.current().args.get("connected");
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
	public static Result trainerCourseHistory(int status, int page) {
		CourseHandler ch = new CourseHandler();
		BackendCourseFilter fb = new BackendCourseFilter();
		if (CourseStatus.fromInteger(status) != null)
			fb.setCourseStatus(status);
		else
			status = -1;
		fb.setTrainerEmail(session().get("connected"));
		Collection<Course> course = ch.getCourseByCustomRule(fb, null, true,
				page < 1 ? 1 : page,
				Play.application().configuration().getInt("page.size.search"));

		return ok(trainercoursehistory.render(course));
	}

	@Transactional
	@Restrict({ @Group("TRAINER") })
	public static Result trainerInfoEdit() {
		Trainer trainer = (Trainer) Context.current().args.get("connected");
		return ok(trainerinfoedit.render(trainer));
	}

	@Transactional
	@Restrict({ @Group("TRAINER") })
	public static Result trainerInfoEditSubmit() {
		Form<TrainerForm> trainerForm = form(TrainerForm.class)
				.bindFromRequest();
		User user = (User) Context.current().args.get("connected");
		IUserHandler uh = new UserHandler();
		if (trainerForm.hasErrors())
			return badRequest(trainerinfoedit.render((Trainer) user));
		Logger.info(trainerForm.get().getName());
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
		Form<CourseForm> form = form(CourseForm.class).bindFromRequest();
		if (form.hasErrors())
			return badRequest(traineraddcourse.render());
		CourseForm courseForm = form.get();
		Logger.info(courseForm.getCourseDesc() + "hahaha");
		CourseHandler ch = new CourseHandler();
		Course course = ch.addNewCourse(session().get("connected"), courseForm,
				new UserHandler());
		if (request().body().asMultipartFormData() != null) {
			try {
				FilePart picture = request().body().asMultipartFormData()
						.getFile("picture");
				ImageHandler ih = new ImageHandler();
				if (ih.processImage(request().body().asMultipartFormData()
						.getFile("picture"), null) != null) {
					String imagePath = ih.processImage(picture, null);
					course.setImage(imagePath);
				}
			} catch (Exception e) {
				Logger.info(e.toString());
			}
		}
		return redirect(routes.Application.trainerCourseHistory(
				CourseStatus.VERIFYING.ordinal(), 1));
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
	@Restrict({ @Group("CUSTOMER") })
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

		ConcreteCourseForm concreteCourseForm = form(ConcreteCourseForm.class)
				.bindFromRequest().get();
		Logger.info(concreteCourseForm.getConcreteCourseId());
		Logger.info(concreteCourseForm.getLocation().getDetailedLoc());
		CourseHandler ch = new CourseHandler();
		ConcreteCourse concreteCourse = ch
				.getCourseByConcreteCourseId(concreteCourseForm
						.getConcreteCourseId());
		boolean flag = concreteCourseForm.bindConcreteCourse(concreteCourse);
		ObjectNode result = Json.newObject();
		if (flag == true) {
			result.put("result", "true");
			return ok(result);
		}
		result.put("result", "false");
		return ok(result);
	}

	@Transactional
	public static Promise<Result> dashConcreteCourseAdd() throws JsonParseException,
			JsonMappingException, IOException {
		ConcreteCourseForm concreteCourseForm = form(ConcreteCourseForm.class)
				.bindFromRequest().get();
		if (concreteCourseForm.getCourseDates() != null) {
			Logger.info(concreteCourseForm.getCourseDates().size() + "jhhhe");

		}
		CourseHandler ch = new CourseHandler();
		Promise<ConcreteCourse> concreteCoursePromise = ch.addNewConcreteCourse(
				concreteCourseForm.getTrainerEmail(), concreteCourseForm);
		return concreteCoursePromise.map(new Function<ConcreteCourse, Result>(){
			@Override
			public Result apply(ConcreteCourse concreteCourse) throws Throwable {
				ObjectNode result = Json.newObject();
				if (concreteCourse != null) {
					result.put("result", "true");
					return ok(result);
				}
				result.put("result", "false");
				return ok(result);
			}
			
		});
	}

	@Transactional
	public static Result courseDisplay() {
		LocationHandler lh = new LocationHandler();
		List<String> stateList = LocationHandler.getStateList();
		return ok(Course_request.render(stateList));
	}

	@Transactional
	public static Result dashCourse() {
		CourseHandler ch = new CourseHandler();
		FilterBuilder fb = new BackendCourseFilter();

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
	public static Result dashDashboard() {
		return ok(Dashboard.render());

	}

	@Transactional
	public static Result orderDisplay() {
		return ok(Course_order.render());
	}

	@Transactional
	public static Result dashOrder() {
		OrderHandler oh = new OrderHandler();
		FilterBuilder fb = new OrderFilterBuilder();
		Collection<CourseOrder> courseOrder = oh.getCourseOrderByCustomRule(fb,
				null, -1, -1);

		Collection<CourseOrderForm> courseOrderForm = new ArrayList<CourseOrderForm>();
		for (CourseOrder co : courseOrder) {
			CourseOrderForm cof = CourseOrderForm.bindCourseOrderForm(co);
			courseOrderForm.add(cof);
		}
		System.out.print(Json.toJson(courseOrderForm));
		return ok(Json.toJson(courseOrderForm));
	}

	@Transactional
	public static Result dashOrderDetail(String orderId) {

		OrderHandler oh = new OrderHandler();
		CourseOrder courseOrder = oh.getCourseOrderByOrderId(orderId);
		CourseOrderForm cof = CourseOrderForm.bindCourseOrderForm(courseOrder);

		System.out.print(Json.toJson(cof));
		return ok(Json.toJson(cof));

	}

	@Transactional
	public static Result dashOrderUpdate() {
		CourseOrderForm courseOrderForm = form(CourseOrderForm.class)
				.bindFromRequest().get();
		OrderHandler oh = new OrderHandler();
		CourseOrder courseOrder = oh.getCourseOrderByOrderId(courseOrderForm
				.getOrderId());
		boolean flag = courseOrderForm.bindCourseOrder(courseOrder);
		ObjectNode result = Json.newObject();
		if (flag == true) {
			result.put("result", "true");
			return ok(result);
		}
		result.put("result", "false");
		return ok(result);
	}

	@Transactional
	public static Result ratingDisplay() {
		return ok(Rating.render());
	}

	@Transactional
	public static Result dashRating() {
		ReviewHandler rh = new ReviewHandler();
		FilterBuilder fb = new ReviewFilterBuilder();

		Collection<Review> review = rh
				.getReviewByCustomerRule(fb, null, -1, -1);

		Collection<ReviewForm> reviewForm = new ArrayList<ReviewForm>();
		for (Review re : review) {
			ReviewForm rf = ReviewForm.bindReviewForm(re);
			reviewForm.add(rf);
		}
		System.out.print(Json.toJson(reviewForm));
		return ok(Json.toJson(reviewForm));
	}

	// @Transactional
	// public static Result dashOrderDelete(){
	// CourseOrderForm courseOrderForm =
	// form(CourseOrderForm.class).bindFromRequest().get();
	// OrderHandler oh = new OrderHandler();
	// boolean flag = oh.
	// ObjectNode result = Json.newObject();
	// if (flag == true) {
	// result.put("result", "true");
	// return ok(result);
	// }
	// result.put("result", "false");
	// return ok(result);
	// }

	//

	@Transactional
	public static Result trainerDisplay() {
		return ok(User_trainer.render());
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

	@Transactional
	public static Result dashTrainerDetail(Integer id) {
		UserHandler uh = new UserHandler();
		Trainer trainer = (Trainer) uh.getUserById(id);
		TrainerForm trainerForm = TrainerForm.bindTraienrForm(trainer);

		System.out.print(Json.toJson(trainerForm));
		return ok(Json.toJson(trainerForm));
	}

	@Transactional
	public static Result dashTrainerUpdate() {
		TrainerForm trainerForm = form(TrainerForm.class).bindFromRequest()
				.get();
		UserHandler uh = new UserHandler();
		User user = uh.getUserByEmail(trainerForm.getEmail());
		boolean flag = trainerForm.bindUser(user);
		ObjectNode result = Json.newObject();
		if (flag == true) {
			result.put("result", "true");
			return ok(result);
		}
		result.put("result", "false");
		return ok(result);
	}

	@Transactional
	public static Result customerDisplay() {
		return ok(User_attendee.render());
	}

	@Transactional
	public static Result dashCustomer() {
		UserHandler uh = new UserHandler();
		UserFilterBuilder fb = new UserFilterBuilder();
		fb.setUserRole(UserRole.CUSTOMER);
		Collection<User> user = uh.getUserByCustomeRule(fb, null, true, -1, -1);

		Collection<CustomerForm> customerForm = new ArrayList<CustomerForm>();
		for (User u : user) {
			Customer c = (Customer) u;
			CustomerForm tf = CustomerForm.bindCustomerForm(c);
			customerForm.add(tf);
		}
		System.out.print(Json.toJson(customerForm));
		return ok(Json.toJson(customerForm));
	}

	@Transactional
	public static Result dashCustomerDetail(Integer id) {
		UserHandler uh = new UserHandler();
		Customer customer = (Customer) uh.getUserById(id);
		CustomerForm customerForm = CustomerForm.bindCustomerForm(customer);

		System.out.print(Json.toJson(customerForm));
		return ok(Json.toJson(customerForm));
	}

	@Transactional
	public static Result dashCustomerUpdate() {
		CustomerForm customerForm = form(CustomerForm.class).bindFromRequest()
				.get();
		UserHandler uh = new UserHandler();
		User user = uh.getUserByEmail(customerForm.getEmail());
		boolean flag = customerForm.bindUser(user);
		ObjectNode result = Json.newObject();
		if (flag == true) {
			result.put("result", "true");
			return ok(result);
		}
		result.put("result", "false");
		return ok(result);
	}

	@Transactional
	public static Result adminDisplay() {
		return ok(User_admin.render());
	}

	@Transactional
	public static Result dashAdmin() {
		UserHandler uh = new UserHandler();
		UserFilterBuilder fb = new UserFilterBuilder();
		fb.setUserRole(UserRole.ADMIN);
		Collection<User> user = uh.getUserByCustomeRule(fb, null, true, -1, -1);

		Collection<AdminForm> adminForm = new ArrayList<AdminForm>();
		for (User u : user) {
			Admin a = (Admin) u;
			AdminForm af = AdminForm.bindAdminForm(a);
			adminForm.add(af);
		}
		System.out.print(Json.toJson(adminForm));
		return ok(Json.toJson(adminForm));
	}

	@Transactional
	public static Result dashAdminDetail(Integer id) {
		UserHandler uh = new UserHandler();
		Admin admin = (Admin) uh.getUserById(id);
		AdminForm adminForm = AdminForm.bindAdminForm(admin);

		System.out.print(Json.toJson(adminForm));
		return ok(Json.toJson(adminForm));
	}

	@Transactional
	public static Result dashAdminUpdate() {
		AdminForm adminForm = form(AdminForm.class).bindFromRequest().get();
		UserHandler uh = new UserHandler();
		User user = uh.getUserByEmail(adminForm.getEmail());
		boolean flag = adminForm.bindUser(user);
		ObjectNode result = Json.newObject();
		if (flag == true) {
			result.put("result", "true");
			return ok(result);
		}
		result.put("result", "false");
		return ok(result);
	}

	@Transactional
	public static Result dashAdminAdd() {
		AdminForm adminForm = form(AdminForm.class).bindFromRequest().get();
		UserHandler uh = new UserHandler();
		User user = uh.createNewUser(adminForm.getEmail(),
				adminForm.getUsername(), adminForm.getPassword(),
				UserRole.ADMIN, adminForm);
		boolean flag = adminForm.bindUser(user);
		ObjectNode result = Json.newObject();
		if (flag == true) {
			result.put("result", "true");
			return ok(result);
		}
		result.put("result", "false");
		return ok(result);
	}

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
				routes.javascript.Application.dashCourseUpdate(),
				routes.javascript.Application.dashOrder(),
				routes.javascript.Application.dashOrderDetail(),
				routes.javascript.Application.dashOrderUpdate(),
				routes.javascript.Application.dashRating(),
				routes.javascript.Application.dashTrainer(),
				routes.javascript.Application.dashTrainerDetail(),
				routes.javascript.Application.dashTrainerUpdate(),
				routes.javascript.Application.dashCustomer(),
				routes.javascript.Application.dashCustomerDetail(),
				routes.javascript.Application.dashCustomerUpdate(),
				routes.javascript.Application.dashAdmin(),
				routes.javascript.Application.dashAdminDetail(),
				routes.javascript.Application.dashAdminUpdate(),
				routes.javascript.Application.dashAdminAdd()));
	}

}