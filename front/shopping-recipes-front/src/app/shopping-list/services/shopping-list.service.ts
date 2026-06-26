import { computed, Injectable, signal } from "@angular/core";
import { RecipeIngredient } from "../../recipes/models/recipe-ingredient";
import { CreateShoppingRequest } from "../models/create-shopping-list-request";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { ShoppingListResponse } from "../models/shoppin-list-response";

@Injectable({ providedIn:'root' })
export class shoppingListService {

    constructor(private http: HttpClient) {};
    private apiUrl = 'http://localhost:8080/shopping-lists';

    // Panier local : ingredients
    items = signal<RecipeIngredient[]>([]);

    // Panier local : recettes
    recipes = signal<{recipeId: number; servings: number;name: string}[]>([]);

    // fusion des ingredients
    mergedItems = computed(() => {
        const list = this.items();
        const map = new Map<string, RecipeIngredient>();

        for(const ing of list) {
            const key = ing.ingredient.id + '_' + ing.unit;

            if(!map.has(key)) {
                map.set(key, {...ing});
            }else {
                map.get(key)!.total += ing.total;
            }
        }

        return Array.from(map.values());

    });

    // ajout des ingredietns au panier
    addItems(newItems: RecipeIngredient[]){
        this.items.update(list => [...list, ...newItems]);
    }

    // ajout des recettes au panier
    addRecipe(recipeId: number, servings: number, name: string){
        this.recipes.update(list => [
            ...list,
            { recipeId, servings, name}
        ]);
    }

    // vider le panier
    clear(){
        this.items.set([]);
        this.recipes.set([]);
    }

    // appel backend : creation de la liste de course
    createShoppingList(request: CreateShoppingRequest) {
        const token  = localStorage.getItem('token');
        const headers = new HttpHeaders({
            Authorization: `Bearer ${token}`
        });

        return this.http.post<ShoppingListResponse>(`${this.apiUrl}`, request, { headers });
    }
}