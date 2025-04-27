package br.com.joaopedroafluz.petservice.api;

import br.com.joaopedroafluz.petservice.domain.dtos.PetInputDTO;
import br.com.joaopedroafluz.petservice.domain.model.Pet;
import br.com.joaopedroafluz.petservice.domain.service.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/pets")
public class PetController {

    private final PetService petService;

    @GetMapping("/{id}")
    public Pet findById(@PathVariable Long id) {
        return petService.findByIdOrThrow(id);
    }

    @GetMapping
    public List<Pet> findAll() {
        return petService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Pet save(@RequestBody PetInputDTO petInputDTO) {
        return petService.save(petInputDTO);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Pet update(@PathVariable Long id, @RequestBody PetInputDTO petInputDTO) {
        return petService.update(id, petInputDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        petService.deleteById(id);
    }

}
