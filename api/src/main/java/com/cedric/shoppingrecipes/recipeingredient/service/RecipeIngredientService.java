package com.cedric.shoppingrecipes.recipeingredient.service;


import com.cedric.shoppingrecipes.recipeingredient.dto.RecipeIngredientResponse;
import com.cedric.shoppingrecipes.recipeingredient.entity.RecipeIngredient;
import com.cedric.shoppingrecipes.recipeingredient.exception.RecipeIngredientNotFoundException;
import com.cedric.shoppingrecipes.recipeingredient.mapper.RecipeIngredientMapper;
import com.cedric.shoppingrecipes.recipeingredient.repository.RecipeIngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeIngredientService {

    private final RecipeIngredientRepository recipeIngredientRepository;
    private final RecipeIngredientMapper recipeIngredientMapper;

    public List<RecipeIngredientResponse> findAll() {

        return  recipeIngredientRepository.findAll()
                .stream()
                .map(recipeIngredientMapper::toResponse)
                .toList();
    }

    public RecipeIngredientResponse findById(Long id) {
        RecipeIngredient recipeIngredient = recipeIngredientRepository.findById(id)
                .orElseThrow(() -> new RecipeIngredientNotFoundException(id));

        return recipeIngredientMapper.toResponse(recipeIngredient);
    }

    public List<RecipeIngredientResponse> findByRecipeId(Long recipeId) {
        return recipeIngredientRepository.findByRecipeId(recipeId)
                .stream()
                .map(recipeIngredientMapper::toResponse)
                .toList();
    }

    public List<RecipeIngredient> findByIngredientId(Long ingredientId) {
        return recipeIngredientRepository.findByIngredientId(ingredientId);
    }

    public void delete(Long id) {
        recipeIngredientRepository.deleteById(id);
    }
}