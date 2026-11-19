package com.cedric.shoppingrecipes.recipe.dto;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.cedric.shoppingrecipes.recipeingredient.dto.CreateRecipeIngredientRequest;
import com.cedric.shoppingrecipes.recipeingredient.dto.UpdateRecipeIngredientRequest;

import jakarta.validation.Validation;
import jakarta.validation.Validator;

class RecipeIngredientsValidationTest {

    private final Validator validator =
            Validation.buildDefaultValidatorFactory().getValidator();

    private static final String DESCRIPTION = "Une description assez longue";

    private <T> boolean hasViolationAt(T request, String path) {
        return validator.validate(request).stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals(path));
    }

    private CreateRecipeRequest create(List<CreateRecipeIngredientRequest> lines) {
        return new CreateRecipeRequest("Tarte", DESCRIPTION, 4, lines);
    }

    private UpdateRecipeRequest update(List<UpdateRecipeIngredientRequest> lines) {
        return new UpdateRecipeRequest("Tarte", DESCRIPTION, 4, lines);
    }

    private CreateRecipeRequest createWithLine(Long ingredientId, Double quantity, String unit) {
        return create(List.of(new CreateRecipeIngredientRequest(ingredientId, quantity, unit)));
    }

    private UpdateRecipeRequest updateWithLine(Long id, Long ingredientId, Double quantity, String unit) {
        return update(List.of(new UpdateRecipeIngredientRequest(id, ingredientId, quantity, unit)));
    }

    // --- Create ---

    @Test
    void create_shouldAcceptValidRequest() {
        assertTrue(validator.validate(createWithLine(1L, 0.5, "g")).isEmpty());
    }

    @Test
    void create_shouldRejectEmptyOrNullList() {
        assertTrue(hasViolationAt(create(List.of()), "ingredients"));
        assertTrue(hasViolationAt(create(null), "ingredients"));
    }

    @Test
    void create_shouldRejectQuantityZeroOrNegative() {
        assertTrue(hasViolationAt(createWithLine(1L, 0.0, "g"), "ingredients[0].quantityPerPerson"));
        assertTrue(hasViolationAt(createWithLine(1L, -2.0, "g"), "ingredients[0].quantityPerPerson"));
    }

    @Test
    void create_shouldRejectMissingQuantity() {
        assertTrue(hasViolationAt(createWithLine(1L, null, "g"), "ingredients[0].quantityPerPerson"));
    }

    @Test
    void create_shouldRejectQuantityWithMoreThanTwoDecimals() {
        assertTrue(hasViolationAt(createWithLine(1L, 0.005, "g"), "ingredients[0].quantityPerPerson"));
    }

    @Test
    void create_shouldRejectMissingIngredientId() {
        assertTrue(hasViolationAt(createWithLine(null, 1.0, "g"), "ingredients[0].ingredientId"));
    }

    @Test
    void create_shouldRejectBlankOrTooLongUnit() {
        assertTrue(hasViolationAt(createWithLine(1L, 1.0, ""), "ingredients[0].unit"));
        assertTrue(hasViolationAt(createWithLine(1L, 1.0, "a".repeat(51)), "ingredients[0].unit"));
    }

    // --- Update ---

    @Test
    void update_shouldAcceptNewLineAndExistingLine() {
        assertTrue(validator.validate(updateWithLine(null, 1L, 0.5, "g")).isEmpty());
        assertTrue(validator.validate(updateWithLine(5L, 1L, 0.5, "g")).isEmpty());
    }

    @Test
    void update_shouldRejectEmptyOrNullList() {
        assertTrue(hasViolationAt(update(List.of()), "ingredients"));
        assertTrue(hasViolationAt(update(null), "ingredients"));
    }

    @Test
    void update_shouldRejectInvalidQuantity() {
        assertTrue(hasViolationAt(updateWithLine(null, 1L, 0.0, "g"), "ingredients[0].quantityPerPerson"));
        assertTrue(hasViolationAt(updateWithLine(null, 1L, 0.005, "g"), "ingredients[0].quantityPerPerson"));
    }

    @Test
    void update_shouldRejectMissingIngredientIdAndBlankUnit() {
        assertTrue(hasViolationAt(updateWithLine(null, null, 1.0, "g"), "ingredients[0].ingredientId"));
        assertTrue(hasViolationAt(updateWithLine(null, 1L, 1.0, " "), "ingredients[0].unit"));
    }
}