package com.geovannycode.nisum.domain;

import com.geovannycode.nisum.domain.model.CreateUserRequest;
import com.geovannycode.nisum.domain.model.CreateUserResponse;
import com.geovannycode.nisum.domain.model.UserDTO;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    @Value("${app.regex.password}")
    private String passwordRegex;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDTO> getAll() {
        try {
            return userRepository.findAll().stream()
                    .map(UserMapper::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            log.error("Error al obtener todos los usuarios: {}", ex.getMessage(), ex);
            throw new UserRetrievalException("Error al obtener todos los usuarios", ex);
        }
    }

    public CreateUserResponse createUser(CreateUserRequest user) {
        try {
            userValidation(user);
            UserEntity newUser = UserMapper.convertToEntity(user);
            UserEntity savedUser = this.userRepository.save(newUser);
            log.info("Created User with id={}", savedUser.getId());
            return buildSuccessResponse(savedUser);
        } catch (EmailAlreadyExistsException | InvalidPasswordFormatException ex) {
            log.error("Validation error: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("Unexpected error during user registration", ex);
            throw new RuntimeException("Error al registrar al usuario", ex);
        }
    }

    private CreateUserResponse<UserDTO> buildSuccessResponse(UserEntity user) {
        UserDTO userDTO = UserMapper.convertToDTO(user);
        CreateUserResponse<UserDTO> response = new CreateUserResponse<>();
        response.setBody(userDTO);
        response.setMessage("Usuario creado satisfactoriamente.");
        return response;
    }

    private void userValidation(CreateUserRequest user) {
        if (userRepository.findByEmail(user.email()).isPresent()) {
            throw new EmailAlreadyExistsException("El correo de " + user.email() + " ya se encuentra registrado");
        }
        if (!user.password().matches(passwordRegex)) {
            throw new InvalidPasswordFormatException("Formato de contraseña es inválido");
        }
    }
}
