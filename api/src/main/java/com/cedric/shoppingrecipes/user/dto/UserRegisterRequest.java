package com.cedric.shoppingrecipes.user.dto;

import jakarta.validation.constraints.Email;

public record UserRegisterRequest(
        String username,
        @Email
        String email,
        String password
) { }