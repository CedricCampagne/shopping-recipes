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
import com.cedric.shoppingrecipes.recipeingredient.repository.RecipeIngredientRepository;
import com.cedric.shoppingrecipes.shoppinglist.repository.ShoppingListItemRepository;
import com.cedric.shoppingrecipes.shoppinglist.repository.ShoppingListRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IngredientService {

    private final IngredientRepository ingredientRepository;
    private final IngredientMapper ingredientMapper;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final ShoppingListItemRepository shoppingListItemRepository;

    public IngredientService(
            IngredientRepository ingredientRepository,
            IngredientMapper ingredientMapper,
            RecipeIngredientRepository recipeIngredientRepository,
            ShoppingListItemRepository shoppingListItemRepository
    ) {
        this.ingredientRepository = ingredientRepository;
        this.ingredientMapper = ingredientMapper;
        this.recipeIngredientRepository = recipeIngredientRepository;
        this.shoppingListItemRepository = shoppingListItemRepository;
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
            throw new IngredientConflictException(request.name());
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
        if (!ingredientRepository.existsById(id)) {
            throw new IngredientNotFoundException(id);
        }

        if (recipeIngredientRepository.existsByIngredientId(id)
                || shoppingListItemRepository.existsByIngredientId(id)) {
            throw new IngredientConflictException(
                    "Impossible de supprimer cet ingrédient : il est utilisé dans une recette ou une liste sauvegardée."
            );
        }

        ingredientRepository.deleteById(id);
    }

    public Unit[] getUnits(){
        return  Unit.values();
    }
}