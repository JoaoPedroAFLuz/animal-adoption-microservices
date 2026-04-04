package br.com.joaopedroafluz.notificationservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RabbitMQConfig {

    private final RabbitMQProperties properties;

    @Bean
    public TopicExchange petExchange() {
        return new TopicExchange(properties.getExchange());
    }

    // Adopted
    @Bean
    public Queue adoptedQueue() {
        return buildQueueWithDlq(properties.getAdopted());
    }

    @Bean
    public Queue adoptedDlq() {
        return buildDlq(properties.getAdopted());
    }

    @Bean
    public Binding adoptedBinding(Queue adoptedQueue, TopicExchange petExchange) {
        return bind(adoptedQueue, petExchange, properties.getAdopted().getRoutingKey());
    }

    @Bean
    public Binding adoptedDlqBinding(Queue adoptedDlq, TopicExchange petExchange) {
        return bind(adoptedDlq, petExchange, properties.getAdopted().getRoutingKey() + ".dlq");
    }

    // Registered
    @Bean
    public Queue registeredQueue() {
        return buildQueueWithDlq(properties.getRegistered());
    }

    @Bean
    public Queue registeredDlq() {
        return buildDlq(properties.getRegistered());
    }

    @Bean
    public Binding registeredBinding(Queue registeredQueue, TopicExchange petExchange) {
        return bind(registeredQueue, petExchange, properties.getRegistered().getRoutingKey());
    }

    @Bean
    public Binding registeredDlqBinding(Queue registeredDlq, TopicExchange petExchange) {
        return bind(registeredDlq, petExchange, properties.getRegistered().getRoutingKey() + ".dlq");
    }

    // Deleted
    @Bean
    public Queue deletedQueue() {
        return buildQueueWithDlq(properties.getDeleted());
    }

    @Bean
    public Queue deletedDlq() {
        return buildDlq(properties.getDeleted());
    }

    @Bean
    public Binding deletedBinding(Queue deletedQueue, TopicExchange petExchange) {
        return bind(deletedQueue, petExchange, properties.getDeleted().getRoutingKey());
    }

    @Bean
    public Binding deletedDlqBinding(Queue deletedDlq, TopicExchange petExchange) {
        return bind(deletedDlq, petExchange, properties.getDeleted().getRoutingKey() + ".dlq");
    }

    // Helpers
    private Queue buildQueueWithDlq(RabbitMQProperties.QueueConfig config) {
        return QueueBuilder.durable(config.getQueue())
                .withArgument("x-dead-letter-exchange", properties.getExchange())
                .withArgument("x-dead-letter-routing-key", config.getRoutingKey() + ".dlq")
                .build();
    }

    private Queue buildDlq(RabbitMQProperties.QueueConfig config) {
        return QueueBuilder.durable(config.getQueue() + ".dlq").build();
    }

    private Binding bind(Queue queue, TopicExchange exchange, String routingKey) {
        return BindingBuilder.bind(queue).to(exchange).with(routingKey);
    }

}
