package com.cedric.shoppingrecipes.exception;

import com.cedric.shoppingrecipes.ingredient.exception.IngredientConflictException;

import com.cedric.shoppingrecipes.ingredient.exception.IngredientNotFoundException;
import com.cedric.shoppingrecipes.recipe.exception.RecipeConflictException;
import com.cedric.shoppingrecipes.recipeingredient.exception.RecipeIngredientNotFoundException;
import com.cedric.shoppingrecipes.recipe.exception.RecipeNotFoundException;
import com.cedric.shoppingrecipes.shoppinglist.exception.ShoppingListNotFoundException;
import com.cedric.shoppingrecipes.user.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IngredientConflictException.class)
    public ResponseEntity<ErrorResponse> handleIngredientConflict(IngredientConflictException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                ex.getMessage(),
                ex.getIngredientId()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(IngredientNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleIngredientNotFound(IngredientNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                ex.getIngredientId()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(RecipeConflictException.class)
    public  ResponseEntity<ErrorResponse> handleRecipeConflict(RecipeConflictException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                ex.getMessage(),
                null
        );
        return  ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(RecipeNotFoundException.class)
    public  ResponseEntity<ErrorResponse> handleRecipeNotFoundException(RecipeNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                ex.getRecipeId()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(RecipeIngredientNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlerRecipeIngredientNotFoundException(
            RecipeIngredientNotFoundException ex
    ) {
       ErrorResponse error = new ErrorResponse(
               HttpStatus.NOT_FOUND.value(),
               ex.getMessage(),
               ex.getRecipeIngredientId()
       );
       return   ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(ShoppingListNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlerShoppinListNotFoundException( ShoppingListNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                ex.getShoppingListId()
        );
        return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlerUserNotFoundException(UserNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                null
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}