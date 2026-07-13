package com.cedric.shoppingrecipes.ingredient.dto;

import com.cedric.shoppingrecipes.ingredient.Unit;

public record UpdateIngredientRequest(
        String name,
        Unit unit
) {}