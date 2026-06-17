package com.cedric.shoppingrecipes.recipeingredient.controller;

import com.cedric.shoppingrecipes.recipeingredient.entity.RecipeIngredient;
import com.cedric.shoppingrecipes.recipeingredient.service.RecipeIngredientService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recipe-ingredients")
public class RecipeIngredientController {

    private final RecipeIngredientService recipeIngredientService;

    @GetMapping
    public List<RecipeIngredient> findAll() {
        return recipeIngredientService.findAll();
    }

    @GetMapping("/{id}")
    public RecipeIngredient findById(@PathVariable Long id) {
        return recipeIngredientService.findById(id);
    }

    @PostMapping
    public RecipeIngredient create(@RequestBody RecipeIngredient recipeIngredient) {
        return recipeIngredientService.save(recipeIngredient);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        recipeIngredientService.delete(id);
    }

    @GetMapping("/recipe/{recipeId}")
    public List<RecipeIngredient> findByRecipe(@PathVariable Long recipeId) {
        return recipeIngredientService.findByRecipeId(recipeId);
    }

}