export interface RecipeIngredientFront {
  index: number; // identifiant interne pour supprimer
  ingredientId: number;
  name: string;
  unit: string;
  quantityPerPerson: number;
}
