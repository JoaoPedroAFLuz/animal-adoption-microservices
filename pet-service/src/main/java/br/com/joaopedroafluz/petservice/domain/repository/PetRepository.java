package br.com.joaopedroafluz.petservice.domain.repository;

import br.com.joaopedroafluz.petservice.domain.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {

    List<Pet> findByOwnerId(Long ownerId);

}
