package com.cedric.shoppingrecipes.recipe.dto;

import com.cedric.shoppingrecipes.recipeingredient.dto.CreateRecipeIngredientRequest;

import java.util.List;

public record CreateRecipeRequest(
        String name,
        String description,
        Integer servings,
        List<CreateRecipeIngredientRequest> ingredients
) {}