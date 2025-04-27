package br.com.joaopedroafluz.petservice.domain.repository;

import br.com.joaopedroafluz.petservice.domain.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
}
