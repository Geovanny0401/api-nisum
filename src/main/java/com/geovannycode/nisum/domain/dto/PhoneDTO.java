package com.geovannycode.nisum.domain.dto;

import jakarta.validation.constraints.NotBlank;

public record PhoneDTO(
        @NotBlank(message = "El número es requerido") String number,
        @NotBlank(message = "Se requiere código de ciudad") String cityCode,
        @NotBlank(message = "Se requiere codificador de país") String countryCode) {}
