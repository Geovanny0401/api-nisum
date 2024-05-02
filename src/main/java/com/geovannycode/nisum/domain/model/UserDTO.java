package com.geovannycode.nisum.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record UserDTO(
        UUID Id,
        String name,
        String email,
        String password,
        Set<PhoneDTO> phones,
        @JsonProperty(value = "created") LocalDateTime createdAt,
        @JsonProperty(value = "modified") LocalDateTime updateAt,
        @JsonProperty(value = "last_login") LocalDateTime lastLogin,
        String token,
        @JsonProperty(value = "isactive") boolean isActive) {}
