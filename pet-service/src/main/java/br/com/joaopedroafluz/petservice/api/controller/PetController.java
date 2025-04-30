package br.com.joaopedroafluz.petservice.api.controller;

import br.com.joaopedroafluz.petservice.domain.dtos.PetInputDTO;
import br.com.joaopedroafluz.petservice.domain.exception.UnauthorizedException;
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

    @GetMapping("/owner/{ownerId}")
    public List<Pet> findByOwner(@PathVariable Long ownerId,
                                 @RequestHeader("X-Auth-User-Role") String role) {
        if (!"ADMIN".equals(role)) {
            throw new UnauthorizedException();
        }

        return petService.findByOwnerId(ownerId);
    }

    @GetMapping("/me")
    public List<Pet> findByLoggedUser(@RequestHeader("X-Auth-User-Id") String userId) {
        return petService.findByOwnerId(Long.parseLong(userId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Pet save(@RequestBody PetInputDTO petInputDTO) {
        return petService.save(petInputDTO);
    }

    @PutMapping("/{id}")
    public Pet update(@PathVariable Long id, @RequestBody PetInputDTO petInputDTO) {
        return petService.update(id, petInputDTO);
    }

    @PutMapping("/adopt/{id}")
    public Pet adopt(@PathVariable Long id,
                     @RequestHeader("X-Auth-User-Id") String userId,
                     @RequestHeader("X-Auth-User-Email") String email) {
        return petService.adopt(id, Long.parseLong(userId), email);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        petService.deleteById(id);
    }

}
