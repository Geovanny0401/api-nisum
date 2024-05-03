package com.geovannycode.nisum.domain.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.Set;

public record CreateUserRequest(
        String name,
        @NotBlank(message = "User email is required") @Email String email,
        @NotBlank(message = "User password is required") String password,
        Set<PhoneDTO> phones,
        Role role) {}
