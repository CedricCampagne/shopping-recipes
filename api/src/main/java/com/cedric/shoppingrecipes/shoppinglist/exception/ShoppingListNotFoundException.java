package com.cedric.shoppingrecipes.shoppinglist.exception;

public class ShoppingListNotFoundException extends RuntimeException{

    private final Long shoppingListId;

    public ShoppingListNotFoundException(Long shoppingListId ) {
        super("ShoppingList not found: " + shoppingListId);
        this.shoppingListId = shoppingListId;
    }

    public Long getShoppingListId() {
        return  shoppingListId;
    }
}