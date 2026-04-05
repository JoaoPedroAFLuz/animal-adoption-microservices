package br.com.joaopedroafluz.petservice.domain.model;

import br.com.joaopedroafluz.petservice.domain.enums.AuditAction;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "audit_log")
public class AuditLog {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AuditAction action;

    @Column(name = "entity_id", nullable = false)
    private UUID entityId;

    @Column(name = "user_id")
    private String userId;

    private String details;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

}
