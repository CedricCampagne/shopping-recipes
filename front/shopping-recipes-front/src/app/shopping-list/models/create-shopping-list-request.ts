export interface CreateShoppingRequest {
    recipes: {
        recipeId: number;
        servings: number;
    } [];
}