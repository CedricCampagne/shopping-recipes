package com.cedric.shoppingrecipes.shoppinglist.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateShoppingListRequest(
        @NotEmpty(message = "La liste des recettes ne peut pas être vide")
        @Valid
        List<RecipeSelection> recipes
) {
    public record RecipeSelection (
            @NotNull(message = "L'identifiant de la recette est obligatoire")
            Long recipeId,
            @NotNull(message = "Le nombre de portions est obligatoire")
            @Min(value = 1, message = "Le nombre de portions doit être d'au moins 1")
            Integer servings
    ) {}
}