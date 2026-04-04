package br.com.joaopedroafluz.shared.domain;

import java.util.UUID;

public record PetRegisteredMessage(
        UUID petId,
        String petName,
        String specie,
        String breed
) {

}
