package com.cedric.shoppingrecipes.ingredient.service;


import com.cedric.shoppingrecipes.ingredient.Unit;
import com.cedric.shoppingrecipes.ingredient.dto.CreateIngredientRequest;
import com.cedric.shoppingrecipes.ingredient.dto.IngredientResponse;
import com.cedric.shoppingrecipes.ingredient.dto.UpdateIngredientRequest;
import com.cedric.shoppingrecipes.ingredient.entity.Ingredient;
import com.cedric.shoppingrecipes.ingredient.exception.IngredientConflictException;
import com.cedric.shoppingrecipes.ingredient.exception.IngredientNotFoundException;
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
                .orElseThrow(() -> new IngredientNotFoundException(id));

        return ingredientMapper.toResponse(ingredient);
    }

    public IngredientResponse findByName(String name) {
        Ingredient ingredient = ingredientRepository.findByName(name)
                .orElseThrow(() ->new IngredientNotFoundException(name));

        return ingredientMapper.toResponse(ingredient);
    }

    public List<IngredientResponse> findByUnit(String unit) {
        return ingredientRepository.findByUnit(unit)
                .stream()
                .map(ingredientMapper::toResponse)
                .toList();
    }

    public IngredientResponse create(CreateIngredientRequest request) {
        Optional<Ingredient> existing = ingredientRepository.findByName(request.name());
        if (existing.isPresent()) {
            throw new IngredientConflictException("Ingredient name already exists");
        }

        Ingredient ingredient = ingredientMapper.toEntity(request);
        Ingredient saved = ingredientRepository.save(ingredient);
        return  ingredientMapper.toResponse(saved);
    }

    public IngredientResponse update(Long id, UpdateIngredientRequest request) {
        Ingredient ingredient = ingredientRepository.findById(id)
                .orElseThrow(() -> new IngredientNotFoundException(id));
        ingredientMapper.updateEntity(ingredient, request);

        Ingredient saved = ingredientRepository.save(ingredient);
        return ingredientMapper.toResponse(saved);
    }

    public void delete(Long id) {
        if(!ingredientRepository.existsById(id)){
            throw new IngredientNotFoundException(id);
        }
        ingredientRepository.deleteById(id);
    }

    public Unit[] getUnits(){
        return  Unit.values();
    }
}