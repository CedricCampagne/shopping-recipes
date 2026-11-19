package com.cedric.shoppingrecipes.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserRegisterRequest(
        String username,
        @Email
        String email,
        @NotBlank(message = "Le mot de passe est obligatoire")
        @Size(min = 12, max = 72, message = "Le mot de passe doit contenir entre 12 et 72 caractères")
        @Pattern(regexp = ".*[^A-Za-z0-9].*", message = "Le mot de passe doit contenir au moins un caractère spécial")
        String password
) { }