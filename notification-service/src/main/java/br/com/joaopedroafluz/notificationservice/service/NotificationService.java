package br.com.joaopedroafluz.notificationservice.service;

import br.com.joaopedroafluz.notificationservice.config.NotificationProperties;
import br.com.joaopedroafluz.shared.domain.AdoptionMessage;
import br.com.joaopedroafluz.shared.domain.PetDeletedMessage;
import br.com.joaopedroafluz.shared.domain.PetRegisteredMessage;
import br.com.joaopedroafluz.shared.util.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.Map;

@Log4j2
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final EmailService emailService;
    private final NotificationProperties notificationProperties;

    @RabbitListener(queues = {"${rabbit.adopted.queue}"})
    public void handleAdoptionMessage(@Payload String message) {
        log.info("Received adoption message: {}", message);

        final var adoptionMessage = JsonUtils.fromJson(message, AdoptionMessage.class);

        emailService.sendHtml(
                adoptionMessage.user().email(),
                "Adoption confirmation",
                "adoption-email",
                Map.of("userName", adoptionMessage.user().name(), "petName", adoptionMessage.petName())
        );

        log.info("Adoption email sent to {} for pet {}", adoptionMessage.user().email(), adoptionMessage.petName());
    }

    @RabbitListener(queues = {"${rabbit.registered.queue}"})
    public void handleRegisteredMessage(@Payload String message) {
        log.info("Received pet registered message: {}", message);

        final var petRegisteredMessage = JsonUtils.fromJson(message, PetRegisteredMessage.class);

        emailService.sendHtml(
                notificationProperties.getAdminEmail(),
                "New pet registered",
                "pet-registered-email",
                Map.of(
                        "petName", petRegisteredMessage.petName(),
                        "specie", petRegisteredMessage.specie(),
                        "breed", petRegisteredMessage.breed()
                )
        );

        log.info("Pet registered email sent to admin for pet {}", petRegisteredMessage.petName());
    }

    @RabbitListener(queues = {"${rabbit.deleted.queue}"})
    public void handleDeletedMessage(@Payload String message) {
        log.info("Received pet deleted message: {}", message);

        final var petDeletedMessage = JsonUtils.fromJson(message, PetDeletedMessage.class);

        emailService.sendHtml(
                notificationProperties.getAdminEmail(),
                "Pet deleted",
                "pet-deleted-email",
                Map.of("petName", petDeletedMessage.petName())
        );

        log.info("Pet deleted email sent to admin for pet {}", petDeletedMessage.petName());
    }

}
