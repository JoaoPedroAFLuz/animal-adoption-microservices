package br.com.joaopedroafluz.petservice.domain.service;

import br.com.joaopedroafluz.petservice.config.RabbitProperties;
import br.com.joaopedroafluz.petservice.domain.dto.AdoptionMessage;
import br.com.joaopedroafluz.petservice.util.JsonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final MessageProducer messageProducer;
    private final RabbitProperties rabbitProperties;

    public void sendAdoptionNotification(AdoptionMessage adoptionMessage) {
        var message = new Message(JsonUtils.toJson(adoptionMessage).getBytes());

        messageProducer.sendMessage(rabbitProperties.getExchange(), rabbitProperties.getRoutingKey(), message);
    }

}
