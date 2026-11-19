package com.cedric.shoppingrecipes.recipeingredient.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record UpdateRecipeIngredientRequest(
        Long id,

        @NotNull(message = "L'ingrédient est obligatoire")
        Long ingredientId,

        @NotNull(message = "La quantité est obligatoire")
        @Positive(message = "La quantité doit être supérieure à 0")
        @Digits(integer = 8, fraction = 2, message = "La quantité doit avoir au maximum 2 décimales")
        Double quantityPerPerson,

        @NotBlank(message = "L'unité est obligatoire")
        @Size(max = 50, message = "L'unité ne doit pas dépasser 50 caractères")
        String unit
) {
}