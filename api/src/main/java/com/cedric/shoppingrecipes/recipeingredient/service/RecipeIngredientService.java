package com.cedric.shoppingrecipes.recipeingredient.service;


import com.cedric.shoppingrecipes.recipeingredient.entity.RecipeIngredient;
import com.cedric.shoppingrecipes.recipeingredient.repository.RecipeIngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeIngredientService {

    private final RecipeIngredientRepository recipeIngredientRepository;

    public List<RecipeIngredient> findAll() {
        return  recipeIngredientRepository.findAll();
    }

    public RecipeIngredient findById(Long id) {
        return recipeIngredientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("RecipeIngredient non trouvé : " + id));
    }

    public RecipeIngredient save(RecipeIngredient recipeIngredient) {
        return recipeIngredientRepository.save(recipeIngredient);
    }

    public void delete(Long id) {
        recipeIngredientRepository.deleteById(id);
    }

    public List<RecipeIngredient> findByRecipeId(Long recipeId) {
        return recipeIngredientRepository.findByRecipeId(recipeId);
    }

    public List<RecipeIngredient> findByIngredientId(Long ingredientId) {
        return recipeIngredientRepository.findByIngredientId(ingredientId);
    }


}