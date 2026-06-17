package com.cedric.shoppingrecipes.ingredient.repository;


import com.cedric.shoppingrecipes.ingredient.entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

    @Query(
            value = "SELECT * FROM ingredient " +
                    "WHERE unaccent(LOWER(name)) = unaccent(LOWER(:name))",
            nativeQuery = true
    )
    Optional<Ingredient> findByName(String name);

    @Query(
            value = "SELECT * FROM ingredient " +
                    "WHERE unaccent(LOWER(unit)) = unaccent(LOWER(:unit))",
            nativeQuery = true
    )
    List<Ingredient> findByUnit(String unit);
}

