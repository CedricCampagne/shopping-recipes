package com.cedric.shoppingrecipes.exception;

public record ErrorResponse (
        String message,
        Long recipeId
) {}


