package com.geovannycode.nisum.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.geovannycode.nisum.domain.model.CreateUserRequest;
import com.geovannycode.nisum.domain.model.CreateUserResponse;
import com.geovannycode.nisum.domain.model.PhoneDTO;
import com.geovannycode.nisum.domain.model.Role;
import com.geovannycode.nisum.security.jwt.TokenProvider;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenProvider tokenProvider;

    @Mock
    private Environment env;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateUserSuccess() throws Exception {
        Set<PhoneDTO> phones = new HashSet<>();
        phones.add(new PhoneDTO("123456789", "08", "20"));

        CreateUserRequest userRequest =
                new CreateUserRequest("Geovanny Mendoza", "me@example.com", "Password123!", phones, Role.USER);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode("Password123!")).thenReturn("encodedPassword");
        when(tokenProvider.createToken(any())).thenReturn("dummyToken");
        UserEntity userEntity = new UserEntity();
        userEntity.setId(UUID.randomUUID());
        userEntity.setName(userRequest.name());
        userEntity.setEmail(userRequest.email());
        userEntity.setPassword("Password123!");
        userEntity.setToken("dummyToken");
        userEntity.setRole(userRequest.role());
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        when(env.getProperty("app.regex.password")).thenReturn("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{7,}$");

        CreateUserResponse response = userService.createUser(userRequest, userRequest.role());
        assertNotNull(response);
        assertEquals("Usuario creado satisfactoriamente.", response.getMessage());
    }
}
