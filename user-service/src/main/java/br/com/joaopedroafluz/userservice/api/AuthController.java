package br.com.joaopedroafluz.userservice.api;

import br.com.joaopedroafluz.userservice.domain.converter.UserConverter;
import br.com.joaopedroafluz.userservice.domain.dto.UserDTO;
import br.com.joaopedroafluz.userservice.domain.model.User;
import br.com.joaopedroafluz.userservice.domain.service.AuthService;
import br.com.joaopedroafluz.userservice.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final AuthService authService;
    private final UserConverter userConverter;

    @GetMapping("/me")
    public UserDTO me(@RequestHeader("X-Auth-User-Id") Long userId) {
        final var user = userService.findById(userId);
        return userConverter.toDTO(user);
    }


    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password) {
        return authService.login(email, password);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO register(@RequestBody User user) {
        final var registeredUser = authService.register(user);
        return userConverter.toDTO(registeredUser);
    }

}