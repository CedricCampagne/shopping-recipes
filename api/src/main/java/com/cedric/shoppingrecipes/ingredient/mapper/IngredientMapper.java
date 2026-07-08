package com.cedric.shoppingrecipes.ingredient.mapper;

import com.cedric.shoppingrecipes.ingredient.dto.CreateIngredientRequest;
import com.cedric.shoppingrecipes.ingredient.dto.IngredientResponse;
import com.cedric.shoppingrecipes.ingredient.dto.UpdateIngredientRequest;
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

    public Ingredient toEntity(CreateIngredientRequest request) {
        Ingredient ingredient = new Ingredient();
        ingredient.setName(request.name());
        ingredient.setUnit(request.unit());
        return  ingredient;
    }

    public void updateEntity(Ingredient ingredient, UpdateIngredientRequest request){
        ingredient.setName(request.name());
        ingredient.setUnit(request.unit());
    }
}