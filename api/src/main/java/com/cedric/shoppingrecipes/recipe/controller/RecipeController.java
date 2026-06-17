package com.cedric.shoppingrecipes.recipe.controller;

import com.cedric.shoppingrecipes.recipe.entity.Recipe;
import com.cedric.shoppingrecipes.recipe.service.RecipeService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recipes")
public class RecipeController {

    private final RecipeService recipeService;

    @GetMapping
    public List<Recipe> findAll() {
        return recipeService.findAll();
    }

    @GetMapping("/{id}")
    public Recipe findById(@PathVariable Long id) {
        return recipeService.findById(id);
    }

    @GetMapping("/name/{name}")
    public Recipe findByName(@PathVariable String name) {
        return recipeService.findByName(name);
    }

    @GetMapping("/search/{keyword}")
    public List<Recipe> searchByDescription(@PathVariable String keyword) {
        return recipeService.searchByDescription(keyword);
    }
}