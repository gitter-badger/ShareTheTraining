package controllers;


import com.typesafe.plugin.MailerAPI;
import com.typesafe.plugin.MailerPlugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import controllers.authentication.AuthenticationHandler;
import controllers.course.CourseHandler;
import controllers.course.OrderHandler;
import controllers.locations.GeolocationService;
import controllers.locations.LocationHandler;
import controllers.user.IMailHandler;
import controllers.user.IUserHandler;
import controllers.user.MailHandler;
import controllers.user.UserHandler;
import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import models.courses.Course;
import models.courses.CourseOrder;
import models.courses.CourseStatus;
import models.courses.OrderStatus;
import models.filters.CourseFilterBuilder;
import models.filters.DateFilterHandler;
import models.filters.OrderFilterBuilder;
import models.forms.CourseFilterForm;
import models.forms.CustomerForm;
import models.forms.LoginForm;
import models.forms.TrainerForm;
import models.locations.Geolocation;
import models.locations.Location;
import models.users.Customer;
import models.users.User;
import models.users.UserRole;
import play.*;
import play.data.Form;
import play.db.jpa.Transactional;
import play.libs.F.Function;
import play.libs.F.Function0;
import play.libs.F.Promise;
import play.mvc.*;
import play.mvc.Http.Context;
import play.mvc.Http.Request;
import play.mvc.Http.Response;
import views.html.*;
import views.html.searchview.*;
import views.html.signup.*;
import views.html.customerprofile.*;
import views.html.login.*;
import views.html.itempage.*;
import views.html.trainerprofile.*;
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
				2, 2);
		Logger.info("course" + course.size());
		return ok(home.render(course));

	}
	
	public static Result addCourse(){
		return TODO;
	}
	
	@Transactional
	public static Result welcome() {
		CourseHandler ch = new CourseHandler();
		CourseFilterBuilder cfb = new CourseFilterBuilder();
//		Collection<Course> course = ch.getCourseByCustomRule(cfb, "popularity",
//				2, 2);
		Collection<Course> course = ch.getCourseByCustomRule(cfb,
				null, 1, 10);
	
		Logger.info("course" + course.size());
		return ok(home.render(course));

	}
	
	
	public static Result login(){
		return ok(login.render());
	}
	
	@Transactional
	public static Result loginAuthen(){
		Form<LoginForm> loginForm = form(LoginForm.class).bindFromRequest();
		Logger.info(loginForm.get().getEmail());
		AuthenticationHandler ah = new AuthenticationHandler();
		IUserHandler userHandler = new UserHandler();
		User u =ah.doLogin(loginForm.get().getEmail(), loginForm.get().getPassword(), Context.current(), userHandler);
		if(u!=null){
			
			return redirect(routes.Application.welcome());
		}
		flash("error", "Username or Password is incorrect");
		return ok(login.render());
	}
	
	@Transactional
	public static Result activate(){
		AuthenticationHandler ah = new AuthenticationHandler();
//		ah.activateUser(token, userHandler);
		return TODO;
	}
	
	@Transactional
	public static Result resetpsw(){
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
		if(u!=null){
			AuthenticationHandler ah = new AuthenticationHandler();
			IMailHandler mh = new MailHandler();
			ah.authorizeResetPassword(loginForm.get().getUsername(), loginForm.get().getEmail(), mh);
			return ok(forgetpswconfirm.render());
		}
		flash("error", "email doesn't exist!!!");
		return ok(forgetpsw.render());
		
	}
	
	@Transactional
	public static Result logout(){
		AuthenticationHandler ah = new AuthenticationHandler();
		ah.doLogout(Context.current());
		return redirect(routes.Application.welcome());
	}
	

	@Transactional
	public static Result search() {
		Form<CourseFilterForm> filterForm = form(CourseFilterForm.class)
				.bindFromRequest();
		Logger.info("keyword" + filterForm.get().getCfb().getKeyword());
		
		int datec = filterForm.get().getCfb().getDataChoice();
		DateFilterHandler dfh = new DateFilterHandler();
		filterForm=dfh.transferChoiceToRange(datec, filterForm);
		
		CourseHandler ch = new CourseHandler();
		Collection<Course> course = ch.getCourseByCustomRule(filterForm.get().getCfb()
				, null, 1, 10);
		Logger.info("course" + course.size());
		return ok(searchindex.render(course));
	}
	
	
	
	@Transactional
	public static Result signupcussubmit() {
		Form<CustomerForm> cusForm = form(CustomerForm.class).bindFromRequest();
		AuthenticationHandler ah = new AuthenticationHandler();
		IUserHandler uh = new UserHandler();
		IMailHandler mh = new MailHandler();
		ah.doRegister(cusForm.get().getEmail(), cusForm.get().getName(), cusForm.get().getPassword(), 
				UserRole.values()[1], uh, mh);

		return ok(signupemail.render());
	}
	
	
	@Transactional
	public static Result signupcus() {
		Logger.info(session().get("connected"));
		return ok(customersignup.render());
	}
	
	@Transactional
	public static Result signuptrainer() {
		return ok(trainersignup.render());
	}
	
	public static Result signuptrainersubmit(){
		Form<TrainerForm> trainerForm = form(TrainerForm.class).bindFromRequest();
		AuthenticationHandler ah = new AuthenticationHandler();
		IUserHandler uh = new UserHandler();
		IMailHandler mh = new MailHandler();
		ah.doRegister(trainerForm.get().getEmail(), trainerForm.get().getName(), trainerForm.get().getPassword(),
				UserRole.values()[2], uh, mh);
		return ok(signupmail.render());
	}

	@Transactional
	public static Result cusprofile() {
		OrderHandler oh = new OrderHandler();
		Collection<CourseOrder> order = oh.getCourseOrderByCustomer(session().get("connected"));
		System.out.print(order.size());
		return ok(cuscoursehistory.render(order));
	}

	@Transactional
	public static Result cuscourseconfirmed() {
		OrderHandler oh = new OrderHandler();
		OrderFilterBuilder fb = new OrderFilterBuilder();
		fb.setOrderStatus(OrderStatus.CONFIRMED.ordinal());
		fb.setUserEmail(session().get("connected"));
		Collection<CourseOrder> order = 
				oh.getCourseOrderByCustomRule(fb, null, 1, 10);
		return ok(cuscoursehistory.render(order));
	}

	@Transactional
	public static Result cuscourseordered() {
		OrderHandler oh = new OrderHandler();
		OrderFilterBuilder fb = new OrderFilterBuilder();
		fb.setOrderStatus(OrderStatus.ORDERED.ordinal());
		fb.setUserEmail(session().get("connected"));
		Collection<CourseOrder> order = 
				oh.getCourseOrderByCustomRule(fb, null, -1, -1);
		return ok(cuscoursehistory.render(order));
	}
	
	@Transactional
	public static Result cuscoursedone() {
		OrderHandler oh = new OrderHandler();
		OrderFilterBuilder fb = new OrderFilterBuilder();
		fb.setOrderStatus(OrderStatus.DONE.ordinal());
		fb.setUserEmail(session().get("connected"));
		Collection<CourseOrder> order = 
				oh.getCourseOrderByCustomRule(fb, null, -1, -1);
		return ok(cuscoursehistory.render(order));
	}

	@Transactional
	public static Result cuscoursecanceled() {
		OrderHandler oh = new OrderHandler();
		OrderFilterBuilder fb = new OrderFilterBuilder();
		fb.setOrderStatus(OrderStatus.CANCELLED.ordinal());
		fb.setUserEmail(session().get("connected"));
		Collection<CourseOrder> order = 
				oh.getCourseOrderByCustomRule(fb, null, -1, -1);
		return ok(cuscoursehistory.render(order));
	}

	@Transactional
	public static Result cusinfo() {
		UserHandler uh = new UserHandler();
		Customer customer = (Customer) uh.getUserByEmail(session().get("connected"));
		return ok(cusinfo.render(customer));
	}

	@Transactional
	public static Result cusinfoedit() {
		UserHandler uh = new UserHandler();
		Customer customer = (Customer) uh.getUserByEmail(session().get("connected"));
		LocationHandler lh = new LocationHandler();
		List<String> stateList = lh.getStateList();
		
		return ok(cusinfoedit.render(customer, stateList));
	}
	
	
	@Transactional
	public static Result cusinfoeditsubmit(){
		Form<CustomerForm> cusForm = form(CustomerForm.class).bindFromRequest();
		IUserHandler uh = new UserHandler();	
		return redirect(routes.Application.cusinfo());
	}

	@Transactional
	public static Result cuschangepsw() {
		return ok(cuschangepsw.render());
	}

	@Transactional
	public static Result itempage(Integer id){
		CourseHandler ch = new CourseHandler();
		Course course=ch.getCourseById(id);
		Collection<Course> similarcourse = ch.getCourseByCategory(course.getCourseCategory(), 1, 3);
		
	
//		Collection<ConcreteCourse> cc=c.getCourses();
//		
//		cc.iterator().next().getMaximum()
		
		
		return ok(itempage.render(course));
	}

	@Transactional
	public static Result showCity(){
		
		String stateName = form().bindFromRequest().get("name");
		System.out.print(stateName);
		if(stateName!=null){
			
			List<String> cityList = LocationHandler.getLocationByStateName(stateName);
			System.out.print(cityList.iterator().next());
			JSONArray jsonArray=JSONArray.fromObject(cityList);
			return ok(jsonArray.toString());
			
		}
		return null;
	}
	
	@Transactional
	public static Result trainerprofile(){
		CourseHandler ch = new CourseHandler();
		Collection<Course> course = ch.getCourseByTrainer(session().get("connected"), 1, 10);
		return ok(trainercoursehistory.render(course));
	}
	
	@Transactional
	public static Result trainercourseapproved(){
	
		CourseHandler ch = new CourseHandler();
		CourseFilterBuilder fb = new CourseFilterBuilder();
		fb.setCourseStatus(CourseStatus.APPROVED.ordinal());
		Collection<Course> course = ch.getCourseByCustomRule(fb, null, 1, 10);
		return ok(trainercoursehistory.render(course));
	}
	
	@Transactional
	public static Result trainercoursepending(){
		return TODO;
	}
	
	@Transactional
	public static Result trainercoursecompleted(){
		return TODO;
	}
	
	@Transactional
	public static Result trainercoursecanceled(){
		return TODO;
	}

}