package br.com.joaopedroafluz.notificationservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RabbitMQConfig {

    private final RabbitMQProperties rabbitMQProperties;

    @Bean
    public Queue petAdoptedQueue() {
        return new Queue(rabbitMQProperties.getQueue(), true);
    }

    @Bean
    public TopicExchange petExchange() {
        return new TopicExchange(rabbitMQProperties.getExchange());
    }

    @Bean
    public Binding binding(Queue petAdoptedQueue, TopicExchange petExchange) {
        return BindingBuilder
                .bind(petAdoptedQueue)
                .to(petExchange)
                .with(rabbitMQProperties.getRoutingKey());
    }

}

