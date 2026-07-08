package com.cedric.shoppingrecipes.ingredient.mapper;

import com.cedric.shoppingrecipes.ingredient.dto.IngredientResponse;
import com.cedric.shoppingrecipes.ingredient.entity.Ingredient;
import org.springframework.stereotype.Component;

@Component
public class IngredientMapper {

    public IngredientResponse toResponse(Ingredient ingredient) {
        return new IngredientResponse(
                ingredient.getId(),
                ingredient.getName(),
                ingredient.getUnit()
        );
    }
}