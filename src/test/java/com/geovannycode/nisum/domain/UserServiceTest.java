package com.geovannycode.nisum.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.geovannycode.nisum.domain.model.Role;
import com.geovannycode.nisum.domain.model.UserDTO;
import com.geovannycode.nisum.security.jwt.TokenProvider;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
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
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    @Mock
    private TokenProvider tokenProvider;

    private UserEntity USER_1;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        Set<PhoneEntity> PHONE_1 = new HashSet<>();
        PHONE_1.add(new PhoneEntity("123456789", "08", "20"));

        USER_1 = new UserEntity("Geovanny Mendoza", "geovanny.mendoza@gmail.com", "Password123!", PHONE_1, Role.USER);

        assertNotNull(USER_1, "USER_1 should not be null");
        assertNotNull(PHONE_1, "PHONE_1 should not be null");

        List<UserEntity> users = Arrays.asList(USER_1);
        Mockito.when(userRepository.findAll()).thenReturn(users);

        userService = new UserService(userRepository, passwordEncoder, authenticationManagerBuilder, tokenProvider);
    }

    @Test
    public void readAllTest() throws Exception {
        UserDTO userDTO = UserMapper.convertToDTO(USER_1);
        List<UserDTO> response = userService.getAll();

        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertEquals(response.size(), 1);
        assertEquals(USER_1.getEmail(), userDTO.email());
    }
}
