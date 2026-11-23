package com.cedric.shoppingrecipes.recipe.exception;

public class DuplicateRecipeIngredientException extends RuntimeException {

    public DuplicateRecipeIngredientException() {
        super("Un même ingrédient ne peut apparaître qu'une seule fois dans une recette");
    }
}
