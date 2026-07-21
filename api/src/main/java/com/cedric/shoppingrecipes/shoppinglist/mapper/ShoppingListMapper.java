package com.cedric.shoppingrecipes.shoppinglist.mapper;

import com.cedric.shoppingrecipes.shoppinglist.dto.ShoppingListItemResponse;
import com.cedric.shoppingrecipes.shoppinglist.dto.ShoppingListRecipeResponse;
import com.cedric.shoppingrecipes.shoppinglist.dto.ShoppingListResponse;
import com.cedric.shoppingrecipes.shoppinglist.entity.ShoppingList;
import com.cedric.shoppingrecipes.shoppinglist.entity.ShoppingListItem;
import com.cedric.shoppingrecipes.shoppinglist.entity.ShoppingListRecipe;
import org.springframework.stereotype.Component;

@Component
public class ShoppingListMapper {

    public ShoppingListResponse toResponse(ShoppingList list) {
        return new ShoppingListResponse(
                list.getId(),
                list.getStatus().name(),
                list.getItems().stream()
                        .map(this::toItemResponse)
                        .toList(),
                list.getRecipes().stream()
                        .map(this::toRecipeResponse)
                        .toList(),
                list.getCreatedAt(),
                list.getUpdatedAt()
        );
    }

    private ShoppingListItemResponse toItemResponse(ShoppingListItem item) {
        return new ShoppingListItemResponse(
                item.getIngredient().getId(),
                item.getIngredient().getName(),
                item.getTotalQuantity(),
                item.getUnit()
        );
    }

    private ShoppingListRecipeResponse toRecipeResponse(ShoppingListRecipe recipe) {
        return new ShoppingListRecipeResponse(
                recipe.getRecipe().getId(),
                recipe.getRecipe().getName(),
                recipe.getServings()
        );
    }
}