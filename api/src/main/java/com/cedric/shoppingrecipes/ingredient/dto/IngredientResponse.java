package com.cedric.shoppingrecipes.ingredient.dto;

public record IngredientResponse(
        Long id,
        String name,
        String unit
) {}