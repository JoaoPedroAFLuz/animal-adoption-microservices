package br.com.joaopedroafluz.petservice.domain.exception;

public class UnauthorizedException extends BusinessException {

    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException() {
        this("You are not authorized to perform this operation.");
    }

}
