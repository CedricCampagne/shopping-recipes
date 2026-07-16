export interface UpdateRecipeIngredientRequest {
    id: number | null;
    ingredientId: number;
    ingredientName: string;
    quantityPerPerson: number | null;
    unit: string;
}