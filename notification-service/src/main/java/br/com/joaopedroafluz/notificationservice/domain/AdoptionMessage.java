package br.com.joaopedroafluz.notificationservice.domain;

import java.util.UUID;

public record AdoptionMessage(
        UUID petId,
        String petName,
        UserDTO user
) {
}