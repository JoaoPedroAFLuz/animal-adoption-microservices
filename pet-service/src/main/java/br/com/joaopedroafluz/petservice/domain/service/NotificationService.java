package br.com.joaopedroafluz.petservice.domain.service;

import br.com.joaopedroafluz.petservice.config.RabbitProperties;
import br.com.joaopedroafluz.shared.domain.AdoptionMessage;
import br.com.joaopedroafluz.shared.domain.PetDeletedMessage;
import br.com.joaopedroafluz.shared.domain.PetRegisteredMessage;
import br.com.joaopedroafluz.shared.util.JsonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final MessageProducer messageProducer;
    private final RabbitProperties rabbitProperties;

    public void sendAdoptionNotification(AdoptionMessage adoptionMessage) {
        send(rabbitProperties.getAdoptedRoutingKey(), adoptionMessage);
    }

    public void sendRegisteredNotification(PetRegisteredMessage petRegisteredMessage) {
        send(rabbitProperties.getRegisteredRoutingKey(), petRegisteredMessage);
    }

    public void sendDeletedNotification(PetDeletedMessage petDeletedMessage) {
        send(rabbitProperties.getDeletedRoutingKey(), petDeletedMessage);
    }

    private void send(String routingKey, Object payload) {
        final var message = new Message(JsonUtils.toJson(payload).getBytes());

        messageProducer.sendMessage(rabbitProperties.getExchange(), routingKey, message);
    }

}
