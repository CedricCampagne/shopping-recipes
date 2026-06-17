package com.cedric.shoppingrecipes.ingredient.entity;

import com.cedric.shoppingrecipes.recipeingredient.entity.RecipeIngredient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "ingredient")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String name;

    @Column(nullable = false, length = 20)
    private String unit;

    @OneToMany(mappedBy = "ingredient")
    @JsonIgnore
    private List<RecipeIngredient> recipes;

}