package br.com.joaopedroafluz.petservice.config;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class AuthenticatedUserUtils {

    public String getUserId() {
        return getClaim("sub");
    }

    public String getEmail() {
        return getClaim("email");
    }

    public String getGivenName() {
        return getClaim("given_name");
    }

    public String getClaim(String claimName) {
        var jwt = getJwt();
        if (jwt != null) {
            return jwt.getClaim(claimName);
        }
        return null;
    }

    public Jwt getJwt() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt;
        }
        return null;
    }
}