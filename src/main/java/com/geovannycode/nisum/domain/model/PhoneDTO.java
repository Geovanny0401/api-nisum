package com.geovannycode.nisum.domain.model;

import jakarta.validation.constraints.NotBlank;

public record PhoneDTO(
        @NotBlank(message = "Number is required") String number,
        @NotBlank(message = "City Code is required") String cityCode,
        @NotBlank(message = "Country Coder is required") String countryCode) {}
