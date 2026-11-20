package com.cedric.shoppingrecipes.recipeingredient.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UpdateRecipeIngredientRequest(
        Long id,

        @NotNull(message = "L'ingrédient est obligatoire")
        Long ingredientId,

        @NotNull(message = "La quantité est obligatoire")
        @Positive(message = "La quantité doit être supérieure à 0")
        @Digits(integer = 8, fraction = 2, message = "La quantité doit avoir au maximum 2 décimales")
        Double quantityPerPerson
) {
}