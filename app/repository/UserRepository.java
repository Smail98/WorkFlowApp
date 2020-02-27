package repository;

import com.google.common.base.Preconditions;
import model.User;
import org.mindrot.jbcrypt.BCrypt;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class UserRepository {

    private final Map<String, User> repo;
    private final SessionRepository sessionRepository;
    private final String salt;

    public static final UserRepository instance = new UserRepository();

    private UserRepository() {
        this.repo = new HashMap<>();
        this.salt = BCrypt.gensalt();
        this.sessionRepository = SessionRepository.instance;
    }

    public User create(String name, String password) {
        final Optional<User> maybeUser = Optional.ofNullable(repo.get(name));
        Preconditions.checkArgument(!maybeUser.isPresent(), "duplicated username");

        final User newUser = new User(name, hash(password));
        repo.put(name, newUser);
        return newUser;
    }

    public boolean login(String name, String password) {
        return Optional.ofNullable(repo.get(name))
                .map(u -> {
                    boolean validPassword = compare(password, u.getPassword());
                    if (validPassword) sessionRepository.login(u);
                    return validPassword;
                }).orElse(false);
    }

    public void logout(String name) {
        this.sessionRepository.logout(name);
    }

    public boolean isLoggedIn(String name) { return this.sessionRepository.isLoggedIn(name); }

    private String hash(String value) {
        return BCrypt.hashpw(value, salt);
    }

    private boolean compare(String password, String hashed) {
        return hash(password).equals(hashed);
    }

}
