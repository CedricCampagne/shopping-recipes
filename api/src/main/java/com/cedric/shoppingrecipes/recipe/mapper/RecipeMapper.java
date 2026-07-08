package com.cedric.shoppingrecipes.recipe.mapper;

import com.cedric.shoppingrecipes.recipe.dto.RecipeDetailResponse;
import com.cedric.shoppingrecipes.recipe.dto.RecipeResponse;
import com.cedric.shoppingrecipes.recipe.entity.Recipe;
import com.cedric.shoppingrecipes.recipeingredient.mapper.RecipeIngredientMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RecipeMapper {

    private final RecipeIngredientMapper recipeIngredientMapper;

    public RecipeResponse toResponse(Recipe recipe) {
        return new RecipeResponse(
                recipe.getId(),
                recipe.getName(),
                recipe.getDescription(),
                recipe.getServings()
        );
    }

    public RecipeDetailResponse toDetailResponse(Recipe recipe) {
        return new RecipeDetailResponse(
                recipe.getId(),
                recipe.getName(),
                recipe.getDescription(),
                recipe.getServings(),
                recipe.getIngredients().stream()
                        .map(recipeIngredientMapper::toResponse)
                        .toList()
        );
    }
}