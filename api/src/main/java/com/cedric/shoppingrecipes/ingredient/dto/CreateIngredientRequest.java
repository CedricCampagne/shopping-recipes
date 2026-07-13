package com.cedric.shoppingrecipes.ingredient.dto;

import com.cedric.shoppingrecipes.ingredient.Unit;

public record CreateIngredientRequest(
        String name,
        Unit unit
) {}