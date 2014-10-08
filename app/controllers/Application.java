package controllers;

import models.Course;
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

}
