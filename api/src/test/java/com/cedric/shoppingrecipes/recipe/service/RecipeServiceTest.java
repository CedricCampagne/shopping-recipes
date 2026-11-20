package com.cedric.shoppingrecipes.recipe.service;

import com.cedric.shoppingrecipes.ingredient.Unit;
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
import org.mockito.ArgumentCaptor;

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
            tomato.setUnit(Unit.g);

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
            tomato.setUnit(Unit.g);

            Ingredient onion = new Ingredient();
            onion.setId(onionId);
            onion.setName("Oignon");
            onion.setUnit(Unit.g);

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

        @Test
        void shouldUseIngredientUnitInsteadOfRequestedUnit() {
            Ingredient pasta = new Ingredient();
            pasta.setId(1L);
            pasta.setName("Pâtes");
            pasta.setUnit(Unit.g);

            CreateRecipeRequest request = new CreateRecipeRequest(
                    "Pâtes au beurre",
                    "Une recette simple",
                    4,
                    List.of(new CreateRecipeIngredientRequest(1L, 100.0, "kg"))
            );

            when(ingredientRepository.findById(1L)).thenReturn(Optional.of(pasta));
            when(recipeRepository.save(any(Recipe.class))).thenAnswer(inv -> inv.getArgument(0));

            recipeService.create(request);

            ArgumentCaptor<Recipe> captor = ArgumentCaptor.forClass(Recipe.class);
            verify(recipeRepository).save(captor.capture());
            assertEquals("g", captor.getValue().getIngredients().get(0).getUnit());
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

            when(shoppingListRepository.existsByRecipes_Recipe_Id(recipeId))
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

            when(shoppingListRepository.existsByRecipes_Recipe_Id(recipeId))
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

            when(shoppingListRepository.existsByRecipes_Recipe_Id(recipeId))
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
            //tomato.setUnit(Unit.g);

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
            tomato.setUnit(Unit.g);

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
            onion.setUnit(Unit.g);

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
            tomato.setUnit(Unit.g);

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
            onion.setUnit(Unit.g);

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

    @Test
    void shouldKeepUnitOfExistingLineAndUseIngredientUnitForNewLine() {
        Ingredient butter = new Ingredient();
        butter.setId(1L);
        butter.setName("Beurre");
        butter.setUnit(Unit.kg);   // l'ingrédient est passé en kg

        Ingredient oil = new Ingredient();
        oil.setId(2L);
        oil.setName("Huile");
        oil.setUnit(Unit.ml);

        Recipe recipe = new Recipe();
        recipe.setId(1L);

        RecipeIngredient existing = new RecipeIngredient();
        existing.setId(10L);
        existing.setRecipe(recipe);
        existing.setIngredient(butter);
        existing.setQuantityPerPerson(100.0);
        existing.setUnit("g");     // la ligne a été saisie quand c'était des grammes
        recipe.setIngredients(new ArrayList<>(List.of(existing)));

        UpdateRecipeRequest request = new UpdateRecipeRequest(
                "Gâteau", "Une description assez longue", 4,
                List.of(
                        new UpdateRecipeIngredientRequest(10L, 1L, 150.0, "kg"),
                        new UpdateRecipeIngredientRequest(null, 2L, 5.0, "g")
                )
        );

        when(recipeRepository.findByIdWithIngredients(1L)).thenReturn(recipe);
        when(ingredientRepository.findById(1L)).thenReturn(Optional.of(butter));
        when(ingredientRepository.findById(2L)).thenReturn(Optional.of(oil));
        when(recipeRepository.save(any(Recipe.class))).thenAnswer(inv -> inv.getArgument(0));

        recipeService.update(1L, request);

        ArgumentCaptor<Recipe> captor = ArgumentCaptor.forClass(Recipe.class);
        verify(recipeRepository).save(captor.capture());
        List<RecipeIngredient> lines = captor.getValue().getIngredients();
        assertEquals("g", lines.get(0).getUnit());    // ligne existante : unité conservée
        assertEquals("ml", lines.get(1).getUnit());   // nouvelle ligne : unité de l'ingrédient
    }

    @Test
    void shouldUseNewIngredientUnitWhenIngredientOfExistingLineChanges() {
        Ingredient pasta = new Ingredient();
        pasta.setId(1L);
        pasta.setName("Pâtes");
        pasta.setUnit(Unit.g);

        Ingredient cream = new Ingredient();
        cream.setId(4L);
        cream.setName("Crème fraîche");
        cream.setUnit(Unit.ml);

        Recipe recipe = new Recipe();
        recipe.setId(1L);

        RecipeIngredient existing = new RecipeIngredient();
        existing.setId(10L);
        existing.setRecipe(recipe);
        existing.setIngredient(pasta);
        existing.setQuantityPerPerson(100.0);
        existing.setUnit("g");
        recipe.setIngredients(new ArrayList<>(List.of(existing)));

        UpdateRecipeRequest request = new UpdateRecipeRequest(
                "Pâtes", "Une description assez longue", 4,
                List.of(new UpdateRecipeIngredientRequest(10L, 4L, 20.0, "g"))
        );

        when(recipeRepository.findByIdWithIngredients(1L)).thenReturn(recipe);
        when(ingredientRepository.findById(4L)).thenReturn(Optional.of(cream));
        when(recipeRepository.save(any(Recipe.class))).thenAnswer(inv -> inv.getArgument(0));

        recipeService.update(1L, request);

        ArgumentCaptor<Recipe> captor = ArgumentCaptor.forClass(Recipe.class);
        verify(recipeRepository).save(captor.capture());
        assertEquals("ml", captor.getValue().getIngredients().get(0).getUnit());
    }
}
