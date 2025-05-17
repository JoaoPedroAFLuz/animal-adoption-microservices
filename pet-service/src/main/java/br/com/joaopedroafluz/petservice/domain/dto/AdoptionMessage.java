package br.com.joaopedroafluz.petservice.domain.dto;

import java.util.UUID;

public record AdoptionMessage(
        UUID petId,
        String petName,
        UserDTO user
) {
}