package br.com.joaopedroafluz.notificationservice.controller;

import br.com.joaopedroafluz.notificationservice.config.RabbitMQProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public Map<String, Integer> retryMessages() {
        var dlqName = properties.getQueue() + ".dlq";
        var count = 0;

        Message message;

        while ((message = rabbitTemplate.receive(dlqName)) != null) {
            rabbitTemplate.send(properties.getExchange(), properties.getRoutingKey(), message);
            count++;
            log.info("Reprocessed message from DLQ: {}", new String(message.getBody()));
        }

        log.info("Reprocessed {} messages from DLQ", count);

        return Map.of("reprocessed", count);
    }

}
