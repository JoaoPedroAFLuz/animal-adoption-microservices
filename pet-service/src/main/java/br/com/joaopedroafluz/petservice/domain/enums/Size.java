package br.com.joaopedroafluz.petservice.domain.enums;

import lombok.Getter;

@Getter
public enum Size {

    SMALL("Small"),
    MEDIUM("Medium"),
    LARGE("Large");

    private final String description;

    Size(String description) {
        this.description = description;
    }

}
