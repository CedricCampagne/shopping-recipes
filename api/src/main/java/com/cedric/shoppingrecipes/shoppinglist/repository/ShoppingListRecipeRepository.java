package com.cedric.shoppingrecipes.shoppinglist.repository;

import com.cedric.shoppingrecipes.shoppinglist.entity.ShoppingListRecipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingListRecipeRepository extends JpaRepository<ShoppingListRecipe, Long> {
}