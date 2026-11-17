package com.cedric.shoppingrecipes.shoppinglist.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class CreateShoppingListRequestTest {

    private final Validator validator =
            Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void shouldAcceptValidRequest() {
        CreateShoppingListRequest request = new CreateShoppingListRequest(
                List.of(new CreateShoppingListRequest.RecipeSelection(1L, 2)));

        assertThat(validator.validate(request)).isEmpty();
    }

    @Test
    void shouldRejectEmptyRecipeList() {
        CreateShoppingListRequest request = new CreateShoppingListRequest(List.of());

        assertThat(messagesOf(validator.validate(request)))
                .containsExactly("La liste des recettes ne peut pas être vide");
    }

    @Test
    void shouldRejectNullServings() {
        CreateShoppingListRequest request = new CreateShoppingListRequest(
                List.of(new CreateShoppingListRequest.RecipeSelection(1L, null)));

        assertThat(messagesOf(validator.validate(request)))
                .containsExactly("Le nombre de portions est obligatoire");
    }

    @Test
    void shouldRejectServingsBelowOne() {
        CreateShoppingListRequest request = new CreateShoppingListRequest(
                List.of(new CreateShoppingListRequest.RecipeSelection(1L, 0)));

        assertThat(messagesOf(validator.validate(request)))
                .containsExactly("Le nombre de portions doit être d'au moins 1");
    }

    @Test
    void shouldRejectMissingRecipeId() {
        CreateShoppingListRequest request = new CreateShoppingListRequest(
                List.of(new CreateShoppingListRequest.RecipeSelection(null, 2)));

        assertThat(messagesOf(validator.validate(request)))
                .containsExactly("L'identifiant de la recette est obligatoire");
    }

    private List<String> messagesOf(Set<ConstraintViolation<CreateShoppingListRequest>> violations) {
        return violations.stream().map(ConstraintViolation::getMessage).toList();
    }
}