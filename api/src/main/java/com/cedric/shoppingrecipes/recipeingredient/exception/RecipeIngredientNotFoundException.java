package com.cedric.shoppingrecipes.recipeingredient.exception;

public class RecipeIngredientNotFoundException extends RuntimeException{
    private final Long recipeIngredientId;

    public RecipeIngredientNotFoundException(Long recipeIngredientId ) {
        super("RecipeIngredient not found: " + recipeIngredientId);
        this.recipeIngredientId = recipeIngredientId;
    }

    public Long getRecipeIngredientId(){
        return recipeIngredientId;
    }
}