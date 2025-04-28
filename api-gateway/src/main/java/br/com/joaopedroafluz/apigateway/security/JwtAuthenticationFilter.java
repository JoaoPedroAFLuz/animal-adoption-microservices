package br.com.joaopedroafluz.apigateway.security;

import br.com.joaopedroafluz.apigateway.config.AuthProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;

@Slf4j
@Component
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
public class JwtAuthenticationFilter implements GlobalFilter {

    private final AuthProperties authProperties;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(authProperties.getSecret().getBytes());
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        var path = exchange.getRequest().getURI().getPath();

        if (isPublicRoute(path)) {
            return chain.filter(exchange);
        }

        var authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return respondWithError(exchange, HttpStatus.UNAUTHORIZED, "Token is required.");
        }

        var token = authHeader.substring(7);

        try {
            var claimsJws = Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(token);

            var claims = claimsJws.getPayload();
            var email = claims.getSubject();
            var userId = claims.get("userId", Long.class);
            var role = claims.get("role", String.class);

            ServerWebExchange modifiedExchange = exchange.mutate()
                    .request(r -> r
                            .header("X-Auth-User-Id", String.valueOf(userId))
                            .header("X-Auth-User-Email", email)
                            .header("X-Auth-User-Role", role))
                    .build();

            return chain.filter(modifiedExchange);
        } catch (JwtException e) {
            log.warn("Token validation failed: {}", e.getMessage());
            return respondWithError(exchange, HttpStatus.FORBIDDEN, "Invalid token.");
        }
    }

    private boolean isPublicRoute(String path) {
        return authProperties.getPublicRoutes().stream()
                .anyMatch(path::startsWith);
    }

    private Mono<Void> respondWithError(ServerWebExchange exchange, HttpStatus status, String message) {
        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        ErrorResponse errorResponse = new ErrorResponse(message);

        byte[] bytes;
        try {
            bytes = objectMapper.writeValueAsBytes(errorResponse);
        } catch (Exception e) {
            log.error("Error serializing error response", e);
            bytes = ("{\"message\":\"Error when validating token\"}").getBytes();
        }

        var buffer = exchange.getResponse().bufferFactory().wrap(bytes);
        return exchange.getResponse().writeWith(Mono.just(buffer));
    }

    private record ErrorResponse(String message) {
    }

}
