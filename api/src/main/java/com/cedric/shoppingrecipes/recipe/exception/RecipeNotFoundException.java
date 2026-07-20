package com.cedric.shoppingrecipes.recipe.exception;

public class RecipeNotFoundException extends RuntimeException{

    private final Long recipeId;

    public RecipeNotFoundException(Long recipeId) {
        super("Recipe not found: " + recipeId);
        this.recipeId = recipeId;
    }

    public RecipeNotFoundException(String name) {
        super("Recipe not found: " + name);
        this.recipeId = null;
    }

    public Long getRecipeId() {
        return recipeId;
    }
}