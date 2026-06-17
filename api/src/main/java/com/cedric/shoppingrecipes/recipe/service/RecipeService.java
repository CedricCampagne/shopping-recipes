package com.cedric.shoppingrecipes.recipe.service;


import com.cedric.shoppingrecipes.recipe.entity.Recipe;
import com.cedric.shoppingrecipes.recipe.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository RecipeRepository;

    public List<Recipe> findAll() {
        return  RecipeRepository.findAll();
    }

    public Recipe findById(Long id) {
        return RecipeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recette non trouvée"));
    }

    public Recipe findByName(String name) {
        return RecipeRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Recette non trouvée"));
    }

    public List<Recipe> searchByDescription(String keyword) {
        return RecipeRepository.searchByDescription(keyword);
    }
}