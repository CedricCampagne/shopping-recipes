package com.cedric.shoppingrecipes.shoppinglist.dto;

import com.cedric.shoppingrecipes.shoppinglist.ShoppingListStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateStatusRequest(
        @NotNull(message = "Le statut est obligatoire")
        ShoppingListStatus status
) {
}