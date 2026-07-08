package com.cedric.shoppingrecipes.recipeingredient.mapper;

import com.cedric.shoppingrecipes.ingredient.mapper.IngredientMapper;
import com.cedric.shoppingrecipes.recipeingredient.dto.CreateRecipeIngredientRequest;
import com.cedric.shoppingrecipes.recipeingredient.dto.RecipeIngredientResponse;
import com.cedric.shoppingrecipes.recipeingredient.entity.RecipeIngredient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RecipeIngredientMapper {

    private final IngredientMapper ingredientMapper;

    public RecipeIngredientResponse toResponse(RecipeIngredient ri) {
        return new RecipeIngredientResponse(
                ri.getId(),
                ri.getQuantityPerPerson(),
                ri.getUnit(),
                ingredientMapper.toResponse(ri.getIngredient())
        );
    }
}
