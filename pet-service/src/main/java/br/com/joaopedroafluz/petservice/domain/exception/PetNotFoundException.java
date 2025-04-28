package br.com.joaopedroafluz.petservice.domain.exception;

public class PetNotFoundException extends EntityNotFoundException {

    public PetNotFoundException(String message) {
        super(message);
    }

    public PetNotFoundException(Long petId) {
        this(String.format("Pet with id %d not found.", petId));
    }

}
