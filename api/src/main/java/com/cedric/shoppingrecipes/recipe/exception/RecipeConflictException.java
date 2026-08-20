package com.cedric.shoppingrecipes.recipe.exception;

public class RecipeConflictException extends RuntimeException{

    public RecipeConflictException(String message) {
        super(message);
    }
}