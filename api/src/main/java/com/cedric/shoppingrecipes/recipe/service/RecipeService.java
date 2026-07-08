package com.cedric.shoppingrecipes.recipe.service;


import com.cedric.shoppingrecipes.recipe.dto.RecipeDetailResponse;
import com.cedric.shoppingrecipes.recipe.dto.RecipeResponse;
import com.cedric.shoppingrecipes.recipe.entity.Recipe;
import com.cedric.shoppingrecipes.recipe.mapper.RecipeMapper;
import com.cedric.shoppingrecipes.recipe.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final RecipeMapper recipeMapper;

    public List<RecipeResponse> findAll() {

        return  recipeRepository.findAll()
                .stream()
                .map(recipeMapper::toResponse)
                .toList();
    }

    public RecipeDetailResponse findById(Long id) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recette non trouvée"));

        return recipeMapper.toDetailResponse(recipe);
    }

    public RecipeDetailResponse findByName(String name) {
        Recipe recipe = recipeRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Recette non trouvée"));

        return recipeMapper.toDetailResponse(recipe);
    }

    public List<RecipeDetailResponse> searchByDescription(String keyword) {

        return recipeRepository.searchByDescription(keyword)
                .stream()
                .map(recipeMapper::toDetailResponse)
                .toList();
    }
}