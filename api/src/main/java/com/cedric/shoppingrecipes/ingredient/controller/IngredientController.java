package com.cedric.shoppingrecipes.ingredient.controller;

import com.cedric.shoppingrecipes.ingredient.entity.Ingredient;
import com.cedric.shoppingrecipes.ingredient.service.IngredientService;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ingredients")
public class IngredientController {

    private final IngredientService ingredientService;

    @GetMapping
    public List<Ingredient> findAll() {
        return ingredientService.findAll();
    }

    @GetMapping("/{id}")
    public Ingredient findById(@PathVariable Long id) {
        return ingredientService.findById(id);
    }

    @GetMapping("/name/{name}")
    public Optional<Ingredient> findByName(@PathVariable String name) {
        return  ingredientService.findByName(name);
    }

    @GetMapping("/unit/{unit}")
    public  List<Ingredient> findByUnit(@PathVariable String unit) {
        return  ingredientService.findByUnit(unit);
    }
}