package com.cedric.shoppingrecipes.shoppinglist.service;

import com.cedric.shoppingrecipes.ingredient.entity.Ingredient;
import com.cedric.shoppingrecipes.recipe.entity.Recipe;

import com.cedric.shoppingrecipes.recipe.repository.RecipeRepository;
import com.cedric.shoppingrecipes.recipeingredient.entity.RecipeIngredient;
import com.cedric.shoppingrecipes.recipeingredient.repository.RecipeIngredientRepository;

import com.cedric.shoppingrecipes.shoppinglist.ShoppingListStatus;
import com.cedric.shoppingrecipes.shoppinglist.dto.CreateShoppingListRequest;

import com.cedric.shoppingrecipes.shoppinglist.dto.ShoppingListResponse;
import com.cedric.shoppingrecipes.shoppinglist.entity.ShoppingList;

import com.cedric.shoppingrecipes.shoppinglist.entity.ShoppingListItem;
import com.cedric.shoppingrecipes.shoppinglist.entity.ShoppingListRecipe;
import com.cedric.shoppingrecipes.shoppinglist.mapper.ShoppingListMapper;
import com.cedric.shoppingrecipes.shoppinglist.repository.ShoppingListRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ShoppingListService {

    private final RecipeRepository recipeRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final ShoppingListRepository shoppingListRepository;
    private final ShoppingListMapper shoppingListMapper;

    @Transactional(readOnly = true)
    public List<ShoppingListResponse> getAll() {
        return shoppingListRepository.findAll()
                .stream()
                .map(shoppingListMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ShoppingListResponse getById(Long id) {
        ShoppingList list = shoppingListRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shopping list non trouvée"));

        return shoppingListMapper.toResponse(list);
    }

    @Transactional
    public ShoppingListResponse createShoppingList(CreateShoppingListRequest request) {

        if (request.recipes() == null || request.recipes().isEmpty()) {
            throw new IllegalArgumentException("La liste des recettes ne peut pas être vide.");
        }

        // Creation de la list
        ShoppingList shoppingList = new ShoppingList();
        shoppingList.setStatus(ShoppingListStatus.EN_COURS);

        // Les Maps pour fusionner
        Map<Long, Double> mergedQuantities = new HashMap<>();
        Map<Long, String> mergedUnits = new HashMap<>();
        Map<Long, Ingredient> mergedIngredients = new HashMap<>();

        // Parcourir les recettes
        for (CreateShoppingListRequest.RecipeSelection selection : request.recipes()) {

            Recipe recipe = recipeRepository.findById(selection.recipeId())
                    .orElseThrow(() -> new RuntimeException("Recette non trouvée"));

            ShoppingListRecipe slr = new ShoppingListRecipe();
            slr.setRecipe(recipe);
            slr.setServings(selection.servings());
            slr.setShoppingList(shoppingList);
            shoppingList.getRecipes().add(slr);
            List<RecipeIngredient> recipeIngredients =
                    recipeIngredientRepository.findByRecipeId(recipe.getId());

            for (RecipeIngredient ri : recipeIngredients) {
                Long ingredientId = ri.getIngredient().getId();
                double quantity = ri.getQuantityPerPerson()* selection.servings();

                mergedQuantities.merge(ingredientId, quantity, Double::sum);
                mergedUnits.put(ingredientId,ri.getUnit());
                mergedIngredients.put(ingredientId, ri.getIngredient());
            }
        }

        // Créer les ShoppingListItem
        for (Long ingredientId : mergedQuantities.keySet()) {

            ShoppingListItem item = new ShoppingListItem();
            item.setIngredient(mergedIngredients.get(ingredientId));
            item.setTotalQuantity(mergedQuantities.get(ingredientId));
            item.setUnit(mergedUnits.get(ingredientId));
            item.setShoppingList(shoppingList);

            shoppingList.getItems().add(item);
        }

        ShoppingList saved = shoppingListRepository.save(shoppingList);
        return shoppingListMapper.toResponse(saved);
    }

    @Transactional
    public void deleteShoppingList(Long id) {
        if (!shoppingListRepository.existsById(id)) {
            throw new RuntimeException("Shopping list non trouvée");
        }
        shoppingListRepository.deleteById(id);
    }

    @Transactional
    public ShoppingListResponse updateStatus(Long id, ShoppingListStatus newStatus) {
        ShoppingList list = shoppingListRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shopping liste non trouvée"));

        list.setStatus(newStatus);

        ShoppingList saved = shoppingListRepository.save(list);

        return shoppingListMapper.toResponse(saved);
    }
}