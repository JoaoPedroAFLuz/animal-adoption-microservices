package br.com.joaopedroafluz.shared.domain;

import java.util.UUID;

public record PetDeletedMessage(
        UUID petId,
        String petName
) {

}
