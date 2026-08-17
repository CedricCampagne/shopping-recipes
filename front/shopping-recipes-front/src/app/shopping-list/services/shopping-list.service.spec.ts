import { TestBed } from "@angular/core/testing";
import { provideHttpClient } from "@angular/common/http";
import { describe, expect, it } from "vitest";

import { shoppingListService } from "./shopping-list.service"
import { RecipeIngredient } from "../../recipes/models/recipe-ingredient";
import  { Recipe } from "../../recipes/models/recipe";
import { Ingredient } from "../../recipes/models/ingredient"

describe('shoppingListService', ()=>{

    describe('mergedItems', ()=>{
        beforeEach(() => {
            TestBed.configureTestingModule({
            providers: [shoppingListService, provideHttpClient()],
            });
        });

        it('should return one ingredient when there is only one item', () => {
            // Arrange
            const service = TestBed.inject(shoppingListService);

            const recipe: Recipe = {
                id: 1,
                name: 'Salade',
                description: 'Une salade simple',
                servings: 4,
            };

            const tomato: Ingredient = {
                id: 1,
                name: 'Tomate',
                unit: 'piece',
            };

            const recipeIngredient: RecipeIngredient = {
                id: 1,
                recipe: recipe,
                ingredient: tomato,
                quantityPerPerson: 1,
                unit: 'piece',
                total: 4,
                recipeAddUid: 1,
            };

            // Act
            service.items.set([recipeIngredient]);

            // Assert
            expect(service.mergedItems()).toEqual([recipeIngredient]);
        });

        it('should merge items with the same ingredient and unit', ()=>{
            const service = TestBed.inject(shoppingListService);

            const recipe: Recipe = {
                id: 1,
                name: 'Salade',
                description: 'Une salade simple',
                servings: 4,
            };

            const tomato: Ingredient = {
                id: 1,
                name: 'Tomate',
                unit: 'piece',
            };

            const tomatoItem1: RecipeIngredient = {
                id: 1,
                recipe,
                ingredient: tomato,
                quantityPerPerson: 1,
                unit: 'piece',
                total: 4,
                recipeAddUid: 1,
            };

            const tomatoItem2: RecipeIngredient = {
                id: 2,
                recipe,
                ingredient: tomato,
                quantityPerPerson: 1,
                unit: 'piece',
                total: 3,
                recipeAddUid: 2,
            };

            // Act
            service.items.set([tomatoItem1, tomatoItem2]);

            // Assert
            expect(service.mergedItems()).toHaveLength(1);
            expect(service.mergedItems()[0].total).toBe(7);
        });

        it('should not merge items with the same ingredient but different units', () => {
            // Arrange
            const service = TestBed.inject(shoppingListService);

            const recipe: Recipe = {
                id: 1,
                name: 'Salade',
                description: 'Une salade simple',
                servings: 4,
            };

            const tomato: Ingredient = {
                id: 1,
                name: 'Tomate',
                unit: 'piece',
            };

            const tomatoPieces: RecipeIngredient = {
                id: 1,
                recipe,
                ingredient: tomato,
                quantityPerPerson: 1,
                unit: 'piece',
                total: 4,
                recipeAddUid: 1,
            };

            const tomatoGrams: RecipeIngredient = {
                id: 2,
                recipe,
                ingredient: tomato,
                quantityPerPerson: 125,
                unit: 'gramme',
                total: 500,
                recipeAddUid: 2,
            };

            // Act
            service.items.set([tomatoPieces, tomatoGrams]);

            // Assert
            expect(service.mergedItems()).toHaveLength(2);
        });

        it('should not merge different ingredients', () => {
            // Arrange
            const service = TestBed.inject(shoppingListService);

            const recipe: Recipe = {
                id: 1,
                name: 'Salade',
                description: 'Une salade simple',
                servings: 4,
            };

            const tomato: Ingredient = {
                id: 1,
                name: 'Tomate',
                unit: 'piece',
            };

            const onion: Ingredient = {
                id: 2,
                name: 'Oignon',
                unit: 'piece',
            };

            const tomatoItem: RecipeIngredient = {
                id: 1,
                recipe,
                ingredient: tomato,
                quantityPerPerson: 1,
                unit: 'piece',
                total: 4,
                recipeAddUid: 1,
            };

            const onionItem: RecipeIngredient = {
                id: 2,
                recipe,
                ingredient: onion,
                quantityPerPerson: 0.5,
                unit: 'piece',
                total: 2,
                recipeAddUid: 1,
            };

            // Act
            service.items.set([tomatoItem, onionItem]);

            // Assert
            expect(service.mergedItems()).toHaveLength(2);
        });

        it('should merge multiple items with the same ingredient and unit', () => {
            // Arrange
            const service = TestBed.inject(shoppingListService);

            const recipe: Recipe = {
                id: 1,
                name: 'Salade',
                description: 'Une salade simple',
                servings: 4,
            };

            const tomato: Ingredient = {
                id: 1,
                name: 'Tomate',
                unit: 'piece',
            };

            const tomatoItem1: RecipeIngredient = {
                id: 1,
                recipe,
                ingredient: tomato,
                quantityPerPerson: 0.5,
                unit: 'piece',
                total: 2,
                recipeAddUid: 1,
            };

            const tomatoItem2: RecipeIngredient = {
                id: 2,
                recipe,
                ingredient: tomato,
                quantityPerPerson: 0.75,
                unit: 'piece',
                total: 3,
                recipeAddUid: 2,
            };

            const tomatoItem3: RecipeIngredient = {
                id: 3,
                recipe,
                ingredient: tomato,
                quantityPerPerson: 1.25,
                unit: 'piece',
                total: 5,
                recipeAddUid: 3,
            };

            // Act
            service.items.set([tomatoItem1, tomatoItem2, tomatoItem3]);

            // Assert
            expect(service.mergedItems()).toHaveLength(1);
            expect(service.mergedItems()[0].total).toBe(10);
        });
    });

    // describe('addRecipe',()=>{

    // });
});