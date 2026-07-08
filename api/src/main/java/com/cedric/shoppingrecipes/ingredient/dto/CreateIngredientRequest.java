package com.cedric.shoppingrecipes.ingredient.dto;

public record CreateIngredientRequest(
        String name,
        String unit
) {}