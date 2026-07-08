package com.cedric.shoppingrecipes.recipeingredient.dto;

public record CreateRecipeIngredientRequest(
        Long ingredientId,
        Double quantityPerPerson,
        String unit
) {}