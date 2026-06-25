package com.cedric.shoppingrecipes.shoppinglist.dto;

public record ShoppingListRecipeResponse(
        Long recipeId,
        String recipeName,
        Integer servings
) {
}