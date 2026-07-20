package com.cedric.shoppingrecipes.ingredient.exception;

public class IngredientNotFoundException extends RuntimeException {

    private final Long ingredientId;

    //Cas 1 : recherche par ID
    public IngredientNotFoundException(Long ingredientId) {
        super("Ingredient not found: " + ingredientId);
        this.ingredientId = ingredientId;
    }

    //Cas 2 : recherche par nom
    public IngredientNotFoundException(String name) {
        super("Ingredient not found: " + name);
        this.ingredientId = null; // pas d'ID dans ce cas
    }

    public Long getIngredientId() {
        return ingredientId;
    }
}
