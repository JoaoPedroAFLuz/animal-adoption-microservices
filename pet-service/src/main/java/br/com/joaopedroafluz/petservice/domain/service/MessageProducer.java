package br.com.joaopedroafluz.petservice.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendMessage(String exchange, String routingKey, Message message) {
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }

}
