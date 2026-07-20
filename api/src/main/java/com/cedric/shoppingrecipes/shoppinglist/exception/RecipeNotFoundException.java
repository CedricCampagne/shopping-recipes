package com.cedric.shoppingrecipes.shoppinglist.exception;

public class RecipeNotFoundException extends RuntimeException{
    private final Long recipeId;

    public RecipeNotFoundException(Long recipeId){
        super("Recette " + recipeId + " introuvable");
        this.recipeId = recipeId;
    }

    public Long getRecipeId() {
        return recipeId;
    }
}