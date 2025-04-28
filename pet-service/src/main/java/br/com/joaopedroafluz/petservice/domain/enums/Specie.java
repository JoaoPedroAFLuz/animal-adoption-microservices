package br.com.joaopedroafluz.petservice.domain.enums;

import lombok.Getter;

@Getter
public enum Specie {

    DOG("Dog"),
    CAT("Cat");

    private final String description;

    Specie(String description) {
        this.description = description;
    }

}
