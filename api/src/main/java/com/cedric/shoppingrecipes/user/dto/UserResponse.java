package com.cedric.shoppingrecipes.user.dto;

import jakarta.validation.constraints.Email;

public record UserResponse (
        Long id,
        String username,
        @Email
        String email
){}
