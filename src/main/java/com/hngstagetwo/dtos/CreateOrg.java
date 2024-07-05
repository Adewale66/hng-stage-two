package com.hngstagetwo.dtos;

import jakarta.validation.constraints.NotBlank;

public record CreateOrg(
        @NotBlank
        String name,
        String description
) {
}
