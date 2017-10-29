package ru.ifmo.neerc.volunteers.openfire;

import java.util.Set;
import java.util.LinkedHashSet;

import org.jivesoftware.openfire.auth.AuthProvider;
import org.jivesoftware.openfire.auth.AuthProviderMapper;
import org.jivesoftware.openfire.auth.DefaultAuthProvider;
import org.jivesoftware.util.ClassUtils;

import org.xmpp.packet.JID;

public class VolunteersAuthProviderMapper implements AuthProviderMapper {

    private final VolunteersManager manager;

    private final VolunteersAuthProvider volunteersAuthProvider;
    private final DefaultAuthProvider defaultAuthProvider;

    public VolunteersAuthProviderMapper() {
        manager = VolunteersManager.getInstance();

        volunteersAuthProvider = new VolunteersAuthProvider();
        defaultAuthProvider = new DefaultAuthProvider();
    }

    @Override
    public AuthProvider getAuthProvider(String username) {
        if (JID.unescapeNode(username).contains("@") && manager.getUser(username) != null) {
            return volunteersAuthProvider;
        } else {
            return defaultAuthProvider;
        }
    }

    @Override
    public Set<AuthProvider> getAuthProviders() {
        final Set<AuthProvider> result = new LinkedHashSet<>();
        result.add(volunteersAuthProvider);
        result.add(defaultAuthProvider);

        return result;
    }
}
