package com.cedric.shoppingrecipes.user.dto;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

class UserRegisterRequestTest {

    private final Validator validator =
            Validation.buildDefaultValidatorFactory().getValidator();

    private Set<ConstraintViolation<UserRegisterRequest>> validatePassword(String password) {
        UserRegisterRequest request =
                new UserRegisterRequest("john", "john@mail.com", password);
        return validator.validateProperty(request, "password");
    }

    @Test
    void shouldAcceptValidPassword() {
        assertTrue(validatePassword("MotDePasse-1234").isEmpty());
    }

    @Test
    void shouldRejectTooShortPassword() {
        assertFalse(validatePassword("Court1!").isEmpty());
    }

    @Test
    void shouldRejectPasswordWithoutSpecialCharacter() {
        assertFalse(validatePassword("MotDePasse1234").isEmpty());
    }

    @Test
    void shouldRejectTooLongPassword() {
        assertFalse(validatePassword("Aaaaaaaaa!".repeat(8)).isEmpty());
    }

    @Test
    void shouldRejectNullPassword() {
        assertFalse(validatePassword(null).isEmpty());
    }
}