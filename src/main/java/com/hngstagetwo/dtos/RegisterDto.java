package com.hngstagetwo.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterDto(
        @NotBlank
        String firstName,
        @NotBlank
        String lastName,

        @NotBlank
        @Email
        String email,

        @NotBlank
        String phone,
        @NotBlank
        String password
) {
}
