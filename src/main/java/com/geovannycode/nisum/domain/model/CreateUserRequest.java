package com.geovannycode.nisum.domain.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.Set;

public record CreateUserRequest(
        @NotBlank(message = "User Name is required") String name,
        @NotBlank(message = "User email is required") @Email String email,
        @NotBlank(message = "User password is required") String password,
        @NotEmpty(message = "Phones cannot be empty") Set<PhoneDTO> phones) {}
