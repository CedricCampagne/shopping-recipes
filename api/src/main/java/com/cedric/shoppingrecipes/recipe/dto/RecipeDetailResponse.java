package com.cedric.shoppingrecipes.recipe.dto;

import com.cedric.shoppingrecipes.recipeingredient.dto.RecipeIngredientResponse;

import java.util.List;

public record RecipeDetailResponse(
        Long id,
        String name,
        String description,
        Integer servings,
        List<RecipeIngredientResponse> ingredients
) {
}