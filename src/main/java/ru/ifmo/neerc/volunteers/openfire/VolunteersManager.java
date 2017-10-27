package ru.ifmo.neerc.volunteers.openfire;

import java.util.Collections;
import java.util.List;

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

import org.xmpp.packet.JID;

class VolunteersManager {
    private static final Logger LOG = LoggerFactory.getLogger(VolunteersManager.class);

    private static volatile VolunteersManager INSTANCE = null;

    private final WebTarget target;
    private final int year;
    private final int day;

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
        final String url = JiveGlobals.getProperty("volunteers.url", "http://localhost:8080");
        final String token = JiveGlobals.getProperty("volunteers.token");
        year = JiveGlobals.getIntProperty("volunteers.year", 1);
        day = JiveGlobals.getIntProperty("volunteers.day", 1);

        Client client = ClientBuilder.newClient();
        client.register(MOXyJsonProvider.class);
        client.register(OAuth2ClientSupport.feature(token));

        target = client.target(url + "/api");

        LOG.info("Initialized with year {} day {}", year, day);
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
}
