package br.com.joaopedroafluz.petservice.domain.exception;

public abstract class EntityNotFoundException extends BusinessException {

    public EntityNotFoundException(String message) {
        super(message);
    }

}
