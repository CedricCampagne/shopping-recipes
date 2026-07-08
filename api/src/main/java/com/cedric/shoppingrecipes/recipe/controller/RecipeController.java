package com.cedric.shoppingrecipes.recipe.controller;

import com.cedric.shoppingrecipes.recipe.dto.RecipeDetailResponse;
import com.cedric.shoppingrecipes.recipe.dto.RecipeResponse;
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
    public List<RecipeResponse> findAll() {
        return recipeService.findAll();
    }

    @GetMapping("/{id}")
    public RecipeDetailResponse findById(@PathVariable Long id) {
        return recipeService.findById(id);
    }

    @GetMapping("/name/{name}")
    public RecipeDetailResponse findByName(@PathVariable String name) {
        return recipeService.findByName(name);
    }

    @GetMapping("/search/{keyword}")
    public List<RecipeDetailResponse> searchByDescription(@PathVariable String keyword) {
        return recipeService.searchByDescription(keyword);
    }
}