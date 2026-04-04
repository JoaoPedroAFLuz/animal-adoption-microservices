package br.com.joaopedroafluz.petservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "rabbit")
public class RabbitProperties {

    private String exchange;
    private String adoptedRoutingKey;
    private String registeredRoutingKey;
    private String deletedRoutingKey;

}
