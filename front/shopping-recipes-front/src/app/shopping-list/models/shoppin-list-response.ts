import { ShoppingListItemResponse } from "./shopping-list-item-response";
import { ShoppingListRecipeResponse } from "./shopping-list-recipe-response";

export interface ShoppingListResponse {
    id: number;
    status: string;
    items: ShoppingListItemResponse[];
    recipes: ShoppingListRecipeResponse[];
}