package com.cedric.shoppingrecipes.shoppinglist.dto;

public  record ShoppingListItemResponse(
        Long ingredientId,
        String ingredientName,
        Double totalQuantity,
        String unit
) {
}