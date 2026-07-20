package com.cedric.shoppingrecipes.recipe.repository;


import com.cedric.shoppingrecipes.recipe.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    @Query(
            value = "SELECT * FROM recipe " +
                    "WHERE unaccent(LOWER(name)) = unaccent(LOWER(:name))",
            nativeQuery = true
    )
    Optional<Recipe> findByName(String name);

    @Query(
            value = "SELECT * FROM recipe " +
                    "WHERE unaccent(LOWER(description)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%')))",
            nativeQuery = true
    )
    List<Recipe> searchByDescription(String keyword);

    @Query("""
        select r
        from Recipe r
        left join fetch r.ingredients ri
        left join fetch ri.ingredient ing
        where r.id = :id
        """
    )
    Recipe findByIdWithIngredients(Long id);
}