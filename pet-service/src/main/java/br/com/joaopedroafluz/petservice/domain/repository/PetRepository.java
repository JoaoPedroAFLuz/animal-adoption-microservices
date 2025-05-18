package br.com.joaopedroafluz.petservice.domain.repository;

import br.com.joaopedroafluz.petservice.domain.model.Pet;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PetRepository extends JpaRepository<Pet, UUID>, JpaSpecificationExecutor<Pet> {

    @Query(value = "SELECT p from Pet p WHERE p.status = 'AVAILABLE' ORDER BY p.createdAt")
    List<Pet> findFeatured(Pageable pageable);

    List<Pet> findByOwnerId(UUID ownerId);

}
