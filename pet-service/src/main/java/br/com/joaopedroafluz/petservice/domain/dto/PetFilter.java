package br.com.joaopedroafluz.petservice.domain.dto;

import br.com.joaopedroafluz.petservice.domain.enums.Gender;
import br.com.joaopedroafluz.petservice.domain.enums.Size;
import br.com.joaopedroafluz.petservice.domain.enums.Specie;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PetFilter {

    private Specie specie;
    private Gender gender;
    private Size petSize;

}
