import { computed, Injectable, signal } from "@angular/core";
import { RecipeIngredient } from "../../recipes/models/recipe-ingredient";


@Injectable({ providedIn:'root' })
export class shoppingListService {

    items = signal<RecipeIngredient[]>([]);

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
    addItems(newItems: RecipeIngredient[]){
        this.items.update(list => [...list, ...newItems]);
    }

    clear(){
        this.items.set([]);
    }
}