export interface UpdateRecipeIngredientRequest {
    id: number;
    ingredientId: number;
    ingredientName: string;
    quantityPerPerson: number;
    unit: string;
}