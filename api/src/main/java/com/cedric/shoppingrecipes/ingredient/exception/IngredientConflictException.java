package com.cedric.shoppingrecipes.ingredient.exception;

public class IngredientConflictException extends RuntimeException {

    private final Long ingredientId;

    public IngredientConflictException(String message) {
        super(message);
        this.ingredientId = null;
    }

    public IngredientConflictException(Long ingredientId) {
        super("Ingredient already exists: " + ingredientId);
        this.ingredientId = ingredientId;
    }

    public Long getIngredientId() {
        return ingredientId;
    }
}
