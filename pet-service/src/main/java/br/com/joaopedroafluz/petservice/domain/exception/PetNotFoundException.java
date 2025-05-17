package br.com.joaopedroafluz.petservice.domain.exception;

import java.util.UUID;

public class PetNotFoundException extends EntityNotFoundException {

    public PetNotFoundException(String message) {
        super(message);
    }

    public PetNotFoundException(UUID petId) {
        this(String.format("Pet with id %s not found.", petId));
    }

}
