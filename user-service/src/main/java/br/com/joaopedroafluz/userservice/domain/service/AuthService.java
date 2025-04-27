package br.com.joaopedroafluz.userservice.domain.service;

import br.com.joaopedroafluz.userservice.core.security.JwtUtil;
import br.com.joaopedroafluz.userservice.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public String login(String email, String password) {
        Optional<User> userFound = userService.findByEmail(email);

        if (userFound.isEmpty() || !passwordEncoder.matches(password, userFound.get().getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        User user = userFound.get();
        return jwtUtil.generateToken(user.getEmail());
    }

    public User register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userService.save(user);
    }

}
