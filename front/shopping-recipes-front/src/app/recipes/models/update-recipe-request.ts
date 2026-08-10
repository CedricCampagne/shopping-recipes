import { UpdateRecipeIngredientRequest } from './update-recipe-ingredient-request';

export interface UpdateRecipeRequest {
  name: string;
  description: string;
  servings: number;
  ingredients: UpdateRecipeIngredientRequest[];
}
