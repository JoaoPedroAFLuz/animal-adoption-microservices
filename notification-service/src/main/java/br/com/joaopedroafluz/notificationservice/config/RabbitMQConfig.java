package br.com.joaopedroafluz.notificationservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RabbitMQConfig {

    private final RabbitMQProperties rabbitMQProperties;

    @Bean
    public Queue petAdoptedQueue() {
        return QueueBuilder
                .durable(rabbitMQProperties.getQueue())
                .withArgument("x-dead-letter-exchange", rabbitMQProperties.getExchange())
                .withArgument("x-dead-letter-routing-key", rabbitMQProperties.getRoutingKey() + ".dlq")
                .build();
    }

    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable(rabbitMQProperties.getQueue() + ".dlq").build();
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

    @Bean
    public Binding deadLetterBinding(Queue deadLetterQueue, TopicExchange petExchange) {
        return BindingBuilder
                .bind(deadLetterQueue)
                .to(petExchange)
                .with(rabbitMQProperties.getRoutingKey() + ".dlq");
    }

}
