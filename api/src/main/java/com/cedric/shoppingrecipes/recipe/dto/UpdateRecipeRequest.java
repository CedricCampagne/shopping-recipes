package com.cedric.shoppingrecipes.recipe.dto;

import com.cedric.shoppingrecipes.recipeingredient.dto.UpdateRecipeIngredientRequest;

import java.util.List;

public record UpdateRecipeRequest(
        String name,
        String description,
        Integer servings,
        List<UpdateRecipeIngredientRequest> ingredients
) {
}