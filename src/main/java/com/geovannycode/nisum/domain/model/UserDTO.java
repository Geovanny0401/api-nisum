package com.geovannycode.nisum.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record UserDTO(
        @JsonProperty(value = "id", index = 0) UUID Id,
        @JsonProperty(value = "name", index = 1) String name,
        @JsonProperty(value = "email", index = 2) String email,
        @JsonProperty(value = "password", index = 3) String password,
        @JsonProperty(value = "phones", index = 4) Set<PhoneDTO> phones,
        @JsonProperty(value = "token", index = 5) String token,
        @JsonProperty(value = "created", index = 6) LocalDateTime createdAt,
        @JsonProperty(value = "modified", index = 7) LocalDateTime updateAt,
        @JsonProperty(value = "last_login", index = 8) LocalDateTime lastLogin,
        @JsonProperty(value = "isactive", index = 9) boolean isActive) {}
