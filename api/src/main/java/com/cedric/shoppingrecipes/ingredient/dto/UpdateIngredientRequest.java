package com.cedric.shoppingrecipes.ingredient.dto;

public record UpdateIngredientRequest(
        String name,
        String unit
) {}