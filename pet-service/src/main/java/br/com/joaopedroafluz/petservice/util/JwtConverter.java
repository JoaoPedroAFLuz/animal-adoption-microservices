package br.com.joaopedroafluz.petservice.util;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@SuppressWarnings("unchecked")
public class JwtConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private static final String ROLES = "roles";

    private final String clientName;

    public JwtConverter(String clientName) {
        this.clientName = clientName;
    }

    @Override
    public AbstractAuthenticationToken convert(@NonNull Jwt jwt) {
        final var authorities = extractAuthorities(jwt);
        return new JwtAuthenticationToken(jwt, authorities);
    }

    private List<SimpleGrantedAuthority> extractAuthorities(Jwt jwt) {
        final var roles = new ArrayList<String>();

        final var realmAccess = (Map<String, Object>) jwt.getClaim("realm_access");
        if (Objects.nonNull(realmAccess) && realmAccess.containsKey(ROLES)) {
            roles.addAll((List<String>) realmAccess.get(ROLES));
        }

        final var resourceAccess = (Map<String, Object>) jwt.getClaim("resource_access");
        if (Objects.nonNull(resourceAccess) && resourceAccess.containsKey(clientName)) {
            final var clientRoles = (Map<String, Object>) resourceAccess.get(clientName);
            roles.addAll((List<String>) clientRoles.get(ROLES));
        }

        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_%s".formatted(role)))
                .toList();
    }

}
