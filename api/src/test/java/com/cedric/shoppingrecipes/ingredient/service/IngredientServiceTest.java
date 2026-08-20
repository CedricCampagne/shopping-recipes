package com.cedric.shoppingrecipes.ingredient.service;

import com.cedric.shoppingrecipes.ingredient.Unit;
import com.cedric.shoppingrecipes.ingredient.dto.CreateIngredientRequest;
import com.cedric.shoppingrecipes.ingredient.dto.IngredientResponse;
import com.cedric.shoppingrecipes.ingredient.entity.Ingredient;
import com.cedric.shoppingrecipes.ingredient.exception.IngredientConflictException;
import com.cedric.shoppingrecipes.ingredient.exception.IngredientNotFoundException;
import com.cedric.shoppingrecipes.ingredient.mapper.IngredientMapper;
import com.cedric.shoppingrecipes.ingredient.repository.IngredientRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class IngredientServiceTest {
    @Mock
    private IngredientRepository ingredientRepository;

    @Mock
    private IngredientMapper ingredientMapper;

    @InjectMocks
    private IngredientService ingredientService;

    @Nested
    class FindTests{

        @Test
        void shouldFindById(){
            //Arrange
            Long ingredientId = 1L;

            Ingredient ingredient = new Ingredient();
            ingredient.setId(ingredientId);
            ingredient.setName("tomato");
            ingredient.setUnit(Unit.pièce);

            when(ingredientRepository.findById(ingredientId))
                    .thenReturn(Optional.of(ingredient));

            IngredientResponse response = new IngredientResponse(
                    ingredientId,
                    ingredient.getName(),
                    ingredient.getUnit()
            );

            when(ingredientMapper.toResponse(ingredient))
                    .thenReturn(response);

            //Act
            IngredientResponse result = ingredientService.findById(ingredientId);

            //Assert
            assertEquals(response, result);
        }

        @Test
        void shouldThrowExceptionWhenIngredientDoesNotExistById(){
            //Arrange
            Long ingredientId = 1L;

            when(ingredientRepository.findById(ingredientId))
                    .thenReturn(Optional.empty());

            //si throw exception on a
            //Act + Assert
            assertThrows(
                    IngredientNotFoundException.class,
                    ()-> ingredientService.findById(ingredientId)
            );
        }

        @Test
        void shouldFindByName(){
            //Arrange
            Long ingredientId = 1L;

            Ingredient ingredient = new Ingredient();
            ingredient.setId(ingredientId);
            ingredient.setName("tomato");
            ingredient.setUnit(Unit.pièce);

            when(ingredientRepository.findByName(ingredient.getName()))
                    .thenReturn(Optional.of(ingredient));

            IngredientResponse response = new IngredientResponse(
                    ingredientId,
                    ingredient.getName(),
                    ingredient.getUnit()
            );

            when(ingredientMapper.toResponse(ingredient))
                    .thenReturn(response);

            //Act
            IngredientResponse result = ingredientService.findByName(ingredient.getName());

            //Assert
            assertEquals(response, result);
        }

        @Test
        void shouldThrowExceptionWhenIngredientDoesNotExistByName(){
            //Arrange
            String ingredientName = "tomato";

            when(ingredientRepository.findByName(ingredientName))
                    .thenReturn(Optional.empty());

            //si throw exception on a
            //Act + Assert
            assertThrows(
                    IngredientNotFoundException.class,
                    ()-> ingredientService.findByName(ingredientName)
            );
        }

        @Test
        void shouldFindIngredientByUnit(){
            //Arrange
            String unit = "piece";

            Long ingredient1Id = 1L;

            Ingredient ingredient1 = new Ingredient();
            ingredient1.setId(ingredient1Id);
            ingredient1.setName("tomato");
            ingredient1.setUnit(Unit.pièce);

            Long ingredient2Id = 2L;

            Ingredient ingredient2 = new Ingredient();
            ingredient2.setId(ingredient2Id);
            ingredient2.setName("oinion");
            ingredient2.setUnit(Unit.pièce);

            when(ingredientRepository.findByUnit(unit))
                    .thenReturn(List.of(ingredient1, ingredient2));

            IngredientResponse response1 = new IngredientResponse(
                    ingredient1Id,
                    ingredient1.getName(),
                    ingredient1.getUnit()
            );

            IngredientResponse response2 = new IngredientResponse(
                    ingredient2Id,
                    ingredient2.getName(),
                    ingredient2.getUnit()
            );

            when(ingredientMapper.toResponse(ingredient1))
                    .thenReturn(response1);

            when(ingredientMapper.toResponse(ingredient2))
                    .thenReturn(response2);

            //Act
            List<IngredientResponse> result = ingredientService.findByUnit(unit);

            //Assert
            assertEquals(
                    List.of(response1, response2),
                    result
            );
        }

        @Test
        void shouldReturnEmptyListWhenNoIngredientMatchesUnit() {
            // Arrange
            String unit = "piece";

            when(ingredientRepository.findByUnit(unit))
                    .thenReturn(List.of());

            // Act
            List<IngredientResponse> result =
                    ingredientService.findByUnit(unit);

            // Assert
            // assertTrue(condition) si condition est vrai ca passe
            assertTrue(result.isEmpty());
        }

        @Test
        void shouldFindAll(){
            //Arrange


            Long ingredient1Id = 1L;

            Ingredient ingredient1 = new Ingredient();
            ingredient1.setId(ingredient1Id);
            ingredient1.setName("tomato");
            ingredient1.setUnit(Unit.pièce);

            Long ingredient2Id = 2L;

            Ingredient ingredient2 = new Ingredient();
            ingredient2.setId(ingredient2Id);
            ingredient2.setName("oinion");
            ingredient2.setUnit(Unit.pièce);

            when(ingredientRepository.findAll())
                    .thenReturn(List.of(ingredient1, ingredient2));

            IngredientResponse response1 = new IngredientResponse(
                    ingredient1Id,
                    ingredient1.getName(),
                    ingredient1.getUnit()
            );

            IngredientResponse response2 = new IngredientResponse(
                    ingredient2Id,
                    ingredient2.getName(),
                    ingredient2.getUnit()
            );

            when(ingredientMapper.toResponse(ingredient1))
                    .thenReturn(response1);

            when(ingredientMapper.toResponse(ingredient2))
                    .thenReturn(response2);

            //Act
            List<IngredientResponse> result = ingredientService.findAll();

            //Assert
            assertEquals(
                    List.of(response1, response2),
                    result
            );
        }

        @Test
        void shouldFindAllEmpty() {
            // Arrange

            when(ingredientRepository.findAll())
                    .thenReturn(List.of());

            // Act
            List<IngredientResponse> result =
                    ingredientService.findAll();

            // Assert
            // assertTrue(condition) si condition est vrai ca passe
            assertTrue(result.isEmpty());
        }
    }

    @Nested
    class CreateTests {

        @Test
        void shouldCreateIngredient(){
            //Arrange
            CreateIngredientRequest request = new CreateIngredientRequest(
                   "tomato",
                   Unit.pièce
            );

            Ingredient ingredient = new Ingredient();
            ingredient.setName(request.name());
            ingredient.setUnit(request.unit());

            when(ingredientRepository.findByName(request.name()))
                    .thenReturn(Optional.empty());

            when(ingredientMapper.toEntity(request))
                    .thenReturn(ingredient);

            Ingredient savedIngredient = new Ingredient();
            savedIngredient.setId(1L);
            savedIngredient.setName(ingredient.getName());
            savedIngredient.setUnit(ingredient.getUnit());

            when(ingredientRepository.save(ingredient))
                    .thenReturn(savedIngredient);

            IngredientResponse response = new IngredientResponse(
                    savedIngredient.getId(),
                    savedIngredient.getName(),
                    savedIngredient.getUnit()
            );

            when(ingredientMapper.toResponse(savedIngredient))
                    .thenReturn(response);

            //Act
            IngredientResponse result = ingredientService.create(request);

            //Assert
            assertEquals(response, result);
        }

        @Test
        void shouldThrowIngredientConflictException(){
            //Arrange
            CreateIngredientRequest request = new CreateIngredientRequest(
                    "tomato",
                    Unit.pièce
            );

            Ingredient ingredient = new Ingredient();
            ingredient.setName(request.name());
            ingredient.setUnit(request.unit());

            when(ingredientRepository.findByName(request.name()))
                    .thenReturn(Optional.of(ingredient));

            //Act + Assert
            assertThrows(
                    IngredientConflictException.class,
                    ()-> ingredientService.create(request)
            );
        }
    }
}
