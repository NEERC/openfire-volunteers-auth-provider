package ru.ifmo.neerc.volunteers.openfire;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jivesoftware.openfire.auth.AuthProvider;
import org.jivesoftware.openfire.auth.ConnectionException;
import org.jivesoftware.openfire.auth.InternalUnauthenticatedException;
import org.jivesoftware.openfire.auth.UnauthorizedException;
import org.jivesoftware.openfire.user.UserNotFoundException;

public class VolunteersAuthProvider implements AuthProvider {
    private static final Logger LOG = LoggerFactory.getLogger(VolunteersAuthProvider.class);

    private VolunteersManager manager;

    public VolunteersAuthProvider() {
        manager = VolunteersManager.getInstance();
    }

    @Override
    public void authenticate(String username, String password) throws UnauthorizedException, ConnectionException, InternalUnauthenticatedException {
        if (!manager.authenticate(username, password)) {
            LOG.debug("Failed to authenticate user {}", username);
            throw new UnauthorizedException();
        }
        LOG.debug("Authorized user {}", username);
    }

    @Override
    public String getPassword(String username) throws UserNotFoundException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setPassword(String username, String password) throws UserNotFoundException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean supportsPasswordRetrieval() {
        return false;
    }

    @Override
    public boolean isScramSupported() {
        return false;
    }

    @Override
    public String getSalt(String username) throws UnsupportedOperationException, UserNotFoundException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getIterations(String username) throws UnsupportedOperationException, UserNotFoundException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getServerKey(String username) throws UnsupportedOperationException, UserNotFoundException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getStoredKey(String username) throws UnsupportedOperationException, UserNotFoundException {
        throw new UnsupportedOperationException();
    }
}
