package com.cedric.shoppingrecipes.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserRegisterRequest(
        @NotBlank(message = "Le nom d'utilisateur est obligatoire")
        @Size(max = 50, message = "Le nom d'utilisateur ne doit pas dépasser 50 caractères")
        String username,

        @NotBlank(message = "L'email est obligatoire")
        @Email(message = "L'email n'est pas valide")
        @Size(max = 255, message = "L'email ne doit pas dépasser 255 caractères")
        String email,

        @NotBlank(message = "Le mot de passe est obligatoire")
        @Size(min = 12, max = 72, message = "Le mot de passe doit contenir entre 12 et 72 caractères")
        @Pattern(regexp = ".*[^A-Za-z0-9].*", message = "Le mot de passe doit contenir au moins un caractère spécial")
        String password
) { }