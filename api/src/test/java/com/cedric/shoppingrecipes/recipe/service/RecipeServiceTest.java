package com.cedric.shoppingrecipes.recipe.service;

import com.cedric.shoppingrecipes.ingredient.entity.Ingredient;
import com.cedric.shoppingrecipes.ingredient.exception.IngredientNotFoundException;
import com.cedric.shoppingrecipes.ingredient.repository.IngredientRepository;

import com.cedric.shoppingrecipes.recipe.dto.CreateRecipeRequest;
import com.cedric.shoppingrecipes.recipe.dto.RecipeDetailResponse;
import com.cedric.shoppingrecipes.recipe.dto.UpdateRecipeRequest;
import com.cedric.shoppingrecipes.recipe.entity.Recipe;
import com.cedric.shoppingrecipes.recipe.exception.RecipeConflictException;
import com.cedric.shoppingrecipes.recipe.exception.RecipeNotFoundException;
import com.cedric.shoppingrecipes.recipe.mapper.RecipeMapper;
import com.cedric.shoppingrecipes.recipe.repository.RecipeRepository;
import com.cedric.shoppingrecipes.recipeingredient.dto.CreateRecipeIngredientRequest;

import com.cedric.shoppingrecipes.recipeingredient.dto.UpdateRecipeIngredientRequest;
import com.cedric.shoppingrecipes.recipeingredient.entity.RecipeIngredient;

import com.cedric.shoppingrecipes.shoppinglist.repository.ShoppingListRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
public class RecipeServiceTest {
    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private RecipeMapper recipeMapper;

    @Mock
    private IngredientRepository ingredientRepository;

    @Mock
    private ShoppingListRepository shoppingListRepository;

    @InjectMocks
    private RecipeService recipeService;

    @Nested
    class FindByIdTests {
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
    }

    @Nested
    class CreatedTests {
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

        @Test
        void shouldCreateRecipeWithMultipleIngredients() {
            // Arrange
            Long tomatoId = 1L;
            Long onionId = 2L;

            Ingredient tomato = new Ingredient();
            tomato.setId(tomatoId);
            tomato.setName("Tomate");

            Ingredient onion = new Ingredient();
            onion.setId(onionId);
            onion.setName("Oignon");

            CreateRecipeIngredientRequest tomatoRequest =
                    new CreateRecipeIngredientRequest(
                            tomatoId,
                            1.0,
                            "piece"
                    );

            CreateRecipeIngredientRequest onionRequest =
                    new CreateRecipeIngredientRequest(
                            onionId,
                            0.5,
                            "piece"
                    );

            CreateRecipeRequest request =
                    new CreateRecipeRequest(
                            "Salade",
                            "Une salade simple",
                            4,
                            List.of(
                                    tomatoRequest,
                                    onionRequest
                            )
                    );

            when(ingredientRepository.findById(tomatoId))
                    .thenReturn(Optional.of(tomato));

            when(ingredientRepository.findById(onionId))
                    .thenReturn(Optional.of(onion));

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

            // Act
            RecipeDetailResponse result = recipeService.create(request);

            // Assert
            assertEquals(response, result);
        }

        @Test
        void shouldThrowExceptionWhenIngredientDoesNotExist(){
            //Arrange
            Long ingredientId = 1L;

            CreateRecipeIngredientRequest ingredientRequest =
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
                            List.of(ingredientRequest)
                    );

            when(ingredientRepository.findById(ingredientId))
                    .thenReturn(Optional.empty());

            //Act + Assert
            assertThrows(
                    IngredientNotFoundException.class,
                    () -> recipeService.create(request)
            );
        }
    }

    @Nested
    class DeleteTests {

        @Test
        void shouldDeleteRecipe(){
            //Arrange
            Long recipeId = 1L;

            Recipe recipe = new Recipe();
            recipe.setId(recipeId);

            when(shoppingListRepository.existsByRecipes_Id(recipeId))
                    .thenReturn(false);

            when(recipeRepository.findById(recipeId))
                    .thenReturn(Optional.of(recipe));

            //Act
            recipeService.delete(recipeId);

            //Assert
            verify(recipeRepository).delete(recipe);
        }

        @Test
        void shouldThrowRecipeNotFoundException(){
            //Arrange
            Long recipeId = 1L;

            when(shoppingListRepository.existsByRecipes_Id(recipeId))
                    .thenReturn(false);

            when(recipeRepository.findById(recipeId))
                    .thenReturn(Optional.empty());

            //Act + Assert
            assertThrows(
                    RecipeNotFoundException.class,
                    ()-> recipeService.delete(recipeId)
            );
        }

        @Test
        void shouldThrowRecipeConflictException(){
            //Arrange
            Long recipeId = 1L;

            when(shoppingListRepository.existsByRecipes_Id(recipeId))
                    .thenReturn(true);

            assertThrows(
                    RecipeConflictException.class,
                    ()-> recipeService.delete(recipeId)
            );
        }
    }

    @Nested
    class UpdateTests {

        @Test
        void shouldUpdateRecipe(){
            Long recipeId = 1L;

            Recipe recipe = new Recipe();
            recipe.setId(recipeId);
            recipe.setName("Salade");
            recipe.setDescription("Ancienne description");
            recipe.setServings(2);

            Long ingredientId = 1L;

            Ingredient tomato = new Ingredient();
            tomato.setId(ingredientId);
            tomato.setName("Tomate");

            Long recipeIngredientId = 10L;

            RecipeIngredient recipeIngredient = new RecipeIngredient();
            recipeIngredient.setId(recipeIngredientId);
            recipeIngredient.setRecipe(recipe);
            recipeIngredient.setIngredient(tomato);
            recipeIngredient.setQuantityPerPerson(1.0);
            recipeIngredient.setUnit("piece");

            recipe.setIngredients(new ArrayList<>(List.of(recipeIngredient)));

            UpdateRecipeIngredientRequest ingredientRequest =
                    new UpdateRecipeIngredientRequest(
                            recipeIngredientId, // id existant
                            ingredientId,       // tomate
                            2.0,
                            "piece"
                    );

            UpdateRecipeRequest request =
                    new UpdateRecipeRequest(
                            "Salade améliorée",
                            "Une meilleure salade",
                            4,
                            List.of(ingredientRequest)
                    );

            when(recipeRepository.findByIdWithIngredients(recipeId))
                    .thenReturn(recipe);

            when(ingredientRepository.findById(ingredientId))
                    .thenReturn(Optional.of(tomato));

            when(recipeRepository.save(any(Recipe.class)))
                    .thenReturn(recipe);

            RecipeDetailResponse response = new RecipeDetailResponse(
                    recipeId,
                    "Salade améliorée",
                    "Une meilleure salade",
                    4,
                    List.of()
            );

            when(recipeMapper.toDetailResponse(recipe))
                    .thenReturn(response);

            //Act
            RecipeDetailResponse result = recipeService.update(recipeId, request);

            //Assert
            assertEquals(response, result);
        }
    @Test
        void shouldUpdateRecipeWithNewIngredient(){
            Long recipeId = 1L;

            Recipe recipe = new Recipe();
            recipe.setId(recipeId);
            recipe.setName("Salade");
            recipe.setDescription("Ancienne description");
            recipe.setServings(2);

            Long ingredientId = 1L;

            Ingredient tomato = new Ingredient();
            tomato.setId(ingredientId);
            tomato.setName("Tomate");

            Long recipeIngredientId = 10L;

            RecipeIngredient recipeIngredient = new RecipeIngredient();
            recipeIngredient.setId(recipeIngredientId);
            recipeIngredient.setRecipe(recipe);
            recipeIngredient.setIngredient(tomato);
            recipeIngredient.setQuantityPerPerson(1.0);
            recipeIngredient.setUnit("piece");

            recipe.setIngredients(new ArrayList<>(List.of(recipeIngredient)));

            Long onionId = 2L;

            Ingredient onion = new Ingredient();
            onion.setId(onionId);
            onion.setName("Oignon");

            UpdateRecipeIngredientRequest onionRequest =
                    new UpdateRecipeIngredientRequest(
                            null,       // ⭐ nouveau RecipeIngredient
                            onionId,
                            0.5,
                            "piece"
                    );

            UpdateRecipeIngredientRequest tomatoRequest =
                    new UpdateRecipeIngredientRequest(
                            recipeIngredientId, // ID existant
                            ingredientId,
                            1.0,
                            "piece"
                    );

            UpdateRecipeRequest request =
                    new UpdateRecipeRequest(
                            "Salade avec oignon",
                            "Une salade améliorée",
                            4,
                            List.of(
                                    tomatoRequest, // existant → conservé
                                    onionRequest   // id null → nouveau
                            )
                    );

            when(recipeRepository.findByIdWithIngredients(recipeId))
                    .thenReturn(recipe);

            when(ingredientRepository.findById(ingredientId))
                    .thenReturn(Optional.of(tomato));

            when(ingredientRepository.findById(onionId))
                    .thenReturn(Optional.of(onion));

            when(recipeRepository.save(any(Recipe.class)))
                    .thenReturn(recipe);

            RecipeDetailResponse response = new RecipeDetailResponse(
                    recipeId,
                    "Salade améliorée",
                    "Une meilleure salade",
                    4,
                    List.of()
            );

            when(recipeMapper.toDetailResponse(recipe))
                    .thenReturn(response);

            //Act
            RecipeDetailResponse result = recipeService.update(recipeId, request);

            //Assert
            assertEquals(response, result);
        }

        @Test
        void shouldUpdateRecipeWhenIngredientChanged(){
            Long recipeId = 1L;

            Recipe recipe = new Recipe();
            recipe.setId(recipeId);
            recipe.setName("Salade");
            recipe.setDescription("Ancienne description");
            recipe.setServings(2);

            Long ingredientId = 1L;

            Ingredient tomato = new Ingredient();
            tomato.setId(ingredientId);
            tomato.setName("Tomate");

            Long recipeIngredientId = 10L;

            RecipeIngredient recipeIngredient = new RecipeIngredient();
            recipeIngredient.setId(recipeIngredientId);
            recipeIngredient.setRecipe(recipe);
            recipeIngredient.setIngredient(tomato);
            recipeIngredient.setQuantityPerPerson(1.0);
            recipeIngredient.setUnit("piece");

            recipe.setIngredients(new ArrayList<>(List.of(recipeIngredient)));

            Long onionId = 2L;

            Ingredient onion = new Ingredient();
            onion.setId(onionId);
            onion.setName("Oignon");

            UpdateRecipeIngredientRequest onionRequest =
                    new UpdateRecipeIngredientRequest(
                            recipeIngredientId,       // ⭐ nouveau RecipeIngredient
                            onionId,
                            0.5,
                            "piece"
                    );

            UpdateRecipeRequest request =
                    new UpdateRecipeRequest(
                            "Salade avec oignon",
                            "Une salade améliorée",
                            4,
                            List.of(
                                    onionRequest
                            )
                    );

            when(recipeRepository.findByIdWithIngredients(recipeId))
                    .thenReturn(recipe);

            when(ingredientRepository.findById(onionId))
                    .thenReturn(Optional.of(onion));

            when(recipeRepository.save(any(Recipe.class)))
                    .thenReturn(recipe);

            RecipeDetailResponse response = new RecipeDetailResponse(
                    recipeId,
                    "Salade améliorée",
                    "Une meilleure salade",
                    4,
                    List.of()
            );

            when(recipeMapper.toDetailResponse(recipe))
                    .thenReturn(response);

            //Act
            RecipeDetailResponse result = recipeService.update(recipeId, request);

            //Assert
            assertEquals(response, result);
        }
    }
}
