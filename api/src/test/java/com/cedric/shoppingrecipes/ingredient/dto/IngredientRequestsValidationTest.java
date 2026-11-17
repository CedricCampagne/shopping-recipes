package com.cedric.shoppingrecipes.ingredient.dto;

import com.cedric.shoppingrecipes.ingredient.Unit;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class IngredientRequestsValidationTest {

    private final Validator validator =
            Validation.buildDefaultValidatorFactory().getValidator();

    // ---- création ----

    @Test
    void createShouldAcceptValidRequest() {
        assertThat(validator.validate(new CreateIngredientRequest("Beurre", Unit.g))).isEmpty();
    }

    @Test
    void createShouldRejectBlankName() {
        assertThat(messagesOf(validator.validate(new CreateIngredientRequest("   ", Unit.g))))
                .containsExactly("Le nom de l'ingrédient est obligatoire");
    }

    @Test
    void createShouldRejectNameLongerThan50Characters() {
        String tooLong = "a".repeat(51);

        assertThat(messagesOf(validator.validate(new CreateIngredientRequest(tooLong, Unit.g))))
                .containsExactly("Le nom ne doit pas dépasser 50 caractères");
    }

    @Test
    void createShouldRejectNullUnit() {
        assertThat(messagesOf(validator.validate(new CreateIngredientRequest("Beurre", null))))
                .containsExactly("L'unité est obligatoire");
    }

    // ---- modification : mêmes règles ----

    @Test
    void updateShouldRejectBlankName() {
        assertThat(messagesOf(validator.validate(new UpdateIngredientRequest("", Unit.g))))
                .containsExactly("Le nom de l'ingrédient est obligatoire");
    }

    @Test
    void updateShouldRejectNullUnit() {
        assertThat(messagesOf(validator.validate(new UpdateIngredientRequest("Beurre", null))))
                .containsExactly("L'unité est obligatoire");
    }

    private <T> List<String> messagesOf(Set<ConstraintViolation<T>> violations) {
        return violations.stream().map(ConstraintViolation::getMessage).toList();
    }
}