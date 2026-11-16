package com.cedric.shoppingrecipes.shoppinglist.service;

import com.cedric.shoppingrecipes.ingredient.entity.Ingredient;
import com.cedric.shoppingrecipes.recipe.entity.Recipe;
import com.cedric.shoppingrecipes.recipe.repository.RecipeRepository;
import com.cedric.shoppingrecipes.recipeingredient.entity.RecipeIngredient;
import com.cedric.shoppingrecipes.recipeingredient.repository.RecipeIngredientRepository;
import com.cedric.shoppingrecipes.shoppinglist.dto.CreateShoppingListRequest;
import com.cedric.shoppingrecipes.shoppinglist.dto.ShoppingListItemResponse;
import com.cedric.shoppingrecipes.shoppinglist.dto.ShoppingListResponse;
import com.cedric.shoppingrecipes.shoppinglist.mapper.ShoppingListMapper;
import com.cedric.shoppingrecipes.shoppinglist.repository.ShoppingListRepository;
import com.cedric.shoppingrecipes.user.entity.User;
import com.cedric.shoppingrecipes.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShoppingListServiceTest {

    @Mock private RecipeRepository recipeRepository;
    @Mock private RecipeIngredientRepository recipeIngredientRepository;
    @Mock private ShoppingListRepository shoppingListRepository;
    @Mock private UserRepository userRepository;
    @Spy private ShoppingListMapper shoppingListMapper = new ShoppingListMapper();

    @InjectMocks private ShoppingListService shoppingListService;

    private Ingredient butter;

    @BeforeEach
    void setUp() {
        // getCurrentUser() lit l'utilisateur dans le SecurityContext : on le simule
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("test@example.com", null, List.of()));

        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        when(shoppingListRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        butter = new Ingredient();
        butter.setId(1L);
        butter.setName("Beurre");
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldNotMergeSameIngredientWithDifferentUnits() {
        stubRecipe(1L, "Recette g", 50.0, "g");
        stubRecipe(2L, "Recette kg", 0.1, "kg");

        ShoppingListResponse response = shoppingListService.createShoppingList(
                request(selection(1L, 4), selection(2L, 4)));

        assertThat(response.items()).hasSize(2);
        assertThat(quantityOf(response, "g")).isCloseTo(200.0, within(0.0001));
        assertThat(quantityOf(response, "kg")).isCloseTo(0.4, within(0.0001));
    }

    @Test
    void shouldMergeSameIngredientWithSameUnit() {
        stubRecipe(1L, "Recette 1", 50.0, "g");
        stubRecipe(2L, "Recette 2", 25.0, "g");

        ShoppingListResponse response = shoppingListService.createShoppingList(
                request(selection(1L, 4), selection(2L, 2)));

        assertThat(response.items()).hasSize(1);
        assertThat(quantityOf(response, "g")).isCloseTo(250.0, within(0.0001));
    }

    // ---- outils du test ----

    private void stubRecipe(Long recipeId, String name, double quantityPerPerson, String unit) {
        Recipe recipe = new Recipe();
        recipe.setId(recipeId);
        recipe.setName(name);

        RecipeIngredient ri = new RecipeIngredient();
        ri.setRecipe(recipe);
        ri.setIngredient(butter);
        ri.setQuantityPerPerson(quantityPerPerson);
        ri.setUnit(unit);

        when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(recipe));
        when(recipeIngredientRepository.findByRecipeId(recipeId)).thenReturn(List.of(ri));
    }

    private CreateShoppingListRequest.RecipeSelection selection(Long recipeId, int servings) {
        return new CreateShoppingListRequest.RecipeSelection(recipeId, servings);
    }

    private CreateShoppingListRequest request(CreateShoppingListRequest.RecipeSelection... selections) {
        return new CreateShoppingListRequest(List.of(selections));
    }

    private double quantityOf(ShoppingListResponse response, String unit) {
        return response.items().stream()
                .filter(i -> i.unit().equals(unit))
                .map(ShoppingListItemResponse::totalQuantity)
                .findFirst()
                .orElseThrow();
    }
}
