package com.geovannycode.nisum.domain.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.Set;

public record CreateUserRequest(
        String name,
        @NotBlank(message = "Se requiere el correo electrónico del usuario") @Email String email,
        @NotBlank(message = "Se requiere contraseña de usuario") String password,
        Set<PhoneDTO> phones,
        Role role) {}
