package com.hngstagetwo.errors;


public record ValidationError(
        String field,
        String message
) {
}
