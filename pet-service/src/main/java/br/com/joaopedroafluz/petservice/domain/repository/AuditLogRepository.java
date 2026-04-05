package br.com.joaopedroafluz.petservice.domain.repository;

import br.com.joaopedroafluz.petservice.domain.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, UUID> {
}
