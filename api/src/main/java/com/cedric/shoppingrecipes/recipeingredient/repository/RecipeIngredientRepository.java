package com.cedric.shoppingrecipes.recipeingredient.repository;


import com.cedric.shoppingrecipes.recipeingredient.entity.RecipeIngredient;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, Long> {

    List<RecipeIngredient> findByRecipeId(Long recipeId);
    List<RecipeIngredient> findByIngredientId(Long ingredientId);

}