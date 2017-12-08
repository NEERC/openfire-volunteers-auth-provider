package ru.ifmo.neerc.volunteers.openfire;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;

import org.eclipse.persistence.jaxb.rs.MOXyJsonProvider;

import org.glassfish.jersey.client.oauth2.OAuth2ClientSupport;

import org.jivesoftware.util.JiveGlobals;
import org.jivesoftware.util.PropertyEventDispatcher;
import org.jivesoftware.util.PropertyEventListener;

import org.xmpp.packet.JID;

class VolunteersManager implements PropertyEventListener {
    private static final Logger LOG = LoggerFactory.getLogger(VolunteersManager.class);

    private static volatile VolunteersManager INSTANCE = null;

    private String url;
    private String token;
    private int year;
    private int day;

    private final Client client;
    private WebTarget target;

    public static VolunteersManager getInstance() {
        if (INSTANCE == null) {
            synchronized(VolunteersManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new VolunteersManager();
                }
            }
        }
        return INSTANCE;
    }

    private VolunteersManager() {
        url = JiveGlobals.getProperty("volunteers.url", "http://localhost:8080");
        token = JiveGlobals.getProperty("volunteers.token");
        year = JiveGlobals.getIntProperty("volunteers.year", 0);
        day = JiveGlobals.getIntProperty("volunteers.day", 0);

        client = ClientBuilder.newClient();
        client.register(MOXyJsonProvider.class);

        updateTarget();

        PropertyEventDispatcher.addListener(this);

        LOG.info("Initialized with year {} day {}", year, day);
    }

    private void updateTarget() {
        target = client.target(url + "/api");
        target.register(OAuth2ClientSupport.feature(token));
    }

    private void setUrl(String url) {
        this.url = url;
        updateTarget();
        LOG.info("URL set to {}", this.url);
    }

    private void setToken(String token) {
        this.token = token;
        updateTarget();
        LOG.info("Token updated");
    }

    private void setYear(int year) {
        this.year = year;
        LOG.info("Year set to {}", this.year);
    }

    private void setDay(int day) {
        this.day = day;
        LOG.info("Day set to {}", this.day);
    }

    public boolean authenticate(String username, String password) {
        MultivaluedMap<String, String> params = new MultivaluedHashMap<>();
        params.add("username", JID.unescapeNode(username));
        params.add("password", password);

        try {
            target.path("/auth")
                  .request(MediaType.APPLICATION_JSON)
                  .post(Entity.form(params), Void.class);
        } catch (NotFoundException e) {
            return false;
        } catch (WebApplicationException | ProcessingException e) {
            LOG.error("Error communicating with Volunteers", e);
            return false;
        }

        return true;
    }

    public List<Hall> getHalls() {
        try {
            return target.path("/halls")
                         .queryParam("year", year)
                         .request(MediaType.APPLICATION_JSON)
                         .get(new GenericType<List<Hall>>() {});
        } catch (NotFoundException e) {
            return Collections.emptyList();
        } catch (WebApplicationException | ProcessingException e) {
            LOG.error("Error communicating with Volunteers", e);
            return Collections.emptyList();
        }
    }

    public User getUser(String username) {
        try {
            return target.path("/user")
                         .queryParam("username", JID.unescapeNode(username))
                         .queryParam("day", day)
                         .request(MediaType.APPLICATION_JSON)
                         .get(User.class);
        } catch (NotFoundException e) {
            return null;
        } catch (WebApplicationException | ProcessingException e) {
            LOG.error("Error communicating with Volunteers", e);
            return null;
        }
    }

    @Override
    public void propertySet(String property, Map<String, Object> params) {
        String value = (String) params.get("value");

        if ("volunteers.url".equals(property)) {
            setUrl(value);
        } else if ("volunteers.token".equals(property)) {
            setToken(value);
        } else if ("volunteers.year".equals(property)) {
            try {
                setYear(Integer.parseInt(value));
            } catch (NumberFormatException e) {
                setYear(0);
            }
        } else if ("volunteers.day".equals(property)) {
            try {
                setDay(Integer.parseInt(value));
            } catch (NumberFormatException e) {
                setDay(0);
            }
        }
    }

    @Override
    public void propertyDeleted(String property, Map<String, Object> params) {
        if ("volunteers.url".equals(property)) {
            setUrl("http://localhost:8080");
        } else if ("volunteers.token".equals(property)) {
            setToken("");
        } else if ("volunteers.year".equals(property)) {
            setYear(0);
        } else if ("volunteers.day".equals(property)) {
            setDay(0);
        }
    }

    @Override
    public void xmlPropertySet(String property, Map<String, Object> params) {
        // ignore
    }

    @Override
    public void xmlPropertyDeleted(String property, Map<String, Object> params) {
        // ignore
    }
}
