package ru.ifmo.neerc.volunteers.openfire;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jivesoftware.openfire.auth.AuthorizationMapping;

public class VolunteersAuthorizationMapping implements AuthorizationMapping {
    private static final Logger LOG = LoggerFactory.getLogger(VolunteersAuthorizationMapping.class);

    private VolunteersManager manager;

    public VolunteersAuthorizationMapping() {
        manager = VolunteersManager.getInstance();
    }

    @Override
    public String map(String principal) {
        User user = manager.getUser(principal);
        if (user != null) {
            LOG.debug("Mapping {} to {}", principal, user.getHall());
            return user.getHall();
        }
        return principal;
    }

    @Override
    public String name() {
        return "Volunteers Mapping";
    }

    @Override
    public String description() {
        return "Maps volunteer to a hall";
    }
}
