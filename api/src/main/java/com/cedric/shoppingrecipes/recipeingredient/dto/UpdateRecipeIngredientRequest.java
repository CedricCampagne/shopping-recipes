package com.cedric.shoppingrecipes.recipeingredient.dto;

public record UpdateRecipeIngredientRequest(
        Long id,                 // ID du RecipeIngredient
        Long ingredientId,       // ID de l’ingrédient
        Double quantityPerPerson,
        String unit
) {
}