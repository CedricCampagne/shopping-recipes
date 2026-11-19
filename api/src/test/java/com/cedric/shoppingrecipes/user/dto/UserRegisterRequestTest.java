package com.cedric.shoppingrecipes.user.dto;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.annotation.Annotation;
import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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

    private Set<ConstraintViolation<UserRegisterRequest>> validateUsername(String username) {
        UserRegisterRequest request =
                new UserRegisterRequest(username, "john@mail.com", "MotDePasse-1234");
        return validator.validateProperty(request, "username");
    }

    private Set<ConstraintViolation<UserRegisterRequest>> validateEmail(String email) {
        UserRegisterRequest request =
                new UserRegisterRequest("john", email, "MotDePasse-1234");
        return validator.validateProperty(request, "email");
    }

    private boolean violates(Set<ConstraintViolation<UserRegisterRequest>> violations,
                             Class<? extends Annotation> rule) {
        return violations.stream()
                .anyMatch(v -> rule.isInstance(v.getConstraintDescriptor().getAnnotation()));
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

    @Test
    void shouldAcceptValidUsernameUpTo50Characters() {
        assertTrue(validateUsername("john").isEmpty());
        assertTrue(validateUsername("a".repeat(50)).isEmpty());
    }

    @Test
    void shouldRejectBlankOrNullUsername() {
        assertTrue(violates(validateUsername(""), NotBlank.class));
        assertTrue(violates(validateUsername(null), NotBlank.class));
    }

    @Test
    void shouldRejectUsernameLongerThan50Characters() {
        assertTrue(violates(validateUsername("a".repeat(51)), Size.class));
    }

    @Test
    void shouldAcceptValidEmail() {
        assertTrue(validateEmail("john@mail.com").isEmpty());
    }

    @Test
    void shouldRejectBlankOrNullEmail() {
        assertTrue(violates(validateEmail(""), NotBlank.class));
        assertTrue(violates(validateEmail(null), NotBlank.class));
    }

    @Test
    void shouldRejectInvalidEmailFormat() {
        assertTrue(violates(validateEmail("pasunemail"), Email.class));
    }

    @Test
    void shouldRejectEmailLongerThan255Characters() {
        assertTrue(violates(validateEmail("a".repeat(250) + "@mail.com"), Size.class));
    }
}