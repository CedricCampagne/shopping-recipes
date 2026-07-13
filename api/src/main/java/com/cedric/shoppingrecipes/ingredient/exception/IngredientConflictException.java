package com.cedric.shoppingrecipes.ingredient.exception;

public class IngredientConflictException extends RuntimeException{
    public IngredientConflictException(String message) {
        super(message);
    }
}