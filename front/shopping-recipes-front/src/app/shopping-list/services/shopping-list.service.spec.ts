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
    });
});