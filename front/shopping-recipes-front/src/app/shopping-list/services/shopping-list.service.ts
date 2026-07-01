import { computed, Injectable, signal } from "@angular/core";
import { RecipeIngredient } from "../../recipes/models/recipe-ingredient";
import { CreateShoppingRequest } from "../models/create-shopping-list-request";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { ShoppingListResponse } from "../models/shoppin-list-response";

@Injectable({ providedIn:'root' })
export class shoppingListService {

    constructor(private http: HttpClient) {};
    private apiUrl = 'http://localhost:8080/shopping-lists';
    private uidCounter = 0;

    // Panier local : ingredients
    items = signal<RecipeIngredient[]>([]);

    // Panier local : recettes
    recipes = signal<{uid: number,recipeId: number; servings: number;name: string}[]>([]);

    savedLists = signal<ShoppingListResponse[]>([]);

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

    sortedMergedItems = computed(() => {
        return [...this.mergedItems()].sort((a, b) =>
            a.ingredient.name.localeCompare(b.ingredient.name)
        );
    });
    
    // ajout des recettes au panier
    addRecipe(recipeId: number, servings: number, name: string, ingredients: RecipeIngredient[]) {
        const uid = ++this.uidCounter;

        // 1) Ajouter la recette
        this.recipes.update(list => [
            ...list,
            { uid, recipeId, servings, name }
        ]);

        // 2) Ajouter les ingrédients avec le même UID
        const itemsWithUid = ingredients.map(i => ({
            ...i,
            recipeAddUid: uid
        }));

        this.items.update(list => [...list, ...itemsWithUid]);
    }

    // vider le panier
    clear(){
        this.items.set([]);
        this.recipes.set([]);
    }

    deleteRecipeById(recipeId: number) {
        let removed = false;
        this.recipes.update(list =>
            list.filter(r => {
            if (!removed && r.recipeId === recipeId) {
                removed = true;
                return false;
            }
            return true;
            })
        );
    }

    deleteRecipeAndItems(uid: number) {
        // 1) supprimer la recette
        this.recipes.update(list => list.filter(r => r.uid !== uid));

        // 2) supprimer les ingrédients associés
        this.items.update(list => list.filter(i => i.recipeAddUid !== uid));
    }

    updateServings(uid: number, newServings: number) {
        // 1. mettre a jour la recette
        this.recipes.update(list =>
            list.map(r =>
                r.uid === uid ? {...r, servings: newServings } : r
            )
        );

        // 2. recalculer les ingredietns liés a cette recette
        this.items.update(list =>
            list.map(i => {
                if (i.recipeAddUid === uid) {
                    const quantityPerPerson = i.quantityPerPerson;
                    return {
                        ...i,
                        total: quantityPerPerson * newServings
                    };
            }
            return i;
                }
            )
        );
    }

    // appel backend : creation de la liste de course
    createShoppingList(request: CreateShoppingRequest) {
        const token  = localStorage.getItem('token');
        const headers = new HttpHeaders({
            Authorization: `Bearer ${token}`
        });

        return this.http.post<ShoppingListResponse>(`${this.apiUrl}`, request, { headers });
    }

    // supprimmer une liste
    deleteList(id: number) {
        const token  = localStorage.getItem('token');
        const headers = new HttpHeaders({
            Authorization: `Bearer ${token}`
        });

        return this.http.delete(`${this.apiUrl}/${id}`, { headers });
    }
    
    // Recuperer toutes les list saved
    getAllSavedList() {
        const token  = localStorage.getItem('token');
        const headers = new HttpHeaders({
            Authorization: `Bearer ${token}`
        });

        return this.http.get<ShoppingListResponse[]>(`${this.apiUrl}`, { headers });
    }

    // rafraichir les lsites sauvegardées
    refreshSavedLists() {
        this.getAllSavedList().subscribe({
            next : (res) => this.savedLists.set(res)
        });
    }

    // Recuperer list saved by id
    getSavedListById(id: number) {
        const token  = localStorage.getItem('token');
        const headers = new HttpHeaders({
            Authorization: `Bearer ${token}`
        });

        return this.http.get<ShoppingListResponse>(`${this.apiUrl}/${id}`, { headers });
    }

    updateStatus(id: number, status: string) {
        const token = localStorage.getItem('token');
        const headers = new HttpHeaders({ Authorization: `Bearer ${token}` });

        return this.http.put(`${this.apiUrl}/${id}/status`, { status }, { headers });
    }

}