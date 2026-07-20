package com.cedric.shoppingrecipes.ingredient.exception;

public class IngredientNotFoundException extends RuntimeException{
    public IngredientNotFoundException(Long ingredientId) {
        super("Ingredient not found: " + ingredientId);
    }
}