package br.com.joaopedroafluz.notificationservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "rabbit")
public class RabbitMQProperties {

    private String queue;
    private String exchange;
    private String routingKey;

}
