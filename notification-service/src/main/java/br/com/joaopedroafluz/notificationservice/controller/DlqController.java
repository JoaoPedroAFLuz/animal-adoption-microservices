package br.com.joaopedroafluz.notificationservice.controller;

import br.com.joaopedroafluz.notificationservice.config.RabbitMQProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/dlq")
public class DlqController {

    private final RabbitTemplate rabbitTemplate;
    private final RabbitMQProperties properties;

    @PostMapping("/retry")
    public Map<String, Object> retryMessages(@RequestParam String queue) {
        var dlqName = queue + ".dlq";
        var routingKey = resolveRoutingKey(queue);
        var count = 0;

        Message message;

        while ((message = rabbitTemplate.receive(dlqName)) != null) {
            rabbitTemplate.send(properties.getExchange(), routingKey, message);
            count++;
            log.info("Reprocessed message from {}: {}", dlqName, new String(message.getBody()));
        }

        log.info("Reprocessed {} messages from {}", count, dlqName);

        return Map.of("queue", dlqName, "reprocessed", count);
    }

    private String resolveRoutingKey(String queue) {
        if (queue.equals(properties.getAdopted().getQueue())) {
            return properties.getAdopted().getRoutingKey();
        }

        if (queue.equals(properties.getRegistered().getQueue())) {
            return properties.getRegistered().getRoutingKey();
        }

        if (queue.equals(properties.getDeleted().getQueue())) {
            return properties.getDeleted().getRoutingKey();
        }

        throw new IllegalArgumentException("Unknown queue: " + queue);
    }

}
