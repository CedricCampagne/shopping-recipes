package com.cedric.shoppingrecipes.ingredient.controller;

import com.cedric.shoppingrecipes.ingredient.Unit;
import com.cedric.shoppingrecipes.ingredient.dto.CreateIngredientRequest;
import com.cedric.shoppingrecipes.ingredient.dto.IngredientResponse;
import com.cedric.shoppingrecipes.ingredient.dto.UpdateIngredientRequest;
import com.cedric.shoppingrecipes.ingredient.entity.Ingredient;
import com.cedric.shoppingrecipes.ingredient.service.IngredientService;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ingredients")
public class IngredientController {

    private final IngredientService ingredientService;

    @GetMapping
    public List<IngredientResponse> findAll() {
        return ingredientService.findAll();
    }

    @GetMapping("/{id}")
    public IngredientResponse findById(@PathVariable Long id) {
        return ingredientService.findById(id);
    }

    @GetMapping("/name/{name}")
    public IngredientResponse findByName(@PathVariable String name) {
        return  ingredientService.findByName(name);
    }

    @GetMapping("/unit/{unit}")
    public  List<IngredientResponse> findByUnit(@PathVariable String unit) {
        return  ingredientService.findByUnit(unit);
    }

    @GetMapping("/units")
    public Unit[] getUnits(){
        return ingredientService.getUnits();
    }

    @PostMapping
    public IngredientResponse create(
            @RequestBody CreateIngredientRequest request
            )
    {
        return ingredientService.create(request);
    }

    @PutMapping("/{id}")
    public IngredientResponse update(
            @PathVariable Long id,
            @RequestBody UpdateIngredientRequest request
            )
    {
        return ingredientService.update(id, request);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id){
        ingredientService.delete(id);
    }
}