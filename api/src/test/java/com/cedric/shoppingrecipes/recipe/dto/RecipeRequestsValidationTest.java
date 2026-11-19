package com.cedric.shoppingrecipes.recipe.dto;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

class RecipeRequestsValidationTest {

    private final Validator validator =
            Validation.buildDefaultValidatorFactory().getValidator();

    private static final String VALID_DESCRIPTION = "Une description assez longue";

    private <T> boolean violates(T request, String property, Class<? extends Annotation> rule) {
        Set<ConstraintViolation<T>> violations = validator.validateProperty(request, property);
        return violations.stream()
                .anyMatch(v -> rule.isInstance(v.getConstraintDescriptor().getAnnotation()));
    }

    private <T> boolean isValid(T request, String property) {
        return validator.validateProperty(request, property).isEmpty();
    }

    private CreateRecipeRequest create(String name, String description, Integer servings) {
        return new CreateRecipeRequest(name, description, servings, List.of());
    }

    private UpdateRecipeRequest update(String name, String description, Integer servings) {
        return new UpdateRecipeRequest(name, description, servings, List.of());
    }

    // --- CreateRecipeRequest ---

    @Test
    void create_shouldAcceptValidFields() {
        CreateRecipeRequest r = create("a".repeat(255), VALID_DESCRIPTION, 1);
        assertTrue(isValid(r, "name"));
        assertTrue(isValid(r, "description"));
        assertTrue(isValid(r, "servings"));
    }

    @Test
    void create_shouldRejectBlankName() {
        assertTrue(violates(create("", VALID_DESCRIPTION, 4), "name", NotBlank.class));
        assertTrue(violates(create(null, VALID_DESCRIPTION, 4), "name", NotBlank.class));
    }

    @Test
    void create_shouldRejectNameLongerThan255() {
        assertTrue(violates(create("a".repeat(256), VALID_DESCRIPTION, 4), "name", Size.class));
    }

    @Test
    void create_shouldRejectBlankDescription() {
        assertTrue(violates(create("Tarte", " ", 4), "description", NotBlank.class));
        assertTrue(violates(create("Tarte", null, 4), "description", NotBlank.class));
    }

    @Test
    void create_shouldRejectMissingServings() {
        assertTrue(violates(create("Tarte", VALID_DESCRIPTION, null), "servings", NotNull.class));
    }

    @Test
    void create_shouldRejectServingsBelowOne() {
        assertTrue(violates(create("Tarte", VALID_DESCRIPTION, 0), "servings", Min.class));
    }

    // --- UpdateRecipeRequest ---

    @Test
    void update_shouldRejectBlankNameAndTooLongName() {
        assertTrue(violates(update("", VALID_DESCRIPTION, 4), "name", NotBlank.class));
        assertTrue(violates(update("a".repeat(256), VALID_DESCRIPTION, 4), "name", Size.class));
    }

    @Test
    void update_shouldRejectBlankDescription() {
        assertTrue(violates(update("Tarte", "", 4), "description", NotBlank.class));
    }

    @Test
    void update_shouldRejectMissingOrInvalidServings() {
        assertTrue(violates(update("Tarte", VALID_DESCRIPTION, null), "servings", NotNull.class));
        assertTrue(violates(update("Tarte", VALID_DESCRIPTION, 0), "servings", Min.class));
    }

    @Test
    void update_shouldAcceptValidFields() {
        UpdateRecipeRequest r = update("Tarte", VALID_DESCRIPTION, 4);
        assertTrue(isValid(r, "name"));
        assertTrue(isValid(r, "description"));
        assertTrue(isValid(r, "servings"));
    }
}