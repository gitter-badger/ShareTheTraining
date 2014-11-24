package controllers;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.typesafe.plugin.MailerAPI;
import com.typesafe.plugin.MailerPlugin;

import java.text.*;

import common.Password;

import java.io.File;
import java.io.FileOutputStream;
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

import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import controllers.authentication.AuthenticationHandler;
import controllers.course.CourseHandler;
import controllers.course.OrderHandler;
import controllers.image.ImageHandler;
import controllers.image.ImageResize;
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
import models.filters.CourseFilterBuilder;
import models.filters.FilterBuilder;
import models.filters.OrderFilterBuilder;
import models.forms.ConcreteCourseForm;
import models.forms.CourseFilterForm;
import models.forms.CourseForm;
import models.forms.CustomerForm;
import models.forms.LoginForm;
import models.forms.NewPswForm;
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
		// Collection<Course> course = ch.getCourseByCustomRule(cfb,
		// "popularity",
		// 2, 2);
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
		Logger.info(loginForm.get().getEmail());
		AuthenticationHandler ah = new AuthenticationHandler();
		IUserHandler userHandler = new UserHandler();
		User u = ah.doLogin(loginForm.get().getEmail(), loginForm.get()
				.getPassword(), Context.current(), userHandler);
		if (u != null) {

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
	public static Result resetpsw(String token) {
		AuthenticationHandler ah = new AuthenticationHandler();
		IUserHandler uh = new UserHandler();
		// ah.doResetPassword(token, newPassword, userHandler);
		return TODO;
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
	public static Result search() {
		Form<CourseFilterForm> filterForm = form(CourseFilterForm.class)
				.bindFromRequest();
	

		int datec = filterForm.get().getCfb().getDataChoice();
		CourseHandler ch = new CourseHandler();
		Collection<Course> course = ch.getCourseByCustomRule(CourseFilterForm
				.transferChoiceToRange(datec, filterForm.get()).getCfb(), null,
				true, 1, 10);
		Logger.info("course" + course.size());
		LocationHandler lh = new LocationHandler();
		Collection<String> states = LocationHandler.getAvailableState(JPA.em());

		return ok(searchindex.render(course, states));
	}

	@Transactional
	public static Result searchall() {

		CourseFilterForm filterForm = new CourseFilterForm();
		CourseHandler ch = new CourseHandler();
		Collection<Course> course = ch.getCourseByCustomRule(
				filterForm.getCfb(), null, true, 1, 10);
		Logger.info("course" + course.size());

		LocationHandler lh = new LocationHandler();
		Collection<String> states = LocationHandler.getAvailableState(JPA.em());

		return ok(searchindex.render(course, states));
	}

	@Transactional
	public static Result searchloc(String city, String region) {
		Logger.info(city);
		Logger.info(region);
		CourseFilterForm filterForm = new CourseFilterForm();
		Location loc = new Location();
		loc.setRegion(region);
		loc.setCity(city);
		ArrayList<Location> locationList = new ArrayList<Location>();
		locationList.add(loc);
		filterForm.getCfb().setLocations(locationList);
		;
		CourseHandler ch = new CourseHandler();
		Collection<Course> course = ch.getCourseByCustomRule(
				filterForm.getCfb(), null, true, 1, 10);
		LocationHandler lh = new LocationHandler();
		Collection<String> states = LocationHandler.getAvailableState(JPA.em());

		return ok(searchindex.render(course, states));
	}

	@Transactional
	public static Result signupcussubmit() {
		Form<CustomerForm> cusForm = form(CustomerForm.class).bindFromRequest();
		AuthenticationHandler ah = new AuthenticationHandler();
		IUserHandler uh = new UserHandler();
		IMailHandler mh = new MailHandler();
		ah.doRegister(cusForm.get().getEmail(), cusForm.get().getName(),
				cusForm.get().getPassword(), UserRole.values()[1],cusForm.get(), uh, mh);

		return ok(signupemail.render());
	}

	@Transactional
	public static Result signupcus() {
		Logger.info(session().get("connected"));
		LocationHandler lh = new LocationHandler();
		List<String> stateList = LocationHandler.getStateList();
		return ok(customersignup.render(stateList));
	}

	@Transactional
	public static Result signuptrainer() {
		LocationHandler lh = new LocationHandler();
		List<String> stateList = LocationHandler.getStateList();
		return ok(trainersignup.render(stateList));
	}

	@Transactional
	public static Result signuptrainersubmit() {
		Form<TrainerForm> trainerForm = form(TrainerForm.class)
				.bindFromRequest();
		AuthenticationHandler ah = new AuthenticationHandler();
		IUserHandler uh = new UserHandler();
		IMailHandler mh = new MailHandler();
		ah.doRegister(trainerForm.get().getEmail(),
				trainerForm.get().getName(), trainerForm.get().getPassword(),
				UserRole.values()[2],trainerForm.get(), uh, mh);
		return ok(signupemail.render());
	}

	@Transactional
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

		return null;
	}

	@Transactional
	@Restrict({ @Group("CUSTOMER")})
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
	@Restrict({ @Group("CUSTOMER")})
	public static Result cuscourseconfirmed() {
		OrderHandler oh = new OrderHandler();
		OrderFilterBuilder fb = new OrderFilterBuilder();
		fb.setOrderStatus(OrderStatus.CONFIRMED.ordinal());
		fb.setUserEmail(session().get("connected"));
		Collection<CourseOrder> order = oh.getCourseOrderByCustomRule(fb, null,
				1, 10);
		return ok(cuscoursehistory.render(order));
	}

	@Transactional
	@Restrict({ @Group("CUSTOMER")})
	public static Result cuscoursedone() {
		OrderHandler oh = new OrderHandler();
		OrderFilterBuilder fb = new OrderFilterBuilder();
		fb.setOrderStatus(OrderStatus.DONE.ordinal());
		fb.setUserEmail(session().get("connected"));
		Collection<CourseOrder> order = oh.getCourseOrderByCustomRule(fb, null,
				-1, -1);
		return ok(cuscoursehistory.render(order));
	}


	@Transactional
	@Restrict({ @Group("CUSTOMER")})
	public static Result cuscoursecanceled() {
		OrderHandler oh = new OrderHandler();
		OrderFilterBuilder fb = new OrderFilterBuilder();
		fb.setOrderStatus(OrderStatus.CANCELLED.ordinal());
		fb.setUserEmail(session().get("connected"));
		Collection<CourseOrder> order = oh.getCourseOrderByCustomRule(fb, null,
				-1, -1);
		return ok(cuscoursehistory.render(order));
	}

	@Transactional
	@Restrict({ @Group("CUSTOMER")})
	public static Result cusinfo() {
		UserHandler uh = new UserHandler();
		Customer customer = (Customer) uh.getUserByEmail(session().get(
				"connected"));

		return ok(cusinfo.render(customer));
	}

	@Transactional
	@Restrict({ @Group("CUSTOMER")})
	public static Result cusinfoedit() {
		UserHandler uh = new UserHandler();

		Customer customer = (Customer) uh.getUserByEmail(session().get(
				"connected"));
		LocationHandler lh = new LocationHandler();
		List<String> stateList = LocationHandler.getStateList();
		return ok(cusinfoedit.render(customer, stateList));

	}

	@Transactional
	@Restrict({ @Group("CUSTOMER")})
	public static Result cusinfoeditsubmit() throws IOException {
		Form<CustomerForm> cusForm = form(CustomerForm.class).bindFromRequest();
		Logger.info(cusForm.get().getPhone());
		Logger.info(cusForm.get().getEmail());
		IUserHandler uh = new UserHandler();
		uh.updateProfile(session().get("connected"), cusForm.get());
		
		//image processing
		MultipartFormData body = request().body().asMultipartFormData();
		FilePart picture = body.getFile("picture");
		String oldpath = uh.getUserByEmail(session().get("connected")).getImage();
		ImageHandler ih = new ImageHandler();
		if(ih.processImage(picture,oldpath)!=null){
			String imagePath = ih.processImage(picture, oldpath);
			uh.getUserByEmail(session().get("connected")).setImage(imagePath);
			return redirect(routes.Application.cusinfo());
		}
		 	flash("error", "Missing file");
			return redirect(routes.Application.cusinfoedit());
		  
	}

	@Transactional
	@Restrict({ @Group("CUSTOMER")})
	public static Result cuschangepsw() {
		return ok(cuschangepsw.render());
	}

	@Transactional
	@Restrict({ @Group("CUSTOMER")})
	public static Result cuschangepswsubmit() throws Exception {
		Form<NewPswForm> npf = form(NewPswForm.class).bindFromRequest();
		Logger.info(npf.get().getOldpsw() + "nimabi");
		Logger.info(npf.get().getNewpsw() + "nimabi");
		UserHandler uh = new UserHandler();
		Customer customer = (Customer) uh.getUserByEmail(session().get(
				"connected"));
		String password = customer.getPassword();

		if (Password.check(npf.get().getOldpsw(), password) == false) {
			flash("error", "original password is incorrect");
			return redirect(routes.Application.cuschangepsw());
		}
		customer.setPassword(npf.get().getNewpsw());

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

		// Collection<ConcreteCourse> cc=c.getCourses();
		//
		// cc.iterator().next().getMaximum()

		return ok(itempage.render(course, states));
	}

	@Transactional
	public static Result viewdetaillocation(Integer id) {
		return TODO;
	}

	@Transactional
	public static Result showSiderbarCity() {
		String stateName = form().bindFromRequest().get("name");
		System.out.print("HEHEHEH");
		System.out.print(stateName);
		if (stateName != null) {
			Collection<String> cityList = LocationHandler.getAvailableCity(
					stateName, JPA.em());

			System.out.println(cityList.size()+"hahaha");
			return ok(Json.toJson(cityList).toString());

		}
		return null;
	}

	@Transactional
	public static Result showCity() {

		String stateName = form().bindFromRequest().get("name");
		if (stateName != null) {
			System.out.println(stateName+"HEHEHEH");
			List<String> cityList = LocationHandler.getCitiesByState(stateName);

			return ok(Json.toJson(cityList).toString());

		}
		return null;
	}

	@Transactional
	@Restrict({ @Group("TRAINER")})
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
	@Restrict({ @Group("TRAINER")})
	public static Result deleteschedule() throws ParseException{
		String start = form().bindFromRequest().get("start");
		Date date = new SimpleDateFormat("yyyy-MM-dd").parse(start);
		UserHandler uh = new UserHandler();
		Trainer trainer = (Trainer) uh.getUserByEmail(session()
				.get("connected"));
		uh.removeAvailableDate(date, trainer);
		
		return ok();
	}

	@Transactional
	@Restrict({ @Group("TRAINER")})
	public static Result getschedule() {
		UserHandler uh = new UserHandler();
		Trainer trainer = uh.getTrainerByEmail(session().get("connected"));
		Set<Date> dates = (Set<Date>) trainer.getAvailableDates();

		List<String> sdates = new ArrayList();
		for (Iterator<Date> iter = dates.iterator(); iter.hasNext();) {
			String sdate = new SimpleDateFormat("yyyy-MM-dd").format(iter
					.next());
			sdates.add(sdate);
		}

		Application aa = new Application();
		String json = aa.jsonexchange(sdates);

		Logger.info(Json.toJson(json).toString());
		return ok(json);
	}

	public String jsonexchange(List<String> sdates) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		String json = "";
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();

		for (int i = 0; i < sdates.size(); i++) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("title", "available day");
			map.put("start", sdates.get(i));

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

	

	@Transactional
	@Restrict({ @Group("TRAINER")})
	public static Result trainercourseapproved() {

		CourseHandler ch = new CourseHandler();
		CourseFilterBuilder fb = new CourseFilterBuilder();
		fb.setCourseStatus(CourseStatus.APPROVED.ordinal());
		fb.setTrainerEmail(session().get("connected"));
		Collection<Course> course = ch.getCourseByCustomRule(fb, null, true, 1,
				10);

		return ok(trainercoursehistory.render(course));
	}

	@Transactional
	@Restrict({ @Group("TRAINER")})
	public static Result trainercourseverifying() {

		CourseHandler ch = new CourseHandler();
		CourseFilterBuilder fb = new CourseFilterBuilder();
		fb.setCourseStatus(CourseStatus.VERIFYING.ordinal());
		fb.setTrainerEmail(session().get("connected"));
		Collection<Course> course = ch.getCourseByCustomRule(fb, null, true, 1,
				10);

		return ok(trainercoursehistory.render(course));
	}

	@Transactional
	@Restrict({ @Group("TRAINER")})
	public static Result trainercoursecompleted() {

		CourseHandler ch = new CourseHandler();
		CourseFilterBuilder fb = new CourseFilterBuilder();
		fb.setCourseStatus(CourseStatus.COMPLETED.ordinal());
		fb.setTrainerEmail(session().get("connected"));
		Collection<Course> course = ch.getCourseByCustomRule(fb, null, true, 1,
				10);

		return ok(trainercoursehistory.render(course));
	}

	@Transactional
	@Restrict({ @Group("TRAINER")})
	public static Result trainercoursecanceled() {

		CourseHandler ch = new CourseHandler();
		CourseFilterBuilder fb = new CourseFilterBuilder();
		fb.setCourseStatus(CourseStatus.CANCELLED.ordinal());
		fb.setTrainerEmail(session().get("connected"));
		Collection<Course> course = ch.getCourseByCustomRule(fb, null, true, 1,
				10);

		return ok(trainercoursehistory.render(course));
	}

	@Transactional
	@Restrict({ @Group("TRAINER")})
	public static Result trainerbasicinfo() {
		UserHandler uh = new UserHandler();
		Trainer trainer = (Trainer) uh.getUserByEmail(session()
				.get("connected"));
		return ok(trainerbasicinfo.render(trainer));
	}

	@Transactional
	@Restrict({ @Group("TRAINER")})
	public static Result trainerbasicinfoedit() {
		UserHandler uh = new UserHandler();
		Trainer trainer = (Trainer) uh.getUserByEmail(session()
				.get("connected"));
		LocationHandler lh = new LocationHandler();
		List<String> stateList = LocationHandler.getStateList();

		return ok(trainerbasicinfoedit.render(trainer, stateList));
	}

	@Transactional
	@Restrict({ @Group("TRAINER")})
	public static Result trainerbasicinfoeditsubmit() {
		Form<TrainerForm> trainerForm = form(TrainerForm.class)
				.bindFromRequest();
		Logger.info(trainerForm.get().getName());
		IUserHandler uh = new UserHandler();
		uh.updateProfile(session().get("connected"), trainerForm.get());
		return redirect(routes.Application.trainerbasicinfo());
	}

	@Transactional
	@Restrict({ @Group("TRAINER")})
	public static Result trainerinfo() {
		UserHandler uh = new UserHandler();
		Trainer trainer = (Trainer) uh.getUserByEmail(session()
				.get("connected"));

		return ok(trainerinfo.render(trainer));
	}

	@Transactional
	@Restrict({ @Group("TRAINER")})
	public static Result trainerinfoedit() {
		UserHandler uh = new UserHandler();
		Trainer trainer = (Trainer) uh.getUserByEmail(session()
				.get("connected"));
		return ok(trainerinfoedit.render(trainer));
	}

	@Transactional
	@Restrict({ @Group("TRAINER")})
	public static Result trainerinfoeditsubmit() {
		Form<TrainerForm> trainerForm = form(TrainerForm.class)
				.bindFromRequest();
		Logger.info(trainerForm.get().getName());
		IUserHandler uh = new UserHandler();
		uh.updateProfile(session().get("connected"), trainerForm.get());
		return redirect(routes.Application.trainerinfo());
	}

	@Transactional
	@Restrict({ @Group("TRAINER")})
	public static Result traineraddcourse() {
		return ok(traineraddcourse.render());
	}

	@Transactional
	@Restrict({ @Group("TRAINER")})
	public static Result trainerchangepsw() {
		return ok(trainerchangepsw.render());
	}

	@Transactional
	@Restrict({ @Group("TRAINER")})
	public static Result traineraddcoursesubmit() {
		Form<CourseForm> courseForm = form(CourseForm.class).bindFromRequest();
		courseForm.get();
		Logger.info("papapalala");
		CourseHandler ch = new CourseHandler();
		ch.addNewCourse(session().get("connected"), courseForm.get(),
				new UserHandler());

		return redirect(routes.Application.trainercourseverifying());
	}

	@Transactional
	public static Result review(String orderId) {
		OrderHandler oh = new OrderHandler();
		CourseOrder courseOrder = oh.getCourseOrderByOrderId(orderId);
		return ok(review.render(courseOrder));
	}

	@Transactional
	public static Result reviewsubmit() {
		return TODO;
	}

	@Transactional
	public static Result concreteCourseDisplay() {
		return TODO;
	}

	
	@Transactional
	public static Result dashConcreteCourseRequest() {
		CourseHandler ch = new CourseHandler();
		//TODO get all the concreteCourse
		Collection<ConcreteCourse> concreteCourse = ch.getCourseById(4)
				.getCourses();
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

	//how to pass several dates and location
	@Transactional
	public static Result dashConcreteCourseRequestDetail(String concreteCourseId) {
		CourseHandler ch = new CourseHandler();
		ConcreteCourse concreteCourse = ch
				.getCourseByConcreteCourseId(concreteCourseId);
		ConcreteCourseForm ccf = ConcreteCourseForm
				.bindConcreteCourseForm(concreteCourse);
		System.out.print(Json.toJson(ccf));
		return ok(Json.toJson(ccf));
	}
	
	
	@Transactional
	public static Result courseDisplay() {
		return ok(Course_request.render());
	}
	
	@Transactional
	public static Result dashCourse(){
		CourseHandler ch = new CourseHandler();
		FilterBuilder fb = new CourseFilterBuilder();
		
		Collection<Course> course = ch.getCourseByCustomRule(fb, null, true, -1, -1);
		Collection<CourseForm> courseForm = new ArrayList<CourseForm>();
		for(Course c:course){
			Logger.info(c.getCourseName());
			CourseForm cf = CourseForm.bindCourseForm(c);
			courseForm.add(cf);
		}
		System.out.print(Json.toJson(courseForm));
		return ok(Json.toJson(courseForm));
	}
	
	@Transactional
	public static Result dashCourseDetail(String courseId){
		CourseHandler ch = new CourseHandler();
		Course course = ch.getCourseById(Integer.parseInt(courseId));
		CourseForm cf = CourseForm.bindCourseForm(course);
		System.out.print(Json.toJson(cf));
		return ok(Json.toJson(cf));
	}
	
	@Transactional
	public static Result dashCourseDelete(){
		JsonNode json = request().body().asJson();
		System.out.print(json);
		return TODO;
	}

	
//	@Transactional
//	public static Result trainerDisplay() {
//		return ok(User_trainer.render());
//	}
//	
//	@Transactional
//	public static Result dashTrainer(){
//		return ok(User_trainer.render());
//	}
	
	
	
	
	public static Result javascriptRoutes() {
		response().setContentType("text/javascript");
		return ok(Routes
				.javascriptRouter("jsRoutes", 
						routes.javascript.Application.dashConcreteCourseRequest(),
						routes.javascript.Application.dashConcreteCourseRequestDetail(),
						routes.javascript.Application.dashCourse(),
						routes.javascript.Application.dashCourseDetail(),
						routes.javascript.Application.dashCourseDelete()
						));
	}

}