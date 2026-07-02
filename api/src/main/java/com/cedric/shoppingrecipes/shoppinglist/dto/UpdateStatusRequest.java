package com.cedric.shoppingrecipes.shoppinglist.dto;

import com.cedric.shoppingrecipes.shoppinglist.ShoppingListStatus;

public record UpdateStatusRequest(ShoppingListStatus status) {
}