package com.cedric.shoppingrecipes.exception;

import com.cedric.shoppingrecipes.ingredient.exception.IngredientConflictException;

import com.cedric.shoppingrecipes.recipe.execption.RecipConflictException;
import com.cedric.shoppingrecipes.shoppinglist.exception.RecipeNotFoundException;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IngredientConflictException.class)
    public ResponseEntity<String> handleIngredientConflict(IngredientConflictException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(RecipConflictException.class)
    public  ResponseEntity<String> handleRecipeConflict(RecipConflictException ex) {
        return  ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(RecipeNotFoundException.class)
    public  ResponseEntity<ErrorResponse> handleRecipeNotFoundException(RecipeNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                ex.getRecipeId()
        ));
    }
}