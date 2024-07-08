package com.hngstagetwo.dtos;

import jakarta.validation.constraints.NotBlank;

public record IdDto(
        @NotBlank
        String userId
) {
}