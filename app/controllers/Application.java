package controllers;

import controllers.action.Secured;
import play.*;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {

    @Security.Authenticated(Secured.class)
    public Result index() {
        return ok(index.render(session("user")));
    }

}
