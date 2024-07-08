package com.hngstagetwo.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserResponseDto {
    String userId;
    String firstName;
    String lastName;
    String email;
    String phone;
}
