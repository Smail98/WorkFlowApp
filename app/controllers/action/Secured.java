package controllers.action;

import org.apache.commons.lang3.StringUtils;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import repository.UserRepository;

public class Secured extends Security.Authenticator {

    private final UserRepository repository;

    public Secured() {
        repository = UserRepository.instance;
    }

    @Override
    public String getUsername(Http.Context context) {
        final String userName = context.session().get("user");
        if (StringUtils.isNotBlank(userName) && repository.isLoggedIn(userName)) {
            return userName;
        } else {
            return null;
        }
    }

    @Override
    public Result onUnauthorized(Http.Context context) {
        return redirect("/login");
    }
}
