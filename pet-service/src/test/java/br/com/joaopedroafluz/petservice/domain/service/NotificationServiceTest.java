package br.com.joaopedroafluz.petservice.domain.service;

import br.com.joaopedroafluz.petservice.config.RabbitProperties;
import br.com.joaopedroafluz.shared.domain.AdoptionMessage;
import br.com.joaopedroafluz.shared.domain.PetDeletedMessage;
import br.com.joaopedroafluz.shared.domain.PetRegisteredMessage;
import br.com.joaopedroafluz.shared.domain.UserDTO;
import br.com.joaopedroafluz.shared.util.JsonUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.Message;

import java.util.Arrays;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private MessageProducer messageProducer;

    @Mock
    private RabbitProperties rabbitProperties;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    void shouldSendAdoptionNotification() {
        var adoptionMessage = new AdoptionMessage(UUID.randomUUID(), "Rex",
                new UserDTO(UUID.randomUUID(), "João", "joao@email.com"));

        when(rabbitProperties.getExchange()).thenReturn("pet.exchange");
        when(rabbitProperties.getAdoptedRoutingKey()).thenReturn("pet.adopted");

        notificationService.sendAdoptionNotification(adoptionMessage);

        verify(messageProducer, times(1)).sendMessage(
                eq("pet.exchange"),
                eq("pet.adopted"),
                argThat(msg -> Arrays.equals(msg.getBody(), new Message(JsonUtils.toJson(adoptionMessage).getBytes()).getBody()))
        );
    }

    @Test
    void shouldSendRegisteredNotification() {
        var message = new PetRegisteredMessage(UUID.randomUUID(), "Luna", "DOG", "Golden Retriever");

        when(rabbitProperties.getExchange()).thenReturn("pet.exchange");
        when(rabbitProperties.getRegisteredRoutingKey()).thenReturn("pet.registered");

        notificationService.sendRegisteredNotification(message);

        verify(messageProducer, times(1)).sendMessage(
                eq("pet.exchange"),
                eq("pet.registered"),
                argThat(msg -> Arrays.equals(msg.getBody(), new Message(JsonUtils.toJson(message).getBytes()).getBody()))
        );
    }

    @Test
    void shouldSendDeletedNotification() {
        var message = new PetDeletedMessage(UUID.randomUUID(), "Luna");

        when(rabbitProperties.getExchange()).thenReturn("pet.exchange");
        when(rabbitProperties.getDeletedRoutingKey()).thenReturn("pet.deleted");

        notificationService.sendDeletedNotification(message);

        verify(messageProducer, times(1)).sendMessage(
                eq("pet.exchange"),
                eq("pet.deleted"),
                argThat(msg -> Arrays.equals(msg.getBody(), new Message(JsonUtils.toJson(message).getBytes()).getBody()))
        );
    }

}
