package com.cedric.shoppingrecipes.recipe.service;

import com.cedric.shoppingrecipes.ingredient.entity.Ingredient;
import com.cedric.shoppingrecipes.ingredient.repository.IngredientRepository;
import com.cedric.shoppingrecipes.recipe.dto.CreateRecipeRequest;
import com.cedric.shoppingrecipes.recipe.dto.RecipeDetailResponse;
import com.cedric.shoppingrecipes.recipe.entity.Recipe;
import com.cedric.shoppingrecipes.recipe.exception.RecipeNotFoundException;
import com.cedric.shoppingrecipes.recipe.mapper.RecipeMapper;
import com.cedric.shoppingrecipes.recipe.repository.RecipeRepository;
import com.cedric.shoppingrecipes.recipeingredient.dto.CreateRecipeIngredientRequest;
import com.cedric.shoppingrecipes.recipeingredient.entity.RecipeIngredient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class RecipeServiceTest {
    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private RecipeMapper recipeMapper;

    @Mock
    private IngredientRepository ingredientRepository;

    @InjectMocks
    private RecipeService recipeService;

    @Test
    void shouldFindRecipeById(){
        //Arrange
        Long id = 1L;

        Recipe recipe = new Recipe();
        recipe.setId(id);
        recipe.setName("Salade");

        when(recipeRepository.findById(id))
                .thenReturn(Optional.of(recipe));

        RecipeDetailResponse response = new RecipeDetailResponse(
                id,
                "Salade",
                "Une salade simple",
                4,
                List.of()
        );

        when(recipeMapper.toDetailResponse(recipe))
                .thenReturn(response);

        //Act
        RecipeDetailResponse result = recipeService.findById(id);

        //Assert
        // (gauche ce que je veux, droite ce que mon code retourne)
        assertEquals(response, result);
    }

    @Test
    void shouldThrowExceptionWhenRecipeDoesNotExist(){
        //Arrange
        Long id = 1L;

        when(recipeRepository.findById(id))
                .thenReturn(Optional.empty());

        //Act + Assert
        assertThrows(
                RecipeNotFoundException.class,
                ()-> recipeService.findById(id)
        );
    }

    @Test
    void shouldCreateRecipeWithOneIngredient(){
        //Arrange
        Long ingredientId = 1L;
        Ingredient tomato = new Ingredient();
        tomato.setId(ingredientId);
        tomato.setName("Tomate");

        CreateRecipeIngredientRequest createRecipeIngredientRequest =
                new CreateRecipeIngredientRequest(
                        ingredientId,
                        1.0,
                        "piece"
                );

        CreateRecipeRequest request =
                new CreateRecipeRequest(
                      "Salade",
                      "Une salade simple",
                      4,
                      List.of(createRecipeIngredientRequest)
                );

        when(ingredientRepository.findById(ingredientId))
                .thenReturn(Optional.of(tomato));

        Recipe savedRecipe = new Recipe();
        savedRecipe.setId(1L);
        savedRecipe.setName("Salade");
        savedRecipe.setDescription("Une salade simple");
        savedRecipe.setServings(4);

        when(recipeRepository.save(any(Recipe.class)))
                .thenReturn(savedRecipe);

        RecipeDetailResponse response = new RecipeDetailResponse(
                1L,
                "Salade",
                "Une salade simple",
                4,
                List.of()
        );

        when(recipeMapper.toDetailResponse(savedRecipe))
                .thenReturn(response);

        //Act
        RecipeDetailResponse result = recipeService.create(request);

        //Assert
        assertEquals(response, result);
    }
}
