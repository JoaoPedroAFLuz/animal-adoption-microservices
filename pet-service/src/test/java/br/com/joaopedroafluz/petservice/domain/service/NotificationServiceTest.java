package br.com.joaopedroafluz.petservice.domain.service;

import br.com.joaopedroafluz.petservice.config.RabbitProperties;
import br.com.joaopedroafluz.shared.domain.AdoptionMessage;
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
        final var userId = UUID.randomUUID();
        final var petId = UUID.randomUUID();
        final var user = new UserDTO(userId, "João", "joao@email.com");
        final var adoptionMessage = new AdoptionMessage(petId, "Rex", user);

        final var exchange = "test.exchange";
        final var routingKey = "test.routing.key";

        when(rabbitProperties.getExchange()).thenReturn(exchange);
        when(rabbitProperties.getRoutingKey()).thenReturn(routingKey);

        notificationService.sendAdoptionNotification(adoptionMessage);

        final var expectedMessage = new Message(JsonUtils.toJson(adoptionMessage).getBytes());

        verify(messageProducer, times(1)).sendMessage(
                eq(exchange),
                eq(routingKey),
                argThat(msg -> Arrays.equals(msg.getBody(), expectedMessage.getBody()))
        );
    }
}
