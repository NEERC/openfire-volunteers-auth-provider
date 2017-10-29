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
   | `mappedAuthProvider.mapper.className`            | `ru.ifmo.neerc.volunteers.openfire.VolunteersAuthProviderMapper` |
   | `provider.auth.className`                        | `org.jivesoftware.openfire.auth.MappedAuthProvider` |
   | `provider.authorization.classList`               | `ru.ifmo.neerc.volunteers.openfire.VolunteersAuthorizationPolicy org.jivesoftware.openfire.auth.DefaultAuthorizationPolicy` |
   | `provider.authorizationMapping.classList`        | `ru.ifmo.neerc.volunteers.openfire.VolunteersAuthorizationMapping org.jivesoftware.openfire.auth.DefaultAuthorizationMapping` |

   With this configuration Volunteers auth provider is used for Volunteers users only.
   Openfire users can be authenticated even is Volunteers API is inaccessible.
   
3. Restart Openfire

## Configuration

| Property | Description | Default |
| -------- | ----------- | ------- |
| `volunteers.url`   | Volunteers URL | http://localhost:8080 |
| `volunteers.token` | Volunteers API Token | |
| `volunteers.year`  | Year ID | 1 |
| `volunteers.day`   | Day ID | 1 |

Openfire must be restarted after any configuration changes
