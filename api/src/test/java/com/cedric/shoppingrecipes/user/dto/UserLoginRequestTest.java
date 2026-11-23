package com.cedric.shoppingrecipes.user.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

class UserLoginRequestTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    private Set<String> messages(UserLoginRequest request) {
        return validator.validate(request).stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toSet());
    }

    @Test
    void shouldRejectMissingEmail() {
        assertEquals(Set.of("L'email est obligatoire"),
                messages(new UserLoginRequest(null, "abc")));
    }

    @Test
    void shouldRejectBlankPassword() {
        assertEquals(Set.of("Le mot de passe est obligatoire"),
                messages(new UserLoginRequest("a@b.com", "   ")));
    }

    @Test
    void shouldRejectBothFieldsMissing() {
        assertEquals(Set.of("L'email est obligatoire", "Le mot de passe est obligatoire"),
                messages(new UserLoginRequest(null, null)));
    }

    @Test
    void shouldNotApplyRegistrationRulesAtLogin() {
        assertTrue(messages(new UserLoginRequest("pas-un-email", "abc")).isEmpty());
    }
}