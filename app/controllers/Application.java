	package controllers;
	
	import models.Customer;
	import models.User;
	import play.*;
	import play.data.Form;
	import play.db.jpa.JPA;
	import play.db.jpa.Transactional;
	import play.mvc.*;
	import views.html.*;
	import static play.data.Form.form;
	
	public class Application extends Controller {
		
		//for logging
		public static Logger LOG = new Logger();
		
		public static Form<Customer> signupForm = form(Customer.class);
	
		
		public static Result blank() {
			return ok(signup.render(signupForm));
		}
		
		@Transactional
		public static Result submit(){
			Form<Customer> filledForm = signupForm.bindFromRequest();
			LOG.info(filledForm.toString());
			LOG.info("Username: " + filledForm.field("username").value());
			
			//instantiate User entity
			Customer created = filledForm.get();
			LOG.info("User:" + created.getUsername());
			LOG.info("psw:" + created.getPassword());
			LOG.info("email:" + created.getEmail());
			
			Customer newcus = Customer.create(created.getEmail(), created.getUsername(), created.getPassword());
			
			session("email", newcus.getEmail());
			LOG.info("sessionUser:" + newcus.getEmail());
			
	        return redirect(
	        		//return to home page
	            routes.Application.welcome()
	        );
		}
		
		@Transactional
		public static Result index() {
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
