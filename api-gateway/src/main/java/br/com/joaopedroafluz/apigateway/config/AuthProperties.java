package br.com.joaopedroafluz.apigateway.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "app.auth")
public class AuthProperties {

    private String secret;
    private List<String> publicRoutes;

}
