package com.geovannycode.nisum.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.geovannycode.nisum.domain.dto.CreateUserRequest;
import com.geovannycode.nisum.domain.dto.CreateUserResponse;
import com.geovannycode.nisum.domain.dto.PhoneDTO;
import com.geovannycode.nisum.domain.dto.UserDTO;
import com.geovannycode.nisum.domain.entities.PhoneEntity;
import com.geovannycode.nisum.domain.entities.UserEntity;
import com.geovannycode.nisum.domain.enums.Role;
import com.geovannycode.nisum.domain.mapper.UserMapper;
import com.geovannycode.nisum.exception.EmailAlreadyExistsException;
import com.geovannycode.nisum.exception.InvalidPasswordFormatException;
import com.geovannycode.nisum.exception.ResourceNotFoundException;
import com.geovannycode.nisum.repository.UserRepository;
import com.geovannycode.nisum.security.jwt.TokenProvider;
import com.geovannycode.nisum.service.UserService;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

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

        lenient().when(userRepository.findAll()).thenReturn(users);

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

    @Test
    public void createUser_Success() {
        ReflectionTestUtils.setField(
                userService, "passwordRegex", "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@#$%^&+=!*]{7,}$");

        Set<PhoneDTO> phones = new HashSet<>();
        phones.add(new PhoneDTO("123456789", "01", "57"));

        CreateUserRequest request =
                new CreateUserRequest("John Doe", "user@example.com", "Password123!", phones, Role.USER);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(tokenProvider.createToken(any())).thenReturn("token");
        when(userRepository.save(any(UserEntity.class))).thenReturn(new UserEntity());

        CreateUserResponse response = userService.createUser(request, request.role());

        assertNotNull(response);
        assertEquals("Usuario creado satisfactoriamente.", response.getMessage());
        verify(userRepository).save(any(UserEntity.class));
    }

    @Test
    public void createUser_EmailAlreadyExists() {
        CreateUserRequest request =
                new CreateUserRequest("John Doe", "user@example.com", "Password123!", new HashSet<>(), Role.USER);
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(new UserEntity()));

        Exception exception = assertThrows(EmailAlreadyExistsException.class, () -> {
            userService.createUser(request, request.role());
        });

        assertEquals("El correo de user@example.com ya se encuentra registrado", exception.getMessage());
    }

    @Test
    public void createUser_InvalidPasswordFormat() {
        ReflectionTestUtils.setField(
                userService, "passwordRegex", "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");
        CreateUserRequest request =
                new CreateUserRequest("John Doe", "user@example.com", "pass", new HashSet<>(), Role.USER);

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.empty());

        Exception exception = assertThrows(InvalidPasswordFormatException.class, () -> {
            userService.createUser(request, request.role());
        });

        assertEquals("Formato de contraseña es inválido", exception.getMessage());
    }

    @Test
    public void authenticateUser_Success() throws Exception {
        ReflectionTestUtils.setField(
                userService, "passwordRegex", "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@#$%^&+=!*]{7,}$");

        UserEntity user = new UserEntity();
        user.setEmail("user@example.com");

        Set<PhoneDTO> phones = new HashSet<>();
        phones.add(new PhoneDTO("123456789", "01", "57"));

        CreateUserRequest request =
                new CreateUserRequest("John Doe", "user@example.com", "Password123!", phones, Role.USER);

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));

        Authentication authentication = mock(Authentication.class);
        AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
        when(authenticationManagerBuilder.getObject()).thenReturn(authenticationManager);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        when(tokenProvider.createToken(authentication)).thenReturn("token");

        String result = userService.authenticate(request);

        assertEquals("token", result);
    }

    @Test
    public void authenticateUser_Failure() {
        Set<PhoneDTO> phones = new HashSet<>();
        phones.add(new PhoneDTO("123456789", "01", "57"));

        CreateUserRequest request =
                new CreateUserRequest("John Doe", "user@example.com", "Password123!", phones, Role.USER);
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.authenticate(request);
        });

        assertEquals("Usuario no encontrado", exception.getMessage());
    }

    @Test
    public void testEmailAlreadyExists() {
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(new UserEntity()));
        Exception exception = assertThrows(EmailAlreadyExistsException.class, () -> {
            userService.createUser(
                    new CreateUserRequest("John Doe", "user@example.com", "Password123!", new HashSet<>(), Role.USER),
                    Role.USER);
        });
        assertEquals("El correo de user@example.com ya se encuentra registrado", exception.getMessage());
    }

    @Test
    public void testInvalidPasswordFormat() {
        ReflectionTestUtils.setField(
                userService, "passwordRegex", "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@#$%^&+=!*]{7,}$");
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.empty());
        Exception exception = assertThrows(InvalidPasswordFormatException.class, () -> {
            userService.createUser(
                    new CreateUserRequest("John Doe", "user@example.com", "pass", new HashSet<>(), Role.USER),
                    Role.USER);
        });
        assertEquals("Formato de contraseña es inválido", exception.getMessage());
    }
}
