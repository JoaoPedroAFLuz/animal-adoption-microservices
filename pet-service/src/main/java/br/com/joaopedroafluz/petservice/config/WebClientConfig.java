package br.com.joaopedroafluz.petservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${eureka.client.api-gateway-url}")
    private String apiGatewayUrl;

    @Bean
    @LoadBalanced
    public WebClient.Builder loadBalancedWebClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public WebClient apiGatewayClient(WebClient.Builder builder) {
        return builder
                .baseUrl(apiGatewayUrl)
                .build();
    }

}
