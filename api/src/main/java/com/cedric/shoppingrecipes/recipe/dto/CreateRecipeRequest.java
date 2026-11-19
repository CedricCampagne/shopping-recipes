package com.cedric.shoppingrecipes.recipe.dto;

import com.cedric.shoppingrecipes.recipeingredient.dto.CreateRecipeIngredientRequest;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CreateRecipeRequest(
        @NotBlank(message = "Le nom de la recette est obligatoire")
        @Size(max = 255, message = "Le nom de la recette ne doit pas dépasser 255 caractères")
        String name,

        @NotBlank(message = "La description est obligatoire")
        String description,

        @NotNull(message = "Le nombre de portions est obligatoire")
        @Min(value = 1, message = "Le nombre de portions doit être au moins 1")
        Integer servings,

        @NotEmpty(message = "La recette doit contenir au moins un ingrédient")
        @Valid
        List<CreateRecipeIngredientRequest> ingredients
) {}