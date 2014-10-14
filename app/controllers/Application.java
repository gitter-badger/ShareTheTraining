package controllers;



import play.*;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.mvc.*;
import views.html.*;

public class Application extends Controller {
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
	
	public static Result course() {
		return ok(item_page.render());
	}

}
