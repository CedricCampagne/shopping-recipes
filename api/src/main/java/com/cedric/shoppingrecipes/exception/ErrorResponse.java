package com.cedric.shoppingrecipes.exception;

public record ErrorResponse (
        int status,
        String message,
        Long resourceId
) {}


