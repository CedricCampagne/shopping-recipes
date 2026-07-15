import { RecipeIngredientRequest } from "./recipe-ingredient-request";

export interface CreateRecipeRequest {
  name: string;
  description: string;
  servings: number;
  ingredients: RecipeIngredientRequest[];
}