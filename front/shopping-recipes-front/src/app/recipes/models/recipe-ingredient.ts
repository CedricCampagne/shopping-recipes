import { Recipe } from './recipe';
import { Ingredient } from './ingredient';

export interface RecipeIngredient {
  id: number;
  recipe: Recipe;
  ingredient: Ingredient;
  quantityPerPerson: number;
  unit: string;
  total: number
}
