package com.cedric.shoppingrecipes.recipe.dto;

public record RecipeResponse(
        Long id,
        String name,
        String description,
        Integer servings
) {
}