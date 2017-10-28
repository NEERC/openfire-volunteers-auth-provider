# openfire-volunteers-auth-provider
Openfire auth provider and authorization mapping for Volunteers

## Building
```
./gradlew build
```

## Installation

1. Copy the `openfire-volunteers-auth-provider.jar` into the Openfire `lib` directory
   ```
   cp build/libs/openfire-volunteers-auth-provider.jar /usr/lib/openfire/
   ```
2. Configure Openfire to use Volunteers auth provider and mapping

   | Property | Value |
   | -------- | ----- |
   | `hybridAuthProvider.primaryProvider.className`   | `ru.ifmo.neerc.volunteers.openfire.VolunteersAuthProvider` |
   | `hybridAuthProvider.secondaryProvider.className` | `org.jivesoftware.openfire.auth.DefaultAuthProvider` |
   | `provider.auth.className`                        | `org.jivesoftware.openfire.auth.HybridAuthProvider` |
   | `provider.authorization.classList`               | `ru.ifmo.neerc.volunteers.openfire.VolunteersAuthorizationPolicy org.jivesoftware.openfire.auth.DefaultAuthorizationPolicy` |
   | `provider.authorizationMapping.classList`        | `ru.ifmo.neerc.volunteers.openfire.VolunteersAuthorizationMapping org.jivesoftware.openfire.auth.DefaultAuthorizationMapping` |

   This is an example of fail-safe configuration.
   Should the Volunteers API become inaccessible, Openfire will continue to use default auth provider and authorization mapping.
   
3. Restart Openfire

## Configuration

| Property | Description | Default |
| -------- | ----------- | ------- |
| `volunteers.url`   | Volunteers URL | http://localhost:8080 |
| `volunteers.token` | Volunteers API Token | |
| `volunteers.year`  | Year ID | 1 |
| `volunteers.day`   | Day ID | 1 |

Openfire must be restarted after any configuration changes
