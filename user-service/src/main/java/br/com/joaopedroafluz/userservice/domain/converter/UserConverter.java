package br.com.joaopedroafluz.userservice.domain.converter;

import br.com.joaopedroafluz.userservice.domain.dto.UserDTO;
import br.com.joaopedroafluz.userservice.domain.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

    public UserDTO toDTO(User user) {
        return new UserDTO(user.getId(), user.getName(), user.getEmail());
    }

}
