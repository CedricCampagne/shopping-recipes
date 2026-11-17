package com.cedric.shoppingrecipes.ingredient.dto;

import com.cedric.shoppingrecipes.ingredient.Unit;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateIngredientRequest(
        @NotBlank(message = "Le nom de l'ingrédient est obligatoire")
        @Size(max = 50, message = "Le nom ne doit pas dépasser 50 caractères")
        String name,

        @NotNull(message = "L'unité est obligatoire")
        Unit unit
) {}