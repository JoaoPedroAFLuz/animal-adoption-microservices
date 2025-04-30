package br.com.joaopedroafluz.userservice.domain.service;

import br.com.joaopedroafluz.userservice.domain.model.User;
import br.com.joaopedroafluz.userservice.domain.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public void save(User user) {
        userRepository.save(user);
    }

}
