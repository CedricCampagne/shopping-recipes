package com.cedric.shoppingrecipes.recipe.exception;

public class RecipConflictException extends RuntimeException{

    public RecipConflictException(String message) {
        super(message);
    }
}