package br.com.joaopedroafluz.petservice.domain.exception;

public class PetAlreadyAdoptedException extends BusinessException {

    public PetAlreadyAdoptedException(String message) {
        super(message);
    }

    public PetAlreadyAdoptedException() {
        this("Pet already adopted.");
    }

}
