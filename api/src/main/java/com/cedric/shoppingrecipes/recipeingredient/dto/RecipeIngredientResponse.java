package com.cedric.shoppingrecipes.recipeingredient.dto;

import com.cedric.shoppingrecipes.ingredient.dto.IngredientResponse;

public record RecipeIngredientResponse(
        Long id,
        Double quantityPerPerson,
        String unit,
        IngredientResponse ingredient
) {}