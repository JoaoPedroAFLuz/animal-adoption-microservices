package br.com.joaopedroafluz.petservice.domain.enums;

import lombok.Getter;

@Getter
public enum Status {

    AVAILABLE("Available"),
    ADOPTED("Adopted"),
    PENDING("Pending");

    private final String description;

    Status(String description) {
        this.description = description;
    }

}
