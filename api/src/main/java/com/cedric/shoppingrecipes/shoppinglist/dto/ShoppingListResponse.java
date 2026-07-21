package com.cedric.shoppingrecipes.shoppinglist.dto;

import java.time.LocalDateTime;
import java.util.List;

public  record ShoppingListResponse(
        Long id,
        String status,
        List<ShoppingListItemResponse> items,
        List<ShoppingListRecipeResponse> recipes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

}