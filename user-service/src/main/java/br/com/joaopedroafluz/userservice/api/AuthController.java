package br.com.joaopedroafluz.userservice.api;

import br.com.joaopedroafluz.userservice.domain.model.User;
import br.com.joaopedroafluz.userservice.domain.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password) {
        return authService.login(email, password);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@RequestBody User user) {
        authService.register(user);
    }

}