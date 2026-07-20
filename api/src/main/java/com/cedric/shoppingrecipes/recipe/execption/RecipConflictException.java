package com.cedric.shoppingrecipes.recipe.execption;

public class RecipConflictException extends RuntimeException{
    public RecipConflictException(String message) {
        super(message);
    }
}