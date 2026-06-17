package com.cedric.shoppingrecipes.ingredient.service;


import com.cedric.shoppingrecipes.ingredient.entity.Ingredient;
import com.cedric.shoppingrecipes.ingredient.repository.IngredientRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IngredientService {

    private final IngredientRepository ingredientRepository;

    public IngredientService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    public List<Ingredient> findAll() {
        return  ingredientRepository.findAll();
    }

    public Ingredient findById(Long id) {
        return ingredientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ingredient not found"));
    }

    public Optional<Ingredient> findByName(String name) {
        return ingredientRepository.findByName(name);
    }

    public List<Ingredient> findByUnit(String unit) {
        return ingredientRepository.findByUnit(unit);
    }

}