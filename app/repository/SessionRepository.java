package repository;

import com.google.common.base.Preconditions;
import model.Session;
import model.User;
import org.apache.commons.lang3.StringUtils;
import play.Play;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Created by svinci on 06/12/15.
 */
class SessionRepository {

    private final Map<String, Session> repo;
    private final long ttl;

    public static final SessionRepository instance = new SessionRepository();

    private SessionRepository() {
        this.repo = new HashMap<>();
        this.ttl = Play.application().configuration().getLong("user.inactivity.max");
    }

    public boolean isLoggedIn(String name) {
        final Session session = repo.get(name);
        final boolean loggedIn = Optional.ofNullable(session).map(s -> {
            Date now = new Date();
            final long inactivityPeriod = now.getTime() - s.getLastSeen().getTime();
            return inactivityPeriod < ttl;
        }).orElse(false);

        if (!loggedIn) repo.remove(name);
        else repo.put(name, new Session(session.getUser(), session.getLoggedIn(), new Date()));

        return loggedIn;
    }

    public void login(User user) {
        Preconditions.checkArgument(!isLoggedIn(user.getName()), "user is already logged in");
        final Date now = new Date();
        repo.put(user.getName(), new Session(user, now, now));
    }

    public void logout(String name) {
        repo.remove(name);
    }

}
