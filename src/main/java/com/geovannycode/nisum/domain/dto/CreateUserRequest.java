package com.geovannycode.nisum.domain.dto;

import com.geovannycode.nisum.domain.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.Set;

public record CreateUserRequest(
        String name,
        @NotBlank(message = "Se requiere el correo electrónico del usuario")
                @Email(regexp = "^[^@]+@[^@]+\\.[a-zA-Z]{2,}$", message = "Ingresar el email valido.")
                String email,
        @NotBlank(message = "Se requiere contraseña de usuario") String password,
        Set<PhoneDTO> phones,
        Role role) {}
