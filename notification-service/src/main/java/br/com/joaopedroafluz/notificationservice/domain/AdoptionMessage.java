package br.com.joaopedroafluz.notificationservice.domain;

public record AdoptionMessage(
        Long petId,
        String petName,
        Long userId,
        String userEmail
) {
}