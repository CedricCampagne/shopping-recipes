package com.cedric.shoppingrecipes.recipe.service;

import com.cedric.shoppingrecipes.recipe.dto.RecipeDetailResponse;
import com.cedric.shoppingrecipes.recipe.entity.Recipe;
import com.cedric.shoppingrecipes.recipe.mapper.RecipeMapper;
import com.cedric.shoppingrecipes.recipe.repository.RecipeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class RecipeServiceTest {
    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private RecipeMapper recipeMapper;

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
}
