package br.com.joaopedroafluz.petservice.domain.repository.specification;

import br.com.joaopedroafluz.petservice.domain.dto.PetFilter;
import br.com.joaopedroafluz.petservice.domain.model.Pet;
import jakarta.persistence.criteria.Predicate;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class PetSpecification {

    public static Specification<Pet> withFilters(PetFilter filter) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getSpecie() != null) {
                predicates.add(builder.equal(root.get("specie"), filter.getSpecie()));
            }
            if (filter.getGender() != null) {
                predicates.add(builder.equal(root.get("gender"), filter.getGender()));
            }
            if (filter.getPetSize() != null) {
                predicates.add(builder.equal(root.get("size"), filter.getPetSize()));
            }

            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }

}
