package com.cedric.shoppingrecipes.recipe.service;


import com.cedric.shoppingrecipes.ingredient.entity.Ingredient;
import com.cedric.shoppingrecipes.ingredient.repository.IngredientRepository;
import com.cedric.shoppingrecipes.recipe.dto.CreateRecipeRequest;
import com.cedric.shoppingrecipes.recipe.dto.RecipeDetailResponse;
import com.cedric.shoppingrecipes.recipe.dto.RecipeResponse;
import com.cedric.shoppingrecipes.recipe.dto.UpdateRecipeRequest;
import com.cedric.shoppingrecipes.recipe.entity.Recipe;
import com.cedric.shoppingrecipes.recipe.mapper.RecipeMapper;
import com.cedric.shoppingrecipes.recipe.repository.RecipeRepository;
import com.cedric.shoppingrecipes.recipeingredient.entity.RecipeIngredient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final RecipeMapper recipeMapper;
    private final IngredientRepository ingredientRepository;

    public List<RecipeResponse> findAll() {

        return  recipeRepository.findAll()
                .stream()
                .map(recipeMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
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

    public RecipeDetailResponse create(CreateRecipeRequest request) {
        // Création de la recette
        Recipe recipe = new Recipe();
        recipe.setName(request.name());
        recipe.setDescription(request.description());
        recipe.setServings(request.servings());

        // Création des RecipeIngredient
        List<RecipeIngredient> recipeIngredients = request.ingredients().stream()
                .map(riRequest -> {
                    Ingredient ingredient = ingredientRepository.findById(riRequest.ingredientId())
                            .orElseThrow(()-> new RuntimeException("Ingredient not found : " + riRequest.ingredientId()));

                    RecipeIngredient ri = new RecipeIngredient();
                    ri.setRecipe(recipe);
                    ri.setIngredient(ingredient);
                    ri.setQuantityPerPerson(riRequest.quantityPerPerson());
                    ri.setUnit(riRequest.unit());

                    return ri;
                })
                .toList();

        // lier les RecipeIngredient a la recette
        recipe.setIngredients(recipeIngredients);

        //Sauvegarder la recette (Casace ALL save aussi les RecipeIngredients)
        Recipe saved = recipeRepository.save(recipe);

        // retounrer le DTO complet
        return recipeMapper.toDetailResponse(saved);
    }

    public RecipeDetailResponse update(Long id, UpdateRecipeRequest request) {

        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recipe not found : " + id));

        recipe.setName(request.name());
        recipe.setDescription(request.description());
        recipe.setServings(request.servings());

        // 3) Construire une nouvelle liste de RecipeIngredient à partir du DTO UpdateRecipeRequest
        //    Pour chaque élément du DTO :
        //      - récupérer l’ingrédient en base via ingredientId
        //      - SINON → c’est un nouvel ingrédient ajouté à la recette
        //      - créer ou mettre à jour un RecipeIngredient
        //      - lui donner : recipe, ingredient, quantityPerPerson, unit
        //    → Ajouter chaque RecipeIngredient dans une nouvelle liste
        List<RecipeIngredient> recipeIngredients = request.ingredients().stream()
                .map(riRequest -> {
                    Ingredient ingredient = ingredientRepository.findById(riRequest.ingredientId())
                            .orElseThrow(()-> new RuntimeException("Ingredient not found : " + riRequest.ingredientId()));

                    //SI id du RecipeIngredient existe = c’est une mise à jour
                    if (riRequest.id() != null) {
                        RecipeIngredient existing = recipe.getIngredients().stream()
                                .filter(ri -> ri.getId().equals(riRequest.id()))
                                .findFirst()
                                .orElseThrow(() -> new RuntimeException("RecipeIngredient not found: " + riRequest.id()));
                        // mise à jour quantité + unité
                        existing.setQuantityPerPerson(riRequest.quantityPerPerson());
                        existing.setUnit(riRequest.unit());

                        // vérifier si l’ingrédient a changé
                        if (!existing.getIngredient().getId().equals(riRequest.ingredientId())) {
                            // récupérer le nouvel ingrédient en base
                            Ingredient newIngredient = ingredientRepository.findById(riRequest.ingredientId())
                                    .orElseThrow(() -> new RuntimeException("Ingredient not found: " + riRequest.ingredientId()));

                            // remplacer l’ingrédient
                            existing.setIngredient(newIngredient);
                        }

                        return existing;
                    }else {
                        RecipeIngredient ri = new RecipeIngredient();

                        ri.setRecipe(recipe);
                        ri.setIngredient(ingredient);
                        ri.setQuantityPerPerson(riRequest.quantityPerPerson());
                        ri.setUnit(riRequest.unit());

                        return  ri;
                    }
                })
                .toList();

        // 4) Supprimer les anciens RecipeIngredient
        List<RecipeIngredient> oldList = recipe.getIngredients();

        List<RecipeIngredient> toRemove = oldList.stream()
                .filter(oldRi -> recipeIngredients.stream().noneMatch(newRi ->
                        newRi.getId() != null && newRi.getId().equals(oldRi.getId())
                ))
                .toList();

        // retirer les anciens → orphanRemoval = true les supprimera en base
        toRemove.forEach(oldList::remove);

        // 5) Remplacer la liste dans la recette
        recipe.setIngredients(recipeIngredients);

        // 6) Sauvegarder
        Recipe saved = recipeRepository.save(recipe);

        // 7) Retourner le DTO complet
        return recipeMapper.toDetailResponse(saved);

    }

    public void delete(Long id) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recipe not found : " + id));

        recipeRepository.delete(recipe);
    }
}