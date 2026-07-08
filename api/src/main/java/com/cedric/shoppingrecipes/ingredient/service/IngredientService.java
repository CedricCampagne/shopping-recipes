package com.cedric.shoppingrecipes.ingredient.service;


import com.cedric.shoppingrecipes.ingredient.dto.IngredientResponse;
import com.cedric.shoppingrecipes.ingredient.entity.Ingredient;
import com.cedric.shoppingrecipes.ingredient.mapper.IngredientMapper;
import com.cedric.shoppingrecipes.ingredient.repository.IngredientRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IngredientService {

    private final IngredientRepository ingredientRepository;
    private final IngredientMapper ingredientMapper;

    public IngredientService(IngredientRepository ingredientRepository, IngredientMapper ingredientMapper) {
        this.ingredientRepository = ingredientRepository;
        this.ingredientMapper = ingredientMapper;
    }

    public List<IngredientResponse> findAll() {
        return  ingredientRepository.findAll()
                .stream()
                .map(ingredientMapper::toResponse)
                .toList();
    }

    public IngredientResponse findById(Long id) {
        Ingredient ingredient = ingredientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ingredient not found"));

        return ingredientMapper.toResponse(ingredient);
    }

    public IngredientResponse findByName(String name) {
        Ingredient ingredient = ingredientRepository.findByName(name)
                .orElseThrow(() ->new RuntimeException("Ingredient not found"));

        return ingredientMapper.toResponse(ingredient);
    }

    public List<IngredientResponse> findByUnit(String unit) {
        return ingredientRepository.findByUnit(unit)
                .stream()
                .map(ingredientMapper::toResponse)
                .toList();
    }
}