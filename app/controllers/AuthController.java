package controllers;

import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import repository.UserRepository;
import views.html.login;
import views.html.signup;

import java.util.Objects;
import java.util.Optional;

public class AuthController extends Controller {

    private final UserRepository repository;

    public AuthController() {
        repository = UserRepository.instance;
    }

    public Result login() {
        return ok(login.render(Form.form(LoginForm.class)));
    }

    public Result logout() {
        repository.logout(session("user"));
        session().clear();
        return redirect(routes.AuthController.login());
    }

    public Result authenticate() {
        final Form<LoginForm> loginForm = Form.form(LoginForm.class).bindFromRequest();
        return Optional.ofNullable(loginForm.get())
                .map(f ->  {
                    if (repository.login(f.name, f.password)) {
                        session().put("user", f.name);
                        return redirect(routes.Application.index());
                    } else return redirect(routes.AuthController.login());
                }).orElse(redirect(routes.AuthController.login()));
    }

    public Result signup() {
        return ok(signup.render(Form.form(SignUpForm.class)));
    }

    public Result newUser() {
        return Optional.ofNullable(Form.form(SignUpForm.class).bindFromRequest().get()).map(f -> {
            if (Objects.equals(f.password, f.passwordConfirmation)) {
                repository.create(f.name, f.password);
                return redirect(routes.AuthController.login());
            } else {
                return redirect(routes.AuthController.signup());
            }
        }).orElse(redirect(routes.AuthController.signup()));
    }

}
