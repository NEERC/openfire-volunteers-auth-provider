package ru.ifmo.neerc.volunteers.openfire;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jivesoftware.openfire.auth.AuthorizationPolicy;

public class VolunteersAuthorizationPolicy implements AuthorizationPolicy {
    private static final Logger LOG = LoggerFactory.getLogger(VolunteersAuthorizationPolicy.class);

    private VolunteersManager manager;

    public VolunteersAuthorizationPolicy() {
        manager = VolunteersManager.getInstance();
    }

    @Override
    public boolean authorize(String username, String principal) {
        User user = manager.getUser(principal);
        if (user != null && user.getHall().equals(username)) {
            LOG.debug("Authorized {} to be {}", principal, username);
            return true;
        }
        return false;
    }

    @Override
    public String name() {
        return "Volunteers Policy";
    }

    @Override
    public String description() {
        return "Authorize volunteer to the hall it belongs to";
    }
}
