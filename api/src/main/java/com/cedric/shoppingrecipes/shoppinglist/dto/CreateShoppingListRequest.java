package com.cedric.shoppingrecipes.shoppinglist.dto;

import java.util.List;

public record CreateShoppingListRequest(
        List<RecipeSelection> recipes
) {
    public record RecipeSelection (
            Long recipeId,
            Integer servings
    ) {}
}