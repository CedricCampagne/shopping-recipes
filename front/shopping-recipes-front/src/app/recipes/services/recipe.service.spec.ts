import { TestBed } from "@angular/core/testing";
import { provideHttpClient } from "@angular/common/http";
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { describe, expect, it } from "vitest";

import { RecipesServices } from "./recipes.service";
import { CreateRecipeRequest } from '../models/create-recipe-request';

describe('recipeService', ()=>{
    let service: RecipesServices;
    let httpTesting: HttpTestingController;

    beforeEach(()=>{
        TestBed.configureTestingModule({
            providers: [
                RecipesServices,
                provideHttpClient(),
                provideHttpClientTesting()
            ],
        });

        service =TestBed.inject(RecipesServices);
        httpTesting = TestBed.inject(HttpTestingController);
    });

    afterEach(() => {
        // Vérifie qu'il ne reste pas de requete HTTP en attente
        httpTesting.verify();
    });

    describe('createRecipe', ()=>{
        it('should send a POST request with the recipe payload', ()=>{
            //Arange
            const payload : CreateRecipeRequest = {
                name: 'Salade tomate',
                description: 'Une salade simple aux tomates',
                servings: 4,
                ingredients: [
                    {
                    ingredientId: 1,
                    quantityPerPerson: 2,
                    unit: 'piece',
                    },
                ],
            };

            let response: unknown;

            //Act
            service.createRecipe(payload).subscribe((res)=>{
                response = res;
            });

            //Assert
            const request = httpTesting.expectOne('http://localhost:8080/recipes');

            expect(request.request.method).toBe('POST');
            expect(request.request.body).toEqual(payload);

            request.flush({ id:1});

            expect(response).toEqual({id: 1});
        });

        it('should handle an error when creating a recipe', () => {
            const payload: CreateRecipeRequest = {
                name: 'Salade tomate',
                description: 'Une salade simple aux tomates',
                servings: 4,
                ingredients: [
                {
                    ingredientId: 1,
                    quantityPerPerson: 2,
                    unit: 'piece',
                },
                ],
            };

            let error: unknown;

            service.createRecipe(payload).subscribe({
                next: () => {},
                error: (err) => {
                error = err;
                },
            });

            const request = httpTesting.expectOne('http://localhost:8080/recipes');

            expect(request.request.method).toBe('POST');
            expect(request.request.body).toEqual(payload);

            request.flush('Erreur serveur', {
                status: 500,
                statusText: 'Internal Server Error',
            });

            expect((error as any).status).toBe(500);
        });
    });
});