package br.com.joaopedroafluz.petservice.domain.service;

import br.com.joaopedroafluz.petservice.domain.enums.AuditAction;
import br.com.joaopedroafluz.petservice.domain.model.AuditLog;
import br.com.joaopedroafluz.petservice.domain.model.Pet;
import br.com.joaopedroafluz.petservice.domain.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public void log(AuditAction action, Pet pet) {
        final var auditLog = AuditLog.builder()
                                     .action(action)
                                     .entityId(pet.getId())
                                     .userId(getCurrentUserId())
                                     .details(pet.getName() + " (" + pet.getSpecie() + ", " + pet.getBreed() + ")")
                                     .build();

        auditLogRepository.save(auditLog);
    }

    private String getCurrentUserId() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                       .filter(Authentication::isAuthenticated)
                       .map(Authentication::getPrincipal)
                       .filter(Jwt.class::isInstance)
                       .map(Jwt.class::cast)
                       .map(Jwt::getSubject)
                       .orElse(null);
    }

}
