package controllers;

import static play.data.Form.form;
import models.users.Customer;
import models.users.Trainer;
import models.users.User;
import play.Logger;
import play.i18n.Messages;
import play.data.DynamicForm;
import play.data.Form;
import play.data.validation.Constraints;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import views.html.login;

public class UserController extends Controller {
	
	public static Logger LOG = new Logger();
	
	public static Result authenticate() {
		//receive the value of email and password, as a Login class, validate them
	    Form<Login> loginForm = form(Login.class).bindFromRequest();
	    if(loginForm.hasErrors()){
	    	return badRequest(login.render(loginForm));
	    }else{
	    	session().clear();
	    	//create new session
	        session("email", loginForm.get().email);
	        return redirect(
	        		//return to home page
	            routes.Application.welcome()
	        );
	    }
	    
	}
	
	public static Result login() {
		return ok(
	            login.render(form(Login.class))
	        );
	}
	
	public static Result logout(){
		return TODO;
	}
	
	public static class Login{
		
		@Constraints.Required
		public String email;
		
		@Constraints.Required
		public String password;
		

			
	}
}
